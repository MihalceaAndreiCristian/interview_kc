package com.finance.token.service

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.io.File
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.*

object JwtSigner {

    fun signAssertion(clientId: String, tokenEndpoint: String, privateKeyPath: String): String {
        val privateKey = loadPrivateKey(privateKeyPath)

        val now = Instant.now()
        val claims = JWTClaimsSet.Builder()
            .issuer(clientId)
            .subject(clientId)
            .audience(tokenEndpoint)
            .jwtID(UUID.randomUUID().toString())
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusSeconds(60)))
            .build()

        val signer = RSASSASigner(privateKey)
        val signedJWT = SignedJWT(
            JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
            claims
        )
        signedJWT.sign(signer)

        return signedJWT.serialize()
    }

    private fun loadPrivateKey(path: String): RSAPrivateKey {
        val keyBytes = File(path).readBytes()
            .toString(Charsets.UTF_8)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")
            .let { Base64.getDecoder().decode(it) }

        val spec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePrivate(spec) as RSAPrivateKey
    }
}
