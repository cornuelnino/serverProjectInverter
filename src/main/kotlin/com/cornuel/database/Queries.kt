package com.cornuel.bdd.services

import com.cornuel.models.*
import com.cornuel.models.jwt.Inverter
import com.cornuel.models.jwt.User

// Définition d'une classe Queries
class Queries() {

    companion object {

        // Requête SQL pour récupérer toutes les valeurs des utilisateurs et des onduleurs associés
        var querieGetAllUserValues =
            "SELECT user.iduser, user.email, user.name, user.isAdmin, user.createdAt, inverter.name, inverter.macAddress, inverter.position, inverter.isOnline, inverter.batteryPercentage, settings.gridVoltage, settings.gridFrequency, settings.ACoutputVoltage, settings.ACoutputFrequency, settings.ACoutputApparentPower, settings.ACoutputActivePower, settings.BUSvoltage, settings.batteryVoltage, settings.batteryChargingCurrent, settings.batteryCapacity, settings.inverterHeatSinkTemperature, settings.PVinputCurrent, settings.PVinputVoltage, settings.batteryVoltageSCC, settings.batteryDischargeCurrent, settings.deviceStatus, warnings.lineFail, warnings.OPVShort, warnings.batteryLowAlarm, warnings.EEPROMdefault, warnings.powerLimit, warnings.highPVvoltage, warnings.MPPTOverloadFault, warnings.MPPTOverloadWarning, warnings.batteryLowToCharge FROM inverter JOIN user JOIN settings JOIN warnings ON inverter.user_iduser = user.iduser AND inverter.settings_idsettings = settings.idsettings AND inverter.warnings_idwarnings = warnings.idwarnings AND inverter.warnings_idwarnings = warnings.idwarnings WHERE inverter.user_iduser = ?;"

        // Requêtes pour récupérer des informations sur les utilisateurs
        var querieGetAllUserId = "SELECT user.iduser FROM user"
        var querieGetUserId = "SELECT user.iduser FROM user WHERE user.email = ?"
        var querieGetPassword = "SELECT user.password FROM `user` WHERE user.iduser = ?"

        // Requêtes de vérification d'existence d'utilisateur et d'onduleur
        var querieUserExist = "SELECT * from user WHERE email=?"
        var querieInverterExist = "SELECT * from inverter WHERE macAddress=?"

        // Requêtes pour obtenir les derniers identifiants des différentes tables
        var querieGetLastSettingsID = "SELECT MAX(idsettings) AS max_id FROM settings"
        var querieGetLastWarningsID = "SELECT MAX(idwarnings) as max_id FROM warnings"
        var querieGetLastInverterID = "SELECT MAX(idinverter) as max_id FROM inverter"
        var querieGetLastUserID = "SELECT MAX(iduser) as max_id FROM user"

        // Requêtes d'insertion de données dans les tables
        var querieInsertInverter =
            "INSERT INTO inverter (name, macAddress, position, isOnline, batteryPercentage, warnings_idwarnings, settings_idsettings, user_iduser) VALUES (?, ?, ?, ?, ?, ?, ?, null)"
        var querieInsertSettingsInverter =
            "INSERT INTO settings (gridVoltage, gridFrequency, ACoutputVoltage, ACoutputFrequency, ACoutputApparentPower, ACoutputActivePower, BUSvoltage, batteryVoltage, batteryChargingCurrent, batteryCapacity, inverterHeatSinkTemperature, PVinputCurrent, PVinputVoltage, batteryVoltageSCC, batteryDischargeCurrent, deviceStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        var querieInsertWarningInverter =
            "INSERT INTO warnings (lineFail, OPVShort, batteryLowAlarm, EEPROMdefault, powerLimit, highPVvoltage, MPPTOverloadFault, MPPTOverloadWarning, batteryLowToCharge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

        var querieInsertUser =
            "INSERT INTO user (email, name, password, isAdmin, createdAt) VALUES (?, ?, ?, 0, NOW())"

        var querieDeleteUser = "DELETE FROM user WHERE iduser = ?"

        var querieInsertEarningsInverter =
            "INSERT INTO earnings (date, euro, kilowatter, inverter_idinverter) VALUES (?, ?, ?, ?)"

        var querieSetUserIdToNullInverter = "UPDATE inverter SET user_iduser = null WHERE user_iduser = ?"

        // Requête de mise à jour du prix
        var querieUpdatePrice = "UPDATE price SET kwPrice = ? "

        // Requête de récuperation du prix
        var querieGetPrice = "SELECT kwPrice FROM price"

        // Requêtes de mise à jour des données dans les tables
        var querieUpdateUserIDInverter = "UPDATE inverter SET user_iduser = ? WHERE idinverter = ?"
        var querieUpdateInverter =
            "UPDATE inverter SET name = ?, position = ?, isOnline = ?, batteryPercentage = ? WHERE idinverter = ?"
        var querieUpdateUser = "UPDATE user SET email = ?, name = ?, password = ? WHERE iduser = ?"

        // Requête pour obtenir les gains d'un utilisateur entre deux dates
        var querieGetEarningsUserBetween2Dates =
            "SELECT earnings.date, earnings.euro, earnings.kilowatter FROM `inverter` JOIN earnings on inverter.idinverter = earnings.inverter_idinverter WHERE inverter.user_iduser = ? AND earnings.date BETWEEN ? and ? ORDER BY earnings.date ASC"
    }

