package com.cornuel.plugins

import com.cornuel.bdd.services.Queries
import com.cornuel.isDistant
import com.cornuel.models.*
import com.cornuel.models.jwt.SendToken
import com.cornuel.models.jwt.UserLoginCredentials
import com.cornuel.models.jwt.UserRegisterCredentials
import com.cornuel.utils.TokenManager
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.netty.util.internal.ResourcesUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.h2.tools.Shell
import org.mindrot.jbcrypt.BCrypt
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

// Fonction de configuration du routage de l'application
fun Application.configureRouting() {
    routing {
        // Création d'une instance de la classe Queries pour interagir avec la base de données
        val queries = Queries()

        staticResources("/static", "files")

        get("/databasepanel") {
            call.respondRedirect("/static/index.html")
        }

        authenticate("authbdd") {

            get("/savebdd") {
                if (isDistant == 1) {
                    val backupFile =
                        File("database_backup.sql")

                    try {
                        val process =
                            ProcessBuilder("C:\\xampp\\mysql\\bin\\mysqldump", "--user=root", "--password=", "mydb")
                                .directory(File("C:\\xampp\\mysql\\bin"))
                                .redirectOutput(backupFile)
                                .start()

                        val reader = BufferedReader(InputStreamReader(process.inputStream))
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            println(line) // Facultatif : Afficher la sortie de la commande
                        }

                        process.waitFor()

                        val byteArray = backupFile.readBytes()

                        call.response.header("Content-Disposition", "attachment; filename=\"database_backup.sql\"")
                        call.respondBytes(byteArray)


                        call.respondRedirect("/databasepanel")
                    } catch (e: Exception) {
                        println("Erreur lors de la sauvegarde de la base de données : ${e.message}")
                    }

                } else if (isDistant == 2) {
                    call.respondRedirect("/static/warning.html")
                }

            }
        }

        post("/uploadbdd") {
            val multipartData = call.receiveMultipart()

            if (isDistant == 1) {

                var uploadedFile: File? = File("saveBDD/uploadedFile.sql")

                multipartData.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val originalFileName = part.originalFileName!!.replace(" ", "_") ?: "file.sql"
                        val file = File.createTempFile("upload-", "-$originalFileName")

                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }
                        uploadedFile = file
                    }
                }

                try {
                    // Création du processus
                    val processBuilder = ProcessBuilder("./uploadFileToBDD.bat", uploadedFile.toString())
                    val process = processBuilder.start()

                    // Récupération de la sortie du processus
                    val inputStreamReader = InputStreamReader(process.inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)

                    var ligne: String?
                    while (bufferedReader.readLine().also { ligne = it } != null) {
                        println(ligne)
                    }

                    // Attente de la fin du processus
                    val exitCode = process.waitFor()
                    println("Le processus s'est terminé avec le code de sortie : $exitCode")

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                call.respondRedirect("/databasepanel")

            } else if (isDistant == 2) {
                call.respondRedirect("/static/warning.html")
            }

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
                val token =
                    SendToken(user.iduser, user.email, user.name, user.isAdmin, tokenManager.generateJWTToken(user))
                call.respond(HttpStatusCode.OK, token)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Format du JSON incorrect.")
            }
        }

        // Gestion de la route POST "/register"
        post("/register") {
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

            post("/modifysettingsinverter") {
                val modifySettingsInverterModel = call.receive<ModifySettingsInverterModel>()
                val iduser = modifySettingsInverterModel.iduser

                val principal = call.principal<JWTPrincipal>()
                val isAdmin = principal!!.payload.getClaim("isAdmin").asBoolean()

                if (isAdmin || principal.payload.getClaim("iduser").asInt() == iduser) {

                    queries.modifySettingsInverter(iduser, modifySettingsInverterModel)

                    call.respond(HttpStatusCode.OK, "Les paramètres de l'onduleur à bien été modifié !")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Une erreur s'est produite.")
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
                    val modifyUserModel = call.receive<ModifyUserModel>()
                    val iduserSelected = modifyUserModel.iduser
                    val newEmail = modifyUserModel.email
                    val newName = modifyUserModel.name
                    val newPassword = modifyUserModel.password

                    val principal = call.principal<JWTPrincipal>()
                    val isAdmin = principal!!.payload.getClaim("isAdmin").asBoolean()

                    if (isAdmin || principal.payload.getClaim("iduser").asInt() == iduserSelected) {

                        if (newPassword != "") {

                            if (!modifyUserModel.isValidCredentials()) {
                                call.respond(HttpStatusCode.BadRequest, "Format du login ou/et password incorrect.")
                                return@post
                            }

                            queries.updateUser(newEmail, newName, modifyUserModel.hashedPassword(), iduserSelected)

                        } else {
                            queries.updateUserWithoutPassword(newEmail, newName, iduserSelected)

                        }


                        call.respond(HttpStatusCode.OK, "Les paramètres de l'onduleur à bien été modifié !")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Une erreur s'est produite.")
                    }

                    // Vérification de la validité des informations de modification


                    // Mise à jour des informations de l'utilisateur dans la base de données


                    call.respond(HttpStatusCode.OK, "Les modifications ont bien été effectuées !")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
                }
            }

            // Gestion de la route POST "/getearningwithdate"
            post("/getearningwithdate") {

                try {
                    // Réception des dates envoyées en JSON
                    val getEarningsWith2Dates = call.receive<GetEarningsWith2Dates>()

                    val iduser = getEarningsWith2Dates.iduser
                    val dateDebut = getEarningsWith2Dates.dateDebut
                    val dateFin = getEarningsWith2Dates.dateFin

                    // Récupération des gains de l'utilisateur entre les deux dates spécifiées
                    val ar_Earnings: ArrayList<Earning> =
                        queries.getEarningsWithUserIdAndDate(iduser, dateDebut, dateFin)

                    // Encodage des gains en JSON et envoi de la réponse
                    val json = Json.encodeToString(ar_Earnings)

                    call.respond(HttpStatusCode.OK, json)

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
                }
            }

            post("/deleteuser") {
                val idClient = call.receive<IdClient>()
                val iduser = idClient.idClient

                val principal = call.principal<JWTPrincipal>()
                val isAdmin = principal!!.payload.getClaim("isAdmin").asBoolean()

                if (isAdmin || principal.payload.getClaim("iduser").asInt() == iduser) {
                    queries.setUserIdToNullInInverterTable(iduser!!)
                    queries.deleteUser(iduser)

                    call.respond(HttpStatusCode.OK, "L'utilisateur à bien été supprimé !")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Une erreur s'est produite.")
                }
            }


        }


        post("/modifywarningsinverter") {
            try {
                // Réception des informations d'ajout d'un onduleur
                val modifyWarningsInverterModel = call.receive<ModifyWarningsInverterModel>()

                queries.modifyWarningsInverter(modifyWarningsInverterModel)

                call.respond(HttpStatusCode.OK, "Les warnings de l'onduleur ont bien été modifiés !")

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "Incorrect JSON data !" + e.message!!)
            }
        }

        post("/insertinverter") {

            try {
                // Réception des informations d'ajout d'un onduleur
                val inverter = call.receive<AddInverterModel>()

                // Insertion des paramètres et avertissements de l'onduleur dans la base de données
                queries.insertSettingsInverter(inverter.outputSourcePriority!!)
                queries.insertWarningInverter(
                    inverter.inverterFault!!,
                    inverter.lineFail!!,
                    inverter.voltageTooLow!!,
                    inverter.voltageTooHigh!!,
                    inverter.overTemperature!!,
                    inverter.fanLocked!!,
                    inverter.batteryLowAlarm!!,
                    inverter.softFail!!,
                    inverter.batteryTooLowToCharge!!
                )

                // Récupération des derniers ID de paramètres et d'avertissements insérés
                val warningsid = queries.getLastWarningsID()
                val settingsid = queries.getLastSettingsID()

                // Insertion de l'onduleur avec les paramètres et les avertissements dans la base de données
                queries.insertInverter(
                    inverter.name!!,
                    inverter.macAddress!!,
                    inverter.position!!,
                    inverter.isOnline!!,
                    inverter.batteryPercentage!!,
                    inverter.outputActivePower!!,
                    inverter.outputVoltage!!,
                    warningsid!!,
                    settingsid!!
                )

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
                queries.updateInverter(
                    modifyInverterModel.name,
                    modifyInverterModel.position,
                    modifyInverterModel.isOnline,
                    modifyInverterModel.batteryPercentage,
                    modifyInverterModel.outputActivePower,
                    modifyInverterModel.outputVoltage,
                    inverter.idinverter
                )

                call.respond(HttpStatusCode.OK, "Votre onduleur a bien été modifié !")

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Erreur dans le JSON envoyé !")
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