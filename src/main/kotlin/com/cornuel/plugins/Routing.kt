package com.cornuel.plugins

import com.cornuel.bdd.services.Queries
import com.cornuel.models.*
import com.cornuel.models.jwt.SendToken
import com.cornuel.models.jwt.UserLoginCredentials
import com.cornuel.models.jwt.UserRegisterCredentials
import com.cornuel.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

// Fonction de configuration du routage de l'application
fun Application.configureRouting() {
    routing {

        // Création d'une instance de la classe Queries pour interagir avec la base de données
        val queries = Queries()

        // Gestion de la route GET "/l"
        get("/l") {

            // Hachage et affichage de mots de passe pour les tests
            val password1 = BCrypt.hashpw("test1", BCrypt.gensalt())
            println(password1)
            val password2 = BCrypt.hashpw("test2", BCrypt.gensalt())
            println(password2)
            val password3 = BCrypt.hashpw("test3", BCrypt.gensalt())
            println(password3)
            val password4 = BCrypt.hashpw("test4", BCrypt.gensalt())
            println(password4)
            val password5 = BCrypt.hashpw("test555", BCrypt.gensalt())
            println(password5)

        }

        // Gestion de la route POST "/login"
        post("/login") {

            // Création d'une instance de TokenManager pour la gestion des jetons JWT
            val tokenManager = TokenManager()

            try {
                // Réception des informations de connexion de l'utilisateur
                val userLoginCredentials = call.receive<UserLoginCredentials>()

                // Vérification de la validité des informations de connexion
                if (!userLoginCredentials.isValidCredentials()) {
                    call.respond(HttpStatusCode.BadRequest, "Format du login ou/et password incorrect.")
                    return@post
                }

                val email = userLoginCredentials.email
                val password = userLoginCredentials.password

                // Vérification de l'existence de l'utilisateur dans la base de données
                val user = queries.userExist(email)
                if (user == null) {
                    call.respond(HttpStatusCode.BadRequest, "Cet utilisateur n'existe pas !.")
                    return@post
                }

                // Vérification de la correspondance du mot de passe haché
                val passwordMatch = BCrypt.checkpw(password, user.password)
                if (!passwordMatch) {
                    call.respond(HttpStatusCode.BadRequest, "Username ou password invalide.")
                    return@post
                }
                // Génération du jeton JWT et envoi de la réponse avec le jeton
                val token = SendToken(user.iduser, user.email, user.name, user.isAdmin, tokenManager.generateJWTToken(user))
                call.respond(HttpStatusCode.OK, token)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Format du JSON incorrect.")
            }
        }

        // Gestion de la route POST "/register"
        post("/register"){

            try {
                // Réception des informations d'enregistrement de l'utilisateur
                val userRegisterCredentials = call.receive<UserRegisterCredentials>()

                // Vérification de la validité des informations d'enregistrement
                if (!userRegisterCredentials.isValidCredentials()) {
                    call.respond(HttpStatusCode.BadRequest, "Format du login ou/et password incorrect.")
                    return@post
                }

                val email = userRegisterCredentials.email
                val name = userRegisterCredentials.name
                val macAddress = userRegisterCredentials.macAddress

                // Vérification de l'existence de l'utilisateur dans la base de données
                val user = queries.userExist(email)
                if (user != null) {
                    call.respond(HttpStatusCode.BadRequest, "Cet utilisateur existe déjà !")
                    return@post
                }

                // Vérification de l'existence de l'onduleur avec l'adresse MAC spécifiée
                val inverter = queries.inverterExist(macAddress)
                if (inverter == null) {
                    call.respond(HttpStatusCode.BadRequest, "L'adresse MAC ne correspond à aucun onduleur !")
                    return@post
                } else if (inverter.user_iduser != 0) {
                    call.respond(HttpStatusCode.BadRequest, "Cet onduleur appartient déjà à un client !")
                    return@post
                }

                // Insertion de l'utilisateur dans la base de données
                queries.insertUser(email, name, userRegisterCredentials.hashedPassword())

                // Récupération de l'ID de l'utilisateur récemment inséré
                val lastUserID = queries.getLastUserID()

                // Mise à jour de l'ID utilisateur de l'onduleur
                queries.updateUserIDInverter(inverter.idinverter, lastUserID!!)

                call.respond(HttpStatusCode.OK, "Votre compte utilisateur a bien été créé !")

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Format du JSON incorrect.")
            }
        }

        authenticate("auth-jwt") {

            // Gestion de la route POST "/insertinverter"
            post("/insertinverter") {

                try {
                    // Réception des informations d'ajout d'un onduleur
                    val inverter = call.receive<AddInverterModel>()

                    // Insertion des paramètres et avertissements de l'onduleur dans la base de données
                    queries.insertSettingsInverter(inverter.gridVoltage!!, inverter.gridFrequency!!, inverter.ACoutputVoltage!!, inverter.ACoutputFrequency!!, inverter.ACoutputApparentPower!!, inverter.ACoutputActivePower!!, inverter.BUSvoltage!!, inverter.batteryVoltage!!, inverter.batteryChargingCurrent!!, inverter.batteryCapacity!!, inverter.inverterHeatSinkTemperature!!, inverter.PVinputCurrent!!, inverter.PVinputVoltage!!, inverter.batteryVoltageSCC!!, inverter.batteryDischargeCurrent!!, inverter.deviceStatus!!)
                    queries.insertWarningInverter(inverter.lineFail!!, inverter.OPVShort!!, inverter.batteryLowAlarm!!, inverter.EEPROMdefault!!, inverter.powerLimit!!, inverter.highPVvoltage!!, inverter.MPPTOverloadFault!!, inverter.MPPTOverloadWarning!!, inverter.batteryLowToCharge!!)

                    // Récupération des derniers ID de paramètres et d'avertissements insérés
                    val warningsid = queries.getLastWarningsID()
                    val settingsid = queries.getLastSettingsID()

                    // Insertion de l'onduleur avec les paramètres et les avertissements dans la base de données
                    queries.insertInverter(inverter.name!!, inverter.macAddress!!, inverter.position!!, inverter.isOnline!!, inverter.batteryPercentage!!, warningsid!!, settingsid!!)

                    call.respond(HttpStatusCode.OK, "L'onduleur a bien été créé !")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !" + e.message!!)
                }
            }

            // Gestion de la route POST "/modifyinverter"
            post("/modifyinverter") {
                try {
                    // Réception des informations de modification de l'onduleur
                    val modifyInverterModel = call.receive<ModifyInverterModel>()
                    val macAddress = modifyInverterModel.macAddress

                    // Vérification de l'existence de l'onduleur avec l'adresse MAC spécifiée
                    val inverter = queries.inverterExist(macAddress)

                    if (inverter == null) {
                        call.respond(HttpStatusCode.BadRequest, "L'adresse MAC ne correspond à aucun onduleur !")
                        return@post
                    }

                    // Mise à jour des informations de l'onduleur dans la base de données
                    queries.updateInverter(modifyInverterModel.name, modifyInverterModel.position, modifyInverterModel.isOnline, modifyInverterModel.batteryPercentage, inverter.idinverter)

                    call.respond(HttpStatusCode.OK, "Votre onduleur a bien été modifié !")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
                }
            }

            // Gestion de la route POST "/modifyprice"
            post("/modifyprice") {
                try {
                    // Réception des informations de modification du prix
                    val price = call.receive<ModifyPriceModel>()

                    // Mise à jour du prix dans la base de données
                    queries.updatePrice(price.price!!)

                    call.respond(HttpStatusCode.OK, "Le prix a bien été modifié !")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
                }
            }

            // Gestion de la route POST "/modifyuser"
            post("/modifyuser") {
                try {
                    // Réception des informations de modification de l'utilisateur
                    val modifyUserModel = call.receive<ModifyUserModel>()
                    val iduserSelected = modifyUserModel.iduser
                    val newEmail = modifyUserModel.email
                    val newName = modifyUserModel.name
                    val newPassword = modifyUserModel.hashedPassword()

                    // Vérification de la validité des informations de modification
                    if (!modifyUserModel.isValidCredentials()) {
                        call.respond(HttpStatusCode.BadRequest, "Format du login ou/et password incorrect.")
                        return@post
                    }

                    // Mise à jour des informations de l'utilisateur dans la base de données
                    queries.updateUser(newEmail, newName, newPassword, iduserSelected)

                    call.respond(HttpStatusCode.OK, "Les modifications ont bien été effectuées !")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
                }
            }

            // Gestion de la route POST "/getearningwithdate"
            post("/getearningwithdate"){

                try {
                    // Réception des dates envoyées en JSON
                    val getEarningsWith2Dates = call.receive<GetEarningsWith2Dates>()

                    val iduser = getEarningsWith2Dates.iduser
                    val dateDebut = getEarningsWith2Dates.dateDebut
                    val dateFin = getEarningsWith2Dates.dateFin

                    val dateFormatter: DateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    val from = LocalDate.parse(dateDebut, dateFormatter)
                    val to = LocalDate.parse(dateFin, dateFormatter)

                    val period = Period.between(from, to)
                    val years = period.years
                    val months = period.months
                    val days = period.days

                    println(years)
                    println(" ")
                    println(months)
                    println(" ")
                    println(days)

                    // Récupération des gains de l'utilisateur entre les deux dates spécifiées
                    val ar_Earnings: ArrayList<Earning> = queries.getEarningsWithUserIdAndDate(iduser, dateDebut, dateFin)

                    // Encodage des gains en JSON et envoi de la réponse
                    val json = Json.encodeToString(ar_Earnings)

                    call.respond(HttpStatusCode.OK, json)

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
                }
            }

            post("/deleteuser"){
                val idClient = call.receive<IdClient>()
                val iduser = idClient.idClient

                val principal = call.principal<JWTPrincipal>()
                val isAdmin = principal!!.payload.getClaim("isAdmin").asBoolean()

                if(isAdmin || principal.payload.getClaim("iduser").asInt() == iduser){
                    queries.setUserIdToNullInInverterTable(iduser!!)
                    queries.deleteUser(iduser)

                    call.respond(HttpStatusCode.OK, "L'utilisateur à bien été supprimé !")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Une erreur s'est produite.")
                }
            }


        }

        // Gestion de la route POST "/insertearning"
        post("/insertearning") {

            try {
                // Reception des informations d'ajout de gains
                val earning = call.receive<AddEarningsModel>()
                val macAddress = earning.macAddress

                // Vérification de l'existence de l'onduleur avec l'adresse MAC spécifiée
                val inverter = queries.inverterExist(macAddress!!)
                if (inverter == null) {
                    call.respond(HttpStatusCode.BadRequest, "L'adresse MAC ne correspond à aucun onduleur !")
                    return@post
                }

                // Insertion des gains associés à l'onduleur dans la base de données
                queries.insertEarningInverter(earning.date!!, earning.euro!!, earning.kilowatter!!, inverter.idinverter)

                call.respond(HttpStatusCode.OK, "Les gains ont bien été ajoutés !")

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
            }
        }

    }
}