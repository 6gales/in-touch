package tech.intouch.network.tcp

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer


fun readPacket(inputStream: InputStream): ByteArray {
    val size = readPacketSize(inputStream)
    return read(inputStream, size)
}

fun readPacketSize(inputStream: InputStream): Int {
    val sizeArray = read(inputStream, 8)
    val dbuf: ByteBuffer = ByteBuffer.wrap(sizeArray)
    return dbuf.int;
}

fun read(inputStream: InputStream, size: Int): ByteArray {
    var n = 0;
    val byteArray = ByteArray(size)
    while(n != size) {
        n = inputStream.read(byteArray, n, size - n)
    };
    return byteArray;
}

enum class MESSAGE_TYPE(val id: Byte) {
    PROFILE(0),
    MESSAGE(1)
}

//это нужно выделить в отедльный класс
fun recvProfile(inputStream: InputStream): ByteArray {
    val packet = readPacket(inputStream)
    val profile = ByteArray(packet.size - 1)
    System.arraycopy(packet, 1, profile, 0, packet.size - 1)
    return profile
}

fun sendPacket(profileInfo: ByteArray, outputStream: OutputStream, type : MESSAGE_TYPE) {
    //с листами медленно получается
    val toSend = mutableListOf<Byte>()
    val dbuf: ByteBuffer = ByteBuffer.allocate(8)
    dbuf.putInt(profileInfo.size + 1)
    val size = dbuf.array().toList()

    toSend.addAll(size)
    toSend.add(type.id)
    toSend.addAll(toSend)
    outputStream.write(toSend.toByteArray())
}