package com.cornuel.plugins

import com.cornuel.bdd.services.Queries
import com.cornuel.models.UserModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        var queries = Queries()

        post("/login") {
            val user = call.receive<UserModel>()

            if(queries.loginAvailable(user)){
                call.respond(HttpStatusCode.OK, "Login successful")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid credentials")
            }
        }

        post("postvaleur") {
            val parameters = call.receiveParameters()

            val paramVal1 = parameters["param1"]
            val paramVal2 = parameters["param2"]
        }

    }
}
