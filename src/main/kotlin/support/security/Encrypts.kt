package support.security

import java.security.MessageDigest

private val SHA256: MessageDigest = MessageDigest.getInstance("SHA-256")

fun sha256Encrypt(plainText: String): String = bytesToHex(SHA256.digest(plainText.toByteArray()))

private fun bytesToHex(bytes: ByteArray): String =
    bytes.fold("") { previous, current -> previous + "%02x".format(current) }
