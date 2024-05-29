package com.cornuel

import com.cornuel.database.Config
import com.cornuel.plugins.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files

public var isDistant = 0
fun main() {

    val configDefaultStream = object {}.javaClass.classLoader.getResourceAsStream("configuration/config_Default.json")
    val tempConfigDefaultFile = File.createTempFile("temp", ".json")
    tempConfigDefaultFile.deleteOnExit()
    configDefaultStream.use { input ->
        Files.copy(input, tempConfigDefaultFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING)
    }

    var configFile = File("./config.json")

    val jsonConfig = Json.decodeFromString<Config>(tempConfigDefaultFile.readText())

    do {

        println("     DEMARRAGE CONFIGURATION BDD     ")
        println()
        println("|------CONFIGURATION ACTUELLE-------|")
        println("| URL : ${jsonConfig.url}")
        println("| BDD : ${jsonConfig.bdd}")
        println("| USERNAME : ${jsonConfig.username}")
        println("| PASSWORD : ${jsonConfig.password}")
        println("-------------------------------------")

        println()

        println("Tapez 1 pour la configuration par défaut")
        println("Tapez 2 pour écrire une nouvelle configuration")
        println()
        println("Entrez votre choix :")

        var entree = readln()
        var int_Entree = entree.toInt()

        if(int_Entree == 1){

            isDistant = 1
            configFile.writeBytes(tempConfigDefaultFile.readBytes())

        } else if(int_Entree == 2){
            isDistant = 2

            println("Entrez l'URL de votre serveur :")
            var urlServer = readln()

            println("Entrez le nom de votre base de données :")
            var nameBDD = readln()

            println("Entrez le nom d'utilisateur de votre base de données :")
            var username = readln()

            println("Entrez le mot de passe de votre base de données :")
            var password = readln()

            val config = Config("jdbc:mysql://$urlServer:3306/", nameBDD, username, password)
            val json = Json.encodeToString(config)

            configFile.writeBytes(json.toByteArray())
        }

    } while (!(int_Entree == 1 || int_Entree == 2))



    val keyStoreFile = File("src/main/keystore/keystore.jks")
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
            configureRouting()
            configureSockets()
            configureSerialization()
            configureTemplating()

        }
    }
    embeddedServer(Netty, environment).start(wait = true)
}
