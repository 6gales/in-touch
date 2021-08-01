package tech.intouch.network.tcp.server

import tech.intouch.network.Network
import java.net.ServerSocket

class Server(val network: Network) : Runnable {
    override fun run() {
        val serverSocket = ServerSocket(8888)
        while(true) {
            val socket = serverSocket.accept()
            Thread(ClientHandler(network, socket)).start()
        }
    }
}