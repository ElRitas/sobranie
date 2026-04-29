package com.example.app.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date
import java.util.UUID

object JwtConfig {
    private const val SECRET = "super_secret_key"
    private const val ISSUER = "ktor.io"
    private const val AUDIENCE = "ktorAudience"
    private const val VALIDITY = 36_000_00 * 24 // 24 часа

    private val algorithm = Algorithm.HMAC256(SECRET)

    fun generateToken(
        userId: UUID,
        role: String,
    ): String =
        JWT.create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim("userId", userId.toString())
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY))
            .sign(algorithm)

    fun configureKtorFeature(config: JWTAuthenticationProvider.Config) {
        config.verifier(
            JWT.require(algorithm)
                .withAudience(AUDIENCE)
                .withIssuer(ISSUER)
                .build(),
        )
        config.validate { credential ->
            if (credential.payload.getClaim("userId")
                    .asInt() != null
            ) {
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }
    }
}
