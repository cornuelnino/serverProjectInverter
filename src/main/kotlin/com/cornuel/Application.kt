package com.cornuel

import com.cornuel.plugins.*
import io.ktor.http.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import java.io.File

fun main() {

    val keyStoreFile = File("keystore\\keystore.jks")
    val keystore = generateCertificate(
        file = keyStoreFile,
        keyAlias = "cornuel",
        keyPassword = "inverter",
        jksPassword = "inverter"
    )

    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
            host = "10.0.0.172"
        }

        sslConnector(
            keyStore = keystore,
            keyAlias = "cornuel",
            keyStorePassword = { "inverter".toCharArray() },
            privateKeyPassword = { "inverter".toCharArray() }) {
            port = 8443
            host = "10.0.0.172"
            keyStorePath = keyStoreFile
        }
        module {
            install(CORS) {
                allowMethod(HttpMethod.Options)
                allowMethod(HttpMethod.Post)
                allowHeader(HttpHeaders.Authorization)
                allowHeader(HttpHeaders.ContentType)
                allowCredentials = true
                allowNonSimpleContentTypes = true
                anyHost()
            }

            configureSerialization()
            configureRouting()
            configureSockets()

        }
    }
    embeddedServer(Netty, environment).start(wait = true)
}
