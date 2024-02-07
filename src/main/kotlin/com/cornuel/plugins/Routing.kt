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
                val userJSON = call.receive<UserModel>()
                var obj = queries.isLoginCorrect(userJSON)


                when (obj != null) {
                    true -> {
                        call.respond(HttpStatusCode.OK, obj)
                    }

                    false -> {
                        call.respond(HttpStatusCode.BadRequest, "Invalid credentials")

                    }
                }

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !")

            }
        }

        post("/insertinverter") {

            try {
                val inverter = call.receive<AddInverterModel>()

                queries.insertSettingsInverter(inverter.un!!, inverter.deux!!, inverter.trois!!)
                queries.insertWarningInverter(inverter.wUn!!, inverter.wDeux!!, inverter.wTrois!!)
                queries.insertValuesInverter(inverter.kilowatter!!, inverter.volts!!, inverter.batteryPercentage!!)
                queries.insertInverter(inverter.name!!, inverter.macAddress!!, inverter.position!!, inverter.isOnline!!)

                var settings_id = queries.getLastSettingsID()
                var warnings_id = queries.getLastWarningsID()
                var values_id = queries.getLastValuesID()
                var inverter_id = queries.getLastInverterID()

                queries.insertLinkTable(inverter_id!!, values_id!!, settings_id!!, warnings_id!!)

                call.respond(HttpStatusCode.OK, "L'onduleur à bien été créé !")

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !" + e.message!!)

            }
        }

        post("/insertuser") {

            try {
                val user = call.receive<AddUserModel>()

                if (queries.isMacCorrect(user.macAddress)) {
                    queries.insertUser(user.email!!, user.login!!, user.password!!, user.isAdmin!!, user.createdAt!!)

                    var user_id = queries.getLastUserID()
                    var link_id = queries.getLinkIdWithMacAddress(user.macAddress!!)

                    queries.updateLinkTable(user_id!!, link_id!!)

                    call.respond(HttpStatusCode.OK, "L'utilisateur à bien été créé !")

                } else {
                    call.respond(HttpStatusCode.BadRequest, "L'adresse MAC renseignée n'existe pas !")
                }

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !" + e.message!!)

            }

        }

        post("/insertearning") {

            try {
                val earning = call.receive<AddEarningsModel>()
                var inverter_id = 0

                if (queries.isMacCorrect(earning.macAddress)) {

                    inverter_id = queries.getInverterIdWithMacAddress(earning.macAddress!!)!!

                } else {
                    call.respond(HttpStatusCode.BadRequest, "L'adresse MAC renseignée n'existe pas !")
                }


                try {

                    queries.insertEarnings(inverter_id, earning.date!!, earning.perHour!!)

                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Les gains n'ont pas été ajoutés, un problème à été rencontré !"
                    )
                }

                call.respond(HttpStatusCode.OK, "Les gains ont bien été ajoutés !")

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
            }

        }

        post("/modifyinverter") {

        }

        post("/modifyprice") {
            try {
                val price = call.receive<ModifyPriceModel>()

                queries.updatePrice(price.price!!)

                call.respond(HttpStatusCode.OK, "Le prix à bien été modifié !")

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
            }
        }

        post("/modifyuser") {

        }

    }
}
