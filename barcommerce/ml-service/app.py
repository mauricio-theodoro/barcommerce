from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/recommend', methods=['GET'])
def recommend():
    cliente_id = request.args.get('clienteId', type=int)
    limit = request.args.get('limit', type=int)
    # Stub simples: retorna [1,2,…,limit]
    return jsonify(list(range(1, (limit or 0) + 1)))

@app.route('/health', methods=['GET'])
def health():
    return 'OK', 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
