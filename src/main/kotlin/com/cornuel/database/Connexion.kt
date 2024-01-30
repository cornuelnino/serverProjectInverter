package com.cornuel.bdd.services

import java.sql.*


class Connexion(hostname:String, databasename:String, username:String, password:String) {
    var conn: Connection? = null
        get() = field

    private val url = "jdbc:mysql://$hostname:3306/$databasename"


    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (ex: ClassNotFoundException) {
            println("erreur com.mysql.jdbc.Driver")
        }
        try {
            conn = DriverManager.getConnection(url, username, password)
            println("Connexion établie !")
        } catch (ex: SQLException) {
            println("Erreur de connexion à la base de données !")
        }
    }

    fun getConnexion(): Connection? {
        return conn
    }
}