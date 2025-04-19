package com.barcommerce.barcommerce.model.session;

/**
 * Representa uma sessão temporária de acesso à mesa via QR.
 */
public class MesaSession {

    private final Long mesaId;
    private final String deviceId;
    private final boolean anfitriao;

    public MesaSession(Long mesaId, String deviceId, boolean anfitriao) {
        this.mesaId = mesaId;
        this.deviceId = deviceId;
        this.anfitriao = anfitriao;
    }

    public Long getMesaId() {
        return mesaId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isAnfitriao() {
        return anfitriao;
    }
}
