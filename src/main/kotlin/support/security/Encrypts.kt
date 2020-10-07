package support.security

import java.security.MessageDigest

private val SHA256 = MessageDigest.getInstance("SHA-256")

fun sha256Encrypt(plainText: String): String = String(SHA256.digest(plainText.toByteArray()))
