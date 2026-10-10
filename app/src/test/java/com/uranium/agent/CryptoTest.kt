package com.uranium.agent

import com.uranium.agent.crypto.DualCounterCipher
import org.junit.Assert.*
import org.junit.Test

class CryptoTest {
    @Test
    fun testEncryptionDecryption() {
        val psk = "12345678901234567890123456789012".toByteArray()
        val cipher = DualCounterCipher(psk)
        val message = "Uranium Military Secure Message".toByteArray()
        val encrypted = cipher.encrypt(message)
        val decrypted = cipher.decrypt(encrypted)
        assertArrayEquals(message, decrypted)
    }
}
