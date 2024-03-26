package com.cornuel.plugins

import com.cornuel.bdd.services.Queries
import com.cornuel.models.AllValues
import com.cornuel.models.IdClient
import com.cornuel.models.Price
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(0)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }


    routing {

        val queries = Queries()

        webSocket("getalluserid") {
            var oldAr_Valeur: ArrayList<IdClient>? = null
            var ar_Valeur: ArrayList<IdClient>?
            while (true) {
                ar_Valeur = queries.getAllUserId()
                if (ar_Valeur != oldAr_Valeur) {
                    oldAr_Valeur = ar_Valeur
                    sendSerialized(ar_Valeur)
                }
                delay(1000)
            }
        }

        webSocket("getuservalues/{id}") {

            var oldValeur: AllValues? = null
            var flag = false


            while (true) {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    send(Frame.Text("Erreur sur paramètre!"))
                    return@webSocket
                }
                val valeur: AllValues? = queries.getValuesUser(id)
                if (valeur == null && flag == false) {
                    send(Frame.Text("Aucune correspondance pour l'utilisateur $id!"))
                    flag = true
                } else {
                    if (valeur != oldValeur) {

                        sendSerialized(valeur)

                        oldValeur = valeur
                    }
                }
                delay(1000)
            }
        }

        webSocket("getprice") {
            var oldPrice: Price? = null
            var price: Price?

            while (true){
                price = queries.getPrice()

                if(price != oldPrice){
                    oldPrice = price
                    sendSerialized(price)
                }
                delay(1000)
            }
        }

    }


}
