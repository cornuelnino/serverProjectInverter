package com.cornuel

import com.cornuel.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*

fun main() {
    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
            host = "10.0.0.172"
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
