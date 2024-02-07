package com.cornuel.bdd.services

import com.cornuel.models.*


class Queries() {

    var laConnexion = Connexion("localhost", "project2024", "root", "")

    var querieGetAllUserValues =
        "SELECT u.user_id, u.email, u.login, u.createdAt, i.name, i.macAddress, i.position, i.isOnline, v.kilowatter, v.volts, v.batteryPercentage, s.un, s.deux, s.trois, w.wUn, w.wDeux, w.wTrois FROM `link` JOIN user u JOIN inverter i JOIN values_inverter v join settings s JOIN warnings w on u.user_id = link.user_id and i.inverter_id = link.inverter_id and v.values_id = link.values_id and s.settings_id = link.settings_id and w.warnings_id = link.warnings_id WHERE u.user_id = ?"
    var querieGetAllUserId = "SELECT user.user_id FROM user"
    var querieGetUserIdAndIsAdmin = "SELECT user.user_id, user.isAdmin FROM user WHERE user.email = ?"

    var querieIsLoginCorrect = "SELECT * FROM user WHERE email = ? AND password=PASSWORD(?)"
    var querieIsUserAdmin = "SELECT isAdmin FROM user WHERE email = ?"
    var querieIsMacAddressCorrect = "SELECT * FROM inverter WHERE macAddress = ?"

    var querieGetLastSettingsID = "SELECT MAX(settings_id) AS max_id FROM settings"
    var querieGetLastWarningsID = "SELECT MAX(warnings_id) as max_id FROM warnings"
    var querieGetLastValuesInverterID = "SELECT MAX(values_id) as max_id FROM values_inverter"
    var querieGetLastInverterID = "SELECT MAX(inverter_id) as max_id FROM inverter"
    var querieGetLastUserID = "SELECT MAX(user_id) as max_id FROM user"


    var querieGetLinkIDMacAddress =
        "SELECT link.link_id FROM `link` JOIN inverter i JOIN values_inverter v join settings s JOIN warnings w on i.inverter_id = link.inverter_id and v.values_id = link.values_id and s.settings_id = link.settings_id and w.warnings_id = link.warnings_id WHERE i.macAddress = ?"
    var querieGetInverterIDMacAddress =
        "SELECT i.inverter_id FROM `link` JOIN inverter i JOIN values_inverter v join settings s JOIN warnings w on i.inverter_id = link.inverter_id and v.values_id = link.values_id and s.settings_id = link.settings_id and w.warnings_id = link.warnings_id WHERE i.macAddress = ?"

    var querieInsertInverter = "INSERT INTO inverter (name, macAddress, position, isOnline) VALUES (?, ?, ?, ?)"
    var querieInsertValuesInverter =
        "INSERT INTO values_inverter (kilowatter, volts, batteryPercentage) VALUES (?, ?, ?)"
    var querieInsertSettingsInverter = "INSERT INTO settings (un, deux, trois) VALUES (?, ?, ?)"
    var querieInsertWarningInverter = "INSERT INTO warnings (wUn, wDeux, wTrois) VALUES (?, ?, ?)"
    var querieInsertLinkTable =
        "INSERT INTO link (inverter_id, values_id, settings_id, warnings_id) VALUES (?, ?, ?, ?)"
    var querieInsertUser =
        "INSERT INTO user (email, login, password, isAdmin, createdat) VALUES (?, ?, PASSWORD(?), ?, ?)"
    var querieInsertEarnings = "INSERT INTO earnings (inverter_id, date, perHour) VALUES (?, ?, ?)"

    var querieUpdateLinkWithUserId = "UPDATE link SET user_id = ? WHERE link_id = ?"
    var querieUpdatePrice = "UPDATE price SET kwPrice = ? "

    var querieGetEarningsUser =
        "SELECT u.user_id, e.date, e.perHour FROM `link` JOIN earnings e JOIN user u JOIN inverter i JOIN values_inverter v join settings s JOIN warnings w on u.user_id = link.user_id and i.inverter_id = link.inverter_id and v.values_id = link.values_id and s.settings_id = link.settings_id and w.warnings_id = link.warnings_id and e.inverter_id = i.inverter_id"


