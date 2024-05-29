package com.cornuel.bdd.services

import com.cornuel.database.Config
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.sql.*


class Connexion() {
    var conn: Connection? = null
        get() = field


    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (ex: ClassNotFoundException) {
            println("erreur com.mysql.jdbc.Driver")
        }
        try {
            var configFile = File("./config.json").readText()

            val config = Json.decodeFromString<Config>(configFile)

            conn = DriverManager.getConnection("${config.url}${config.bdd}", config.username, config.password)

            println("Connexion établie !")
        } catch (ex: SQLException) {
            println("Erreur de connexion à la base de données !")
        }
    }

    fun getConnexion(): Connection? {
        return conn
    }
}