    // Initialisation de la connexion à la base de données
    var laConnexion = Connexion("localhost", "mydb", "root", "")


    // Méthode pour vérifier si un utilisateur existe
    fun userExist(email: String): User? {
        val prepStatement = laConnexion.getConnexion()!!
            .prepareStatement(querieUserExist)
        prepStatement.setString(1, email)

        val rs = prepStatement.executeQuery()
        var user: User? = null

        while (rs.next()) {
            user = User(
                rs.getInt("iduser"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getBoolean("isAdmin")
            )
        }
        return user
    }

    // Méthode pour vérifier si un onduleur existe
    fun inverterExist(macAddress: String): Inverter? {
        val prepStatement = laConnexion.getConnexion()!!
            .prepareStatement(querieInverterExist)
        prepStatement.setString(1, macAddress)

        val rs = prepStatement.executeQuery()
        var inverter: Inverter? = null

        while (rs.next()) {
            inverter = Inverter(
                rs.getInt("idinverter"),
                rs.getString("name"),
                rs.getString("macAddress"),
                rs.getString("position"),
                rs.getBoolean("isOnline"),
                rs.getInt("batteryPercentage"),
                rs.getInt("warnings_idwarnings"),
                rs.getInt("settings_idsettings"),
                rs.getInt("user_iduser")
            )
        }
        return inverter
    }

    // Méthode pour obtenir les valeurs d'un utilisateur
    fun getValuesUser(id: Int): AllValues? {

        var valeur: AllValues? = null

        val prepStatement = laConnexion.conn!!
            .prepareStatement(querieGetAllUserValues)

        prepStatement.setInt(1, id)

        val rs = prepStatement.executeQuery()

        while (rs.next()) {
            valeur = AllValues(
                rs.getInt("iduser"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getBoolean("isAdmin"),
                rs.getString("createdAt"),
                rs.getString("name"),
                rs.getString("macAddress"),
                rs.getString("position"),
                rs.getBoolean("isOnline"),
                rs.getInt("batteryPercentage"),
                rs.getString("gridVoltage"),
                rs.getString("gridFrequency"),
                rs.getString("ACoutputVoltage"),
                rs.getString("ACoutputFrequency"),
                rs.getString("ACoutputApparentPower"),
                rs.getString("ACoutputActivePower"),
                rs.getString("BUSvoltage"),
                rs.getString("batteryVoltage"),
                rs.getString("batteryChargingCurrent"),
                rs.getString("batteryCapacity"),
                rs.getString("inverterHeatSinkTemperature"),
                rs.getString("PVinputCurrent"),
                rs.getString("PVinputVoltage"),
                rs.getString("batteryVoltageSCC"),
                rs.getString("batteryDischargeCurrent"),
                rs.getString("deviceStatus"),
                rs.getBoolean("lineFail"),
                rs.getBoolean("OPVShort"),
                rs.getBoolean("batteryLowAlarm"),
                rs.getBoolean("EEPROMdefault"),
                rs.getBoolean("powerLimit"),
                rs.getInt("highPVvoltage"),
                rs.getInt("MPPTOverloadFault"),
                rs.getInt("MPPTOverloadWarning"),
                rs.getInt("batteryLowToCharge")
            )
        }
        return valeur
    }

    // Méthode pour obtenir tous les identifiants des utilisateurs
    fun getAllUserId(): ArrayList<IdClient> {
        val ar_IdClient = ArrayList<IdClient>()

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetAllUserId)

        val rs = query.executeQuery()

        while (rs.next()) {
            ar_IdClient.add(IdClient(rs.getInt("iduser")))
        }

        return ar_IdClient
    }

    // Méthode pour insérer un onduleur dans la base de données
    fun insertInverter(
        name: String,
        macAddress: String,
        position: String,
        isOnline: Boolean,
        batteryPercentage: Int,
        warningsid: Int,
        settingsid: Int
    ) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertInverter)

        query.setString(1, name)
        query.setString(2, macAddress)
        query.setString(3, position)
        query.setBoolean(4, isOnline)
        query.setInt(5, batteryPercentage)
        query.setInt(6, warningsid)
        query.setInt(7, settingsid)

