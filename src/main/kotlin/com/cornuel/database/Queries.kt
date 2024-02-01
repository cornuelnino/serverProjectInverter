package com.cornuel.bdd.services

import com.cornuel.models.*
import org.mindrot.jbcrypt.BCrypt


class Queries() {

    var laConnexion = Connexion("localhost", "project2024", "root", "")


    fun getValuesUser(id: Int): AllValues? {

        var valeur: AllValues? = null

        var prepStatement = laConnexion.conn!!
            .prepareStatement("SELECT user.user_id, user.login, user.createdAt, i.name, i.isOnline, v.kilowatter, v.volts, v.batteryPercentage, e.perHour, e.perDay, e.perMonth, s.un, s.deux, s.trois, w.wUn, w.wDeux, w.wTrois FROM user JOIN inverter i JOIN values_inverter v JOIN earnings e JOIN settings s JOIN warnings w on s.settings_id = i.settings_id and e.earnings_id = v.earnings_id and v.values_id = i.values_id and user.inverter_id = i.inverter_id where user_id=?")

        prepStatement.setInt(1, id)

        var rs = prepStatement.executeQuery()

        while (rs.next()) {
            valeur = AllValues(
                rs.getInt("user_id"),
                rs.getString("login"),
                rs.getString("createdAt"),
                rs.getString("name"),
                rs.getBoolean("isOnline"),
                rs.getInt("kilowatter"),
                rs.getInt("volts"),
                rs.getInt("batteryPercentage"),
                rs.getInt("perHour"),
                rs.getInt("perDay"),
                rs.getInt("perMonth"),
                rs.getString("un"),
                rs.getString("deux"),
                rs.getString("trois"),
                rs.getString("wUn"),
                rs.getString("wDeux"),
                rs.getString("wTrois"),
            )
        }
        return valeur
    }

    fun getAllUserId(): ArrayList<IdClient> {
        val ar_IdClient = ArrayList<IdClient>()

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("SELECT user.user_id FROM user")

        val rs = query.executeQuery()

        while (rs.next()) {
            ar_IdClient.add(IdClient(rs.getInt("user_id")))
        }

        return ar_IdClient
    }

    fun insertInverter(inverter: AddInverterModel) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("INSERT INTO inverter (name, macAddress, position, isOnline, values_id, settings_id, warnings_id) VALUES (?, ?, ?, ?, ?, ?, ?)")

        query.setString(1, inverter.name)
        query.setString(2, inverter.macAddress)
        query.setString(3, inverter.position)
        query.setBoolean(4, inverter.isOnline!!)
        query.setInt(5, inverter.values_id!!)
        query.setInt(6, inverter.settings_id!!)
        query.setInt(4, inverter.warnings_id!!)

        query.executeUpdate()
    }

    fun insertValuesInverter(values: AddValuesInverterModel) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("INSERT INTO values_inverter (kilowatter, volts, batteryPercentage, earnings_id) VALUES (?, ?, ?, ?)")

        query.setInt(1, values.kilowatter!!)
        query.setInt(2, values.volts!!)
        query.setInt(3, values.batteryPercentage!!)
        query.setInt(4, values.earnings_id!!)

        query.executeUpdate()
    }

    fun insertSettingsInverter(settings: AddSettingsInverterModel) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("INSERT INTO settings (un, deux, trois) VALUES (?, ?, ?)")

        query.setString(1, settings.un)
        query.setString(2, settings.deux)
        query.setString(3, settings.trois)

        query.executeUpdate()

    }

    fun insertWarningInverter(warnings: AddWarningsInverterModel) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("INSERT INTO warnings (wUn, wDeux, wTrois) VALUES (?, ?, ?)")

        query.setString(1, warnings.wUn)
        query.setString(2, warnings.wDeux)
        query.setString(3, warnings.wTrois)

        query.executeUpdate()
    }


    fun loginAvailable(user: UserModel): Boolean {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("SELECT * FROM user WHERE email = ? AND password=PASSWORD(?)")

        query.setString(1, user.email)
        query.setString(2, user.password)
        val rs = query.executeQuery()

        if (rs.next()) {
            return true
        } else {
            return false
        }
    }

    fun isUserAdmin(user: UserModel): Boolean? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("SELECT isAdmin FROM user WHERE email = ?")

        query.setString(1, user.email)
        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getBoolean("isAdmin")
        } else {
            return null
        }

    }

    fun getLastSettingsID(): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("SELECT MAX(settings_id) AS max_id FROM settings")

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("max_id")
        } else {
            return null
        }
    }

    fun getLastWarningsID(): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("SELECT max(warnings_id) FROM warnings")

        val rs = query.executeQuery()

        return rs.getInt("warnings_id")
    }

    fun getLastEarningsID(): Int? {
        return null
    }


}