package com.cornuel

import com.cornuel.plugins.*
import io.ktor.http.*
import io.ktor.network.tls.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import java.io.File

fun main() {

    val keyStoreFile = File("C:\\Users\\ncornuel\\Desktop\\SERVER APPLI\\serverProjectInverter\\src\\main\\keystore\\keystore.jks")
    val keystore = generateCertificate(
        file = keyStoreFile,
        keyAlias = "selfsigned",
        keyPassword = "mypass",
        jksPassword = "mypass"
    )

    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
            host = "0.0.0.0"
        }

        sslConnector(
            keyStore = keystore,
            keyAlias = "selfsigned",
            keyStorePassword = { "mypass".toCharArray() },
            privateKeyPassword = { "mypass".toCharArray() }) {
            port = 8443
            host = "0.0.0.0"
            keyStorePath = keyStoreFile
        }
        module {
            configureCors()
            configureSecurity()
            configureSerialization()
            configureRouting()
            configureSockets()

        }
    }
    embeddedServer(Netty, environment).start(wait = true)
}