        query.executeUpdate()
    }

    // Méthode pour insérer les paramètres de l'onduleur dans la base de données
    fun insertSettingsInverter(
        gridVoltage: String,
        gridFrequency: String,
        ACoutputVoltage: String,
        ACoutputFrequency: String,
        ACoutputApparentPower: String,
        ACoutputActivePower: String,
        BUSvoltage: String,
        batteryVoltage: String,
        batteryChargingCurrent: String,
        batteryCapacity: String,
        inverterHeatSinkTemperature: String,
        PVinputCurrent: String,
        PVinputVoltage: String,
        batteryVoltageSCC: String,
        batteryDischargeCurrent: String,
        deviceStatus: String
    ) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertSettingsInverter)

        query.setString(1, gridVoltage)
        query.setString(2, gridFrequency)
        query.setString(3, ACoutputVoltage)
        query.setString(4, ACoutputFrequency)
        query.setString(5, ACoutputApparentPower)
        query.setString(6, ACoutputActivePower)
        query.setString(7, BUSvoltage)
        query.setString(8, batteryVoltage)
        query.setString(9, batteryChargingCurrent)
        query.setString(10, batteryCapacity)
        query.setString(11, inverterHeatSinkTemperature)
        query.setString(12, PVinputCurrent)
        query.setString(13, PVinputVoltage)
        query.setString(14, batteryVoltageSCC)
        query.setString(15, batteryDischargeCurrent)
        query.setString(16, deviceStatus)

        query.executeUpdate()

    }

    // Méthode pour insérer les avertissements de l'onduleur dans la base de données
    fun insertWarningInverter(
        lineFail: Boolean,
        OPVShort: Boolean,
        batteryLowAlarm: Boolean,
        EEPROMdefault: Boolean,
        powerLimit: Boolean,
        highPVvoltage: Int,
        MPPTOverloadFault: Int,
        MPPTOverloadWarning: Int,
        batteryLowToCharge: Int
    ) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertWarningInverter)

        query.setBoolean(1, lineFail)
        query.setBoolean(2, OPVShort)
        query.setBoolean(3, batteryLowAlarm)
        query.setBoolean(4, EEPROMdefault)
        query.setBoolean(5, powerLimit)
        query.setInt(6, highPVvoltage)
        query.setInt(7, MPPTOverloadFault)
        query.setInt(8, MPPTOverloadWarning)
        query.setInt(9, batteryLowToCharge)

        query.executeUpdate()
    }

    // Méthode pour obtenir le dernier identifiant d'utilisateur
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

    // Méthode pour obtenir le dernier identifiant de paramètres
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

    // Méthode pour obtenir le dernier identifiant d'avertissement
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

    // Méthode pour insérer un utilisateur dans la base de données
    fun insertUser(email: String, login: String, password: String) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertUser)

        query.setString(1, email)
        query.setString(2, login)
        query.setString(3, password)

        query.executeUpdate()
    }

    // Méthode pour mettre à jour le prix
    fun updatePrice(price: String) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdatePrice)

        query.setDouble(1, price.toDouble())

        query.executeUpdate()
    }

    // Méthode pour mettre à jour un utilisateur
    fun updateUser(email: String, name: String, password: String, iduser: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateUser)

        query.setString(1, email)
        query.setString(2, name)
        query.setString(3, password)
        query.setInt(4, iduser)

        query.executeUpdate()
    }

    // Méthode pour mettre à jour l'identifiant de l'utilisateur associé à un onduleur
    fun updateUserIDInverter(idinverter: Int, lastUserID: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateUserIDInverter)

        query.setInt(1, lastUserID)
        query.setInt(2, idinverter)

        query.executeUpdate()
    }

    // Méthode pour insérer les gains d'un onduleur dans la base de données
    fun insertEarningInverter(date: String, euro: Double, kilowatter: Double, idinverter: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertEarningsInverter)

        query.setString(1, date)
        query.setDouble(2, euro)
        query.setDouble(3, kilowatter)
        query.setInt(4, idinverter)

        query.executeUpdate()
    }

    // Méthode pour mettre à jour les informations d'un onduleur
    fun updateInverter(name: String, position: String, online: Boolean, batteryPercentage: Int, idinverter: Int) {

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateInverter)

        query.setString(1, name)
        query.setString(2, position)
        query.setBoolean(3, online)
        query.setInt(4, batteryPercentage)
        query.setInt(5, idinverter)

        query.executeUpdate()

    }

    // Méthode pour obtenir les gains d'un utilisateur entre deux dates
    fun getEarningsWithUserIdAndDate(iduser: Int, dateDebut: String, dateFin: String): ArrayList<Earning> {

        val ar_Earnings = ArrayList<Earning>()

        val prepStatement = laConnexion.conn!!
            .prepareStatement(querieGetEarningsUserBetween2Dates)


        prepStatement.setInt(1, iduser)
        prepStatement.setString(2, dateDebut)
        prepStatement.setString(3, dateFin)



        val rs = prepStatement.executeQuery()

        while (rs.next()) {
            ar_Earnings.add(Earning(rs.getString("date"), rs.getDouble("euro"), rs.getDouble("kilowatter")))
        }

        return ar_Earnings

    }

    fun getPrice(): Price? {

        var price: Price? = null

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetPrice)

        val rs = query.executeQuery()


        if(rs.next()){
            price = Price(rs.getDouble("kwPrice"))
        }

        return price
    }

    fun deleteUser(iduser: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieDeleteUser)

        query.setInt(1, iduser)

        println(query)

        query.executeUpdate()
    }

    fun setUserIdToNullInInverterTable(iduser: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieSetUserIdToNullInverter)

        query.setInt(1, iduser)

        query.executeUpdate()
    }
}