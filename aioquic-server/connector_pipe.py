from typing import List, Dict

class Client:
    def __init__(self, uid, web_transport, websocket, data):
        self.uid = uid
        self.username = f'user-{uid}'
        self.reliableWS = websocket
        self.unreliableFastWT = web_transport
        self.wt_latency = -1
        self.data = data

    def set_uid(self, uid):
        self.uid = uid
        self.username = f'user-{uid}'

    def set_username(self, username):
        self.username = username

    def __str__(self):
        return f'Client(uid = {self.uid}, WS = {id(self.reliableWS)}, WT = {id(self.unreliableFastWT)})'



clients: List[Client] = []

def set_game_client_communication_web_transport(c_uid: int, web_transport) -> Client:
    for client in clients:
        if client.uid == c_uid:
            client.unreliableFastWT = web_transport
            return client
        
def to_game_server():
    pass