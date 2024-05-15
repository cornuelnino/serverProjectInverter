package com.cornuel.plugins

import com.cornuel.myRealm
import com.cornuel.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*


fun Application.configureSecurity() {
    val tokenManager = TokenManager()
    authentication {
        jwt("auth-jwt") {
            //définir le domaine à transmettre dans l' WWW-Authenticateen-tête lors de l'accès à une route protégée
            realm = myRealm
            //La verifierfonction permet de vérifier un format de token et sa signature :
            //Pour HS256, vous devez transmettre une instance de JWTVerifier pour vérifier un jeton.
            verifier(tokenManager.verifyJWTToken())

            //La validate fonction vous permet d'effectuer des validations supplémentaires sur la charge utile JWT
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("email").asString().isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
            //La challengefonction vous permet de configurer une réponse à envoyer en cas d'échec de l'authentification
            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token ${HttpHeaders.WWWAuthenticate} $defaultScheme realm=\"$realm\" invalide ou expiré"
                )
            }
        }

        basic("authbdd") {
            realm = "Access to the '/savebdd' path"
            validate { credentials ->
                if (credentials.name == "admin" && credentials.password == "Aubenas2023") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

}