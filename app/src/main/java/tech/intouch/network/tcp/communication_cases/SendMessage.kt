package tech.intouch.network.tcp.communication_cases

import tech.intouch.network.tcp.MESSAGE_TYPE
import tech.intouch.network.tcp.sendPacket
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket

class SendMessage(val groupOwnerAddress: InetAddress,val message: ByteArray): Runnable {
    override fun run() {
        val socket = Socket(groupOwnerAddress, 8888)
        val outputStream = socket.getOutputStream()
        sendMessage(message, outputStream)
    }

    private fun sendMessage(message: ByteArray, outputStream: OutputStream) {
        sendPacket(message, outputStream, MESSAGE_TYPE.MESSAGE)
    }

}