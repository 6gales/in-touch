package tech.intouch.network.tcp.communication_cases

import tech.intouch.network.tcp.MESSAGE_TYPE
import tech.intouch.network.tcp.recvProfile
import tech.intouch.network.tcp.sendPacket
import java.net.InetAddress
import java.net.Socket

class ExchangeProfiles(val profileInfo: ByteArray, val groupOwnerAddress: InetAddress) {
    fun exchange(): ByteArray {
        val socket = Socket(groupOwnerAddress, 8888)
        val inputStream = socket.getInputStream()
        val outputStream = socket.getOutputStream()
        sendPacket(profileInfo, outputStream, MESSAGE_TYPE.PROFILE)
        return recvProfile(inputStream)
    }
}