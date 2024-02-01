package com.cornuel.plugins

import com.cornuel.bdd.services.Queries
import com.cornuel.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        var queries = Queries()

        post("/login") {

            try {
                val user = call.receive<UserModel>()

                if (queries.loginAvailable(user)) {

                    if (queries.isUserAdmin(user) == true) {
                        call.respond(HttpStatusCode.OK, "Login admin successful")
                    } else {
                        call.respond(HttpStatusCode.OK, "Login user successful")
                    }

                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid credentials")
                }

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !")

            }
        }

        post("/insertsettings") {

            try {
                val settings = call.receive<AddSettingsInverterModel>()

                queries.insertSettingsInverter(settings)

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !")

            }
        }

        post("/insertwarnings") {

            try {
                val warnings = call.receive<AddWarningsInverterModel>()

                queries.insertWarningInverter(warnings)

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !")

            }
        }

        post("/insertvaluesinverter") {

            try {
                val values = call.receive<AddValuesInverterModel>()

                queries.insertValuesInverter(values)

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !")

            }
        }

        post("/insertinverter") {

            try {
                val inverter = call.receive<AddInverterModel>()

                try {
                    inverter.settings_id = queries.getLastSettingsID()
                    //inverter.warnings_id = queries.getLastWarningsID()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict, "NUL ")
                }

                queries.insertInverter(inverter)

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !")

            }
        }

        post("postvaleur") {
            val parameters = call.receiveParameters()

            val paramVal1 = parameters["param1"]
            val paramVal2 = parameters["param2"]
        }

    }
}
