package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusMesa;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.model.session.MesaSession;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import com.barcommerce.barcommerce.repository.MesaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Serviço responsável por CRUD de mesas, sessões temporárias e validação de proximidade.
 */
@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;

    /** Em memória: associa deviceId → MesaSession */
    private final ConcurrentMap<String, MesaSession> sessaoMap = new ConcurrentHashMap<>();

    /** Raio máximo (em km) para check‑in via QR Code */
    private static final double MAX_DISTANCE_KM = 1.0;

    public MesaService(MesaRepository mesaRepository,
                       ClienteRepository clienteRepository) {
        this.mesaRepository = mesaRepository;
        this.clienteRepository = clienteRepository;
    }

    // ----------------------- CRUD de Mesa -----------------------

    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    public Mesa criarMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public Mesa atualizarStatusMesa(Long id, StatusMesa novoStatus) {
        Mesa m = mesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada"));
        m.setStatus(novoStatus);
        return mesaRepository.save(m);
    }

    public void deletarMesa(Long id) {
        mesaRepository.deleteById(id);
    }

    public Optional<Mesa> buscarPorId(Long id) {
        return mesaRepository.findById(id);
    }

    // ----------------------- Check‑in / Sessão -----------------------

    /**
     * Valida se o dispositivo (cliente) está dentro de 1 km da mesa.
     * Se não vier latitude/longitude (null), pula a validação.
     *
     * @param mesaId   ID da mesa
     * @param clientLat latitude do cliente (pode ser null)
     * @param clientLon longitude do cliente (pode ser null)
     */
    public void validarDistancia(Long mesaId, Double clientLat, Double clientLon) {
        // se não enviou coords, apenas não valida
        if (clientLat == null || clientLon == null) {
            return;
        }

        // busca a mesa e supõe que ela tem os atributos latitude e longitude
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada"));

        // calcula distância em km
        double dist = haversine(
                mesa.getLatitude(), mesa.getLongitude(),
                clientLat, clientLon
        );

        if (dist > MAX_DISTANCE_KM) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    String.format("Fora do raio de %.1f km (distância=%.2f km)",
                            MAX_DISTANCE_KM, dist)
            );
        }
    }

    /**
     * Haversine formula: distância entre dois pontos geográficos em km.
     */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // raio da Terra em km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Atribui uma mesa a um cliente (check‑in no banco).
     */
    public void atribuirCliente(Long mesaId, Long clienteId) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada"));
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        cliente.setMesa(mesa);
        clienteRepository.save(cliente);
    }

    /**
     * Registra sessão temporária do dispositivo (front deve gerar deviceId único).
     */
    public void registrarSessao(Long mesaId, String deviceId, boolean anfitriao) {
        sessaoMap.put(deviceId, new MesaSession(mesaId, deviceId, anfitriao));
    }

    /**
     * Libera a mesa: somente o anfitrião daquela sessão pode chamar.
     */
    public void liberarMesa(Long mesaId, String deviceId) {
        MesaSession session = sessaoMap.get(deviceId);
        if (session == null
                || !session.isAnfitriao()
                || !session.getMesaId().equals(mesaId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas o anfitrião pode liberar a mesa."
            );
        }
        sessaoMap.remove(deviceId);
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada"));
        mesa.setStatus(StatusMesa.LIVRE);
        mesaRepository.save(mesa);
    }

}
