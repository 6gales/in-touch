package tech.intouch.network.data

import java.net.InetAddress

data class User(var address: InetAddress, var profile: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (address != other.address) return false
        if (!profile.contentEquals(other.profile)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + profile.contentHashCode()
        return result
    }
}