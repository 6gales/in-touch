package tech.intouch.network.tcp.server

import tech.intouch.network.Network
import tech.intouch.network.User
import tech.intouch.network.tcp.MESSAGE_TYPE
import tech.intouch.network.tcp.readPacket
import tech.intouch.network.tcp.sendPacket
import java.net.Socket

class ClientHandler(val network: Network, val socket: Socket) : Runnable {
    val outputStream = socket.getOutputStream()
    val inputStream = socket.getInputStream()
    override fun run() {
        while (socket != null) {
            val packet = readPacket(inputStream)
            when (packet[0]) {
                MESSAGE_TYPE.PROFILE.id -> {
                    val profile = ByteArray(packet.size - 1)
                    System.arraycopy(packet, 1, profile, 0, packet.size - 1)
                    network.updateUser(User(socket.inetAddress, profile))
                    sendPacket(profile, outputStream, MESSAGE_TYPE.PROFILE)
                }

                MESSAGE_TYPE.MESSAGE.id -> {
                    val message = ByteArray(packet.size - 1)
                    System.arraycopy(packet, 1, message, 0, packet.size - 1)
                    network.messageCallback(message)
                }
            }
        }
    }
}