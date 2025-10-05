package processor

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.io.File
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.util.Date

class JwtBean {
    fun signJwt(code: String): String {
        val privateKey = loadPrivateKey("keys/private.pem")
        val algorithm = Algorithm.RSA256(null, privateKey)
        return JWT.create()
            .withIssuer("token-service")
            .withClaim("code", code)
            .withAudience("keycloak")
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(algorithm)
    }

    private fun loadPrivateKey(path: String): RSAPrivateKey {
        val pem = File(path).readText()
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(pem))
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}