    fun getValuesUser(id: Int): AllValues? {

        var valeur: AllValues? = null

        var prepStatement = laConnexion.conn!!
            .prepareStatement(querieGetAllUserValues)

        prepStatement.setInt(1, id)

        var rs = prepStatement.executeQuery()

        while (rs.next()) {
            valeur = AllValues(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("createdAt"),
                rs.getString("name"),
                rs.getString("macAddress"),
                rs.getString("position"),
                rs.getBoolean("isOnline"),
                rs.getInt("kilowatter"),
                rs.getInt("volts"),
                rs.getInt("batteryPercentage"),
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
            .prepareStatement(querieGetAllUserId)

        val rs = query.executeQuery()

        while (rs.next()) {
            ar_IdClient.add(IdClient(rs.getInt("user_id")))
        }

        return ar_IdClient
    }

    fun getUserIdAndIsAdmin(email: String): UserValueLogin? {

        var result: UserValueLogin? = null

        var query = laConnexion.conn!!
            .prepareStatement(querieGetUserIdAndIsAdmin)

        query.setString(1, email)

        val rs = query.executeQuery()

        while (rs.next()) {
            result = UserValueLogin(
                rs.getInt("user_id"),
                rs.getBoolean("isAdmin")
            )
        }
        return result
    }

    fun getLinkIdWithMacAddress(macAddress: String): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetLinkIDMacAddress)

        query.setString(1, macAddress)

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("link_id")
        } else {
            return null
        }
    }

    fun getInverterIdWithMacAddress(macAddress: String): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetInverterIDMacAddress)

        query.setString(1, macAddress)

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("inverter_id")
        } else {
            return null
        }
    }

    fun insertInverter(name: String, macAddress: String, position: String, isOnline: Boolean) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertInverter)

        query.setString(1, name)
        query.setString(2, macAddress)
        query.setString(3, position)
        query.setBoolean(4, isOnline)

        query.executeUpdate()
    }

    fun insertValuesInverter(kilowatter: Double, volts: Double, batteryPercentage: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertValuesInverter)

        query.setDouble(1, kilowatter)
        query.setDouble(2, volts)
        query.setInt(3, batteryPercentage)

        query.executeUpdate()
    }

    fun insertSettingsInverter(set1: String, set2: String, set3: String) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertSettingsInverter)

        query.setString(1, set1)
        query.setString(2, set2)
        query.setString(3, set3)

        query.executeUpdate()

    }

    fun insertWarningInverter(warn1: String, warn2: String, warn3: String) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertWarningInverter)

        query.setString(1, warn1)
        query.setString(2, warn2)
        query.setString(3, warn3)

        query.executeUpdate()
    }

    fun isLoginCorrect(user: UserModel): UserValueLogin? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieIsLoginCorrect)

        query.setString(1, user.email)
        query.setString(2, user.password)
        val rs = query.executeQuery()

        if (rs.next()) {
            return UserValueLogin(
                rs.getInt("user_id"),
                rs.getBoolean("isAdmin")
            )
        } else {
            return null
        }
    }

    fun isMacCorrect(macAddress: String?): Boolean {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieIsMacAddressCorrect)

        query.setString(1, macAddress)
        val rs = query.executeQuery()

        if (rs.next()) {
            return true
        } else {
            return false
        }
    }

    fun getLastUserID(): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetLastUserID)

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("max_id")
        } else {
            return null
        }
    }

    fun getLastSettingsID(): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetLastSettingsID)

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
            .prepareStatement(querieGetLastWarningsID)

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("max_id")
        } else {
            return null
        }
    }

    fun getLastValuesID(): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetLastValuesInverterID)

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("max_id")
        } else {
            return null
        }
    }

    fun getLastInverterID(): Int? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetLastInverterID)

        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getInt("max_id")
        } else {
            return null
        }
    }

    fun insertLinkTable(inverter_id: Int, values_id: Int, settings_id: Int, warnings_id: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertLinkTable)

        query.setInt(1, inverter_id)
        query.setInt(2, values_id)
        query.setInt(3, settings_id)
        query.setInt(4, warnings_id)

        query.executeUpdate()
    }

    fun insertUser(email: String, login: String, password: String, admin: Boolean, createdAt: String) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertUser)

        query.setString(1, email)
        query.setString(2, login)
        query.setString(3, password)
        query.setBoolean(4, admin)
        query.setString(5, createdAt)

        query.executeUpdate()
    }

    fun updateLinkTable(user_id: Int, link_id: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateLinkWithUserId)

        query.setInt(1, user_id)
        query.setInt(2, link_id)

        query.executeUpdate()
    }

    fun insertEarnings(inverterId: Int, date: String, perHour: Double) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertEarnings)

        query.setInt(1, inverterId)
        query.setString(2, date)
        query.setDouble(3, perHour)

        query.executeUpdate()
    }

    fun updatePrice(price: Double) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdatePrice)

        query.setDouble(1, price)

        query.executeUpdate()
    }
}