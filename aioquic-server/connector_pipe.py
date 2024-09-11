
import os
import asyncio

from typing import List, Dict
from app_logs import getLogger
Log = getLogger(__name__)

class Client:
    def __init__(self, uid, web_transport, data):
        self.uid = uid
        self.username = f'user-{uid}'
        self.unreliableFastWT = web_transport
        self.wt_latency = -1
        self.data = data

    def set_uid(self, uid):
        self.uid = uid
        self.username = f'user-{uid}'

    def set_username(self, username):
        self.username = username

    def __str__(self):
        return f'Client(uid = {self.uid}, WT = {id(self.unreliableFastWT)})'



clients: List[Client] = []
# Define the path for the named pipe
fifo_path_read = '/tmp/to_python_named_pipe'
fifo_path_write = '/tmp/from_python_named_pipe'

# Create the named pipe if it doesn't exist
if not os.path.exists(fifo_path_read):
    os.mkfifo(fifo_path_read)
if not os.path.exists(fifo_path_write):
    os.mkfifo(fifo_path_write)

async def listen_to_pipe():
    print(f"Listening to named pipe: {fifo_path_read}")
    with open(fifo_path_read, 'r') as fifo:
        while True:
            data = await asyncio.to_thread(fifo.readline)
            if data:
                from_connector(data)

def write_to_pipe(msg: str):
    with open(fifo_path_write, 'w') as fifo:
        fifo.write(f'{msg}\n')

def set_game_client_communication_web_transport(c_uid: int, web_transport) -> Client:
    for client in clients:
        if client.uid == c_uid:
            client.unreliableFastWT = web_transport
            return client

def from_connector(msg: str):
    print(f"from_connector: {msg}")
    # if client is None:
    #     client = Client(int(msg.split('.')[1]), None, None)
    #     clients.append(client)

def to_connector(msg, client: Client):
    print(f"to_connector: {msg}")
    if client is not None:
        write_to_pipe(f'{client.uid}.{msg}')
    # Log.info(msg)
    pass