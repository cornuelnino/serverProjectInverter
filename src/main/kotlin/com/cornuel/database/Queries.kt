package com.cornuel.bdd.services

import com.cornuel.models.*
import com.cornuel.models.jwt.Inverter
import com.cornuel.models.jwt.User

// Définition d'une classe Queries
class Queries() {

    companion object {

        // Requête SQL pour récupérer toutes les valeurs des utilisateurs et des onduleurs associés
        var querieGetAllUserValues =
            "SELECT user.iduser, user.email, user.name, user.isAdmin, user.createdAt, inverter.inverterName, inverter.macAddress, inverter.position, inverter.isOnline, inverter.batteryPercentage, inverter.outputActivePower, inverter.outputVoltage, settings.outputSourcePriority, warnings.inverterFault, warnings.lineFail, warnings.voltageTooLow, warnings.voltageTooHigh, warnings.overTemperature, warnings.fanLocked, warnings.batteryLowAlarm, warnings.softFail, warnings.batteryTooLowToCharge FROM inverter JOIN user JOIN settings JOIN warnings ON inverter.user_iduser = user.iduser AND inverter.settings_idsettings = settings.idsettings AND inverter.warnings_idwarnings = warnings.idwarnings AND inverter.warnings_idwarnings = warnings.idwarnings WHERE inverter.user_iduser = ?;"
        var querieGetAllInverterValues = "SELECT inverter.idinverter, inverter.inverterName, inverter.macAddress, inverter.position, inverter.isOnline, inverter.batteryPercentage, inverter.outputActivePower, inverter.outputVoltage, settings.outputSourcePriority, warnings.inverterFault, warnings.lineFail, warnings.voltageTooLow, warnings.voltageTooHigh, warnings.overTemperature, warnings.fanLocked, warnings.batteryLowAlarm, warnings.softFail, warnings.batteryTooLowToCharge FROM inverter JOIN settings JOIN warnings ON inverter.settings_idsettings = settings.idsettings AND inverter.warnings_idwarnings = warnings.idwarnings AND inverter.warnings_idwarnings = warnings.idwarnings WHERE inverter.idinverter = ?;"

        // Requêtes pour récupérer des informations sur les utilisateurs
        var querieGetAllUserId = "SELECT user.iduser FROM user"
        var querieGetAllInverterId = "SELECT inverter.idinverter FROM inverter"
        var querieGetUserId = "SELECT user.iduser FROM user WHERE user.email = ?"
        var querieGetPassword = "SELECT user.password FROM `user` WHERE user.iduser = ?"
        var querieGetAllLocationUsers = "SELECT user.iduser, inverter.position FROM inverter JOIN user ON inverter.user_iduser = user.iduser WHERE user.isAdmin = false"

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
            "INSERT INTO inverter (inverterName, macAddress, position, isOnline, batteryPercentage, outputActivePower, outputVoltage, warnings_idwarnings, settings_idsettings, user_iduser) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, null)"
        var querieInsertSettingsInverter =
            "INSERT INTO settings (outputSourcePriority) VALUES (?)"
        var querieInsertWarningInverter =
            "INSERT INTO warnings (inverterFault, lineFail, voltageTooLow, voltageTooHigh, overTemperature, fanLocked, batteryLowAlarm, softFail, batteryTooLowToCharge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

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
            "UPDATE inverter SET inverterName = ?, position = ?, isOnline = ?, batteryPercentage = ?, outputActivePower = ?, outputVoltage = ? WHERE idinverter = ?"
        var querieUpdateUser = "UPDATE user SET email = ?, name = ?, password = ? WHERE iduser = ?"
        var querieUpdateUserWithoutPassword = "UPDATE user SET email = ?, name = ? WHERE iduser = ?"
        var querieUpdateSettingsInverter = "UPDATE settings JOIN inverter ON settings.idsettings = inverter.settings_idsettings JOIN user ON user.iduser = inverter.user_iduser SET settings.outputSourcePriority = ? WHERE user.iduser = ?"
        var querieUpdateWarningsInverter = "UPDATE warnings JOIN inverter ON warnings.idwarnings = inverter.warnings_idwarnings SET warnings.inverterFault = ?, warnings.lineFail = ?, warnings.voltageTooLow = ?, warnings.voltageTooHigh = ?, warnings.overTemperature = ?, warnings.fanLocked = ?, warnings.batteryLowAlarm = ?, warnings.softFail = ?, warnings.batteryTooLowToCharge = ? WHERE inverter.macAddress = ?"

        // Requête pour obtenir les gains d'un utilisateur entre deux dates
        var querieGetEarningsUserBetween2Dates =
            "SELECT earnings.date, earnings.euro, earnings.kilowatter FROM `inverter` JOIN earnings on inverter.idinverter = earnings.inverter_idinverter WHERE inverter.user_iduser = ? AND earnings.date BETWEEN ? and ? ORDER BY earnings.date ASC"
    }


    // Initialisation de la connexion à la base de données
    var laConnexion = Connexion()


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
                rs.getString("inverterName"),
                rs.getString("macAddress"),
                rs.getString("position"),
                rs.getBoolean("isOnline"),
                rs.getInt("batteryPercentage"),
                rs.getDouble("outputActivePower"),
                rs.getDouble("outputVoltage"),
                rs.getInt("warnings_idwarnings"),
                rs.getInt("settings_idsettings"),
                rs.getInt("user_iduser")
            )
        }
        return inverter
    }

    // Méthode pour obtenir les valeurs d'un utilisateur
    fun getValuesUser(id: Int): AllUserValues? {

        var valeur: AllUserValues? = null

        val prepStatement = laConnexion.conn!!
            .prepareStatement(querieGetAllUserValues)

        prepStatement.setInt(1, id)

        val rs = prepStatement.executeQuery()

        while (rs.next()) {
            valeur = AllUserValues(
                rs.getInt("iduser"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getBoolean("isAdmin"),
                rs.getString("createdAt"),
                rs.getString("inverterName"),
                rs.getString("macAddress"),
                rs.getString("position"),
                rs.getBoolean("isOnline"),
                rs.getInt("batteryPercentage"),
                rs.getDouble("outputActivePower"),
                rs.getDouble("outputVoltage"),
                rs.getInt("outputSourcePriority"),
                rs.getBoolean("inverterFault"),
                rs.getBoolean("lineFail"),
                rs.getBoolean("voltageTooLow"),
                rs.getBoolean("voltageTooHigh"),
                rs.getBoolean("overTemperature"),
                rs.getBoolean("fanLocked"),
                rs.getBoolean("batteryLowAlarm"),
                rs.getBoolean("softFail"),
                rs.getBoolean("batteryTooLowToCharge")
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
        outputActivePower: Double,
        outputVoltage: Double,
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
        query.setDouble(6, outputActivePower)
        query.setDouble(7, outputVoltage)
        query.setInt(8, warningsid)
        query.setInt(9, settingsid)

        query.executeUpdate()
    }

    // Méthode pour insérer les paramètres de l'onduleur dans la base de données
    fun insertSettingsInverter(outputSourceDirectory: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertSettingsInverter)

        query.setInt(1, outputSourceDirectory)

        query.executeUpdate()

    }

    // Méthode pour insérer les avertissements de l'onduleur dans la base de données
    fun insertWarningInverter(
        inverterFault: Boolean,
        lineFail: Boolean,
        voltageTooLow: Boolean,
        voltageTooHigh: Boolean,
        overTemperature: Boolean,
        fanLocked: Boolean,
        batteryLowAlarm: Boolean,
        softFail: Boolean,
        batteryTooLowToCharge: Boolean
    ) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieInsertWarningInverter)

        query.setBoolean(1, inverterFault)
        query.setBoolean(2, lineFail)
        query.setBoolean(3, voltageTooLow)
        query.setBoolean(4, voltageTooHigh)
        query.setBoolean(5, overTemperature)
        query.setBoolean(6, fanLocked)
        query.setBoolean(7, batteryLowAlarm)
        query.setBoolean(8, softFail)
        query.setBoolean(9, batteryTooLowToCharge)

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
    fun updateInverter(name: String, position: String, online: Boolean, batteryPercentage: Int, outputActivePower: Double, outputVoltage: Double, idinverter: Int) {

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateInverter)

        query.setString(1, name)
        query.setString(2, position)
        query.setBoolean(3, online)
        query.setInt(4, batteryPercentage)
        query.setDouble(5, outputActivePower)
        query.setDouble(6, outputVoltage)
        query.setInt(7, idinverter)

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

    fun modifySettingsInverter(iduser: Int?, modifySettingsInverterModel: ModifySettingsInverterModel) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateSettingsInverter)

        query.setInt(1, modifySettingsInverterModel.outputSourcePriority!!)
        query.setInt(2, iduser!!)

        query.executeUpdate()

    }

    fun modifyWarningsInverter(modifyWarningsInverterModel: ModifyWarningsInverterModel) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateWarningsInverter)


        query.setBoolean(1, modifyWarningsInverterModel.inverterFault!!)
        query.setBoolean(2, modifyWarningsInverterModel.lineFail!!)
        query.setBoolean(3, modifyWarningsInverterModel.voltageTooLow!!)
        query.setBoolean(4, modifyWarningsInverterModel.voltageTooHigh!!)
        query.setBoolean(5, modifyWarningsInverterModel.overTemperature!!)
        query.setBoolean(6, modifyWarningsInverterModel.fanLocked!!)
        query.setBoolean(7, modifyWarningsInverterModel.batteryLowAlarm!!)
        query.setBoolean(8, modifyWarningsInverterModel.softFail!!)
        query.setBoolean(9, modifyWarningsInverterModel.batteryTooLowToCharge!!)
        query.setString(10, modifyWarningsInverterModel.macAddress)

        query.executeUpdate()
    }

    fun updateUserWithoutPassword(newEmail: String, newName: String, iduserSelected: Int) {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieUpdateUserWithoutPassword)

        query.setString(1, newEmail)
        query.setString(2, newName)
        query.setInt(3, iduserSelected)

        query.executeUpdate()
    }

    fun getAllLocationUsers(): ArrayList<LocationClient> {
        val arAllLocationUser = ArrayList<LocationClient>()

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetAllLocationUsers)

        val rs = query.executeQuery()

        while (rs.next()) {
            arAllLocationUser.add(LocationClient(rs.getInt("iduser"), rs.getString("position")))
        }

        return arAllLocationUser
    }

    fun getAllInverterId(): ArrayList<IdInverter>? {
        val ar_IdClient = ArrayList<IdInverter>()

        val query = laConnexion
            .getConnexion()!!
            .prepareStatement(querieGetAllInverterId)

        val rs = query.executeQuery()

        while (rs.next()) {
            ar_IdClient.add(IdInverter(rs.getInt("idinverter")))
        }

        return ar_IdClient
    }

    fun getValuesInverter(id: Int): AllInverterValues? {
        var valeur: AllInverterValues? = null

        val prepStatement = laConnexion.conn!!
            .prepareStatement(querieGetAllInverterValues)

        prepStatement.setInt(1, id)

        val rs = prepStatement.executeQuery()

        while (rs.next()) {
            valeur = AllInverterValues(
                rs.getInt("idinverter"),
                rs.getString("inverterName"),
                rs.getString("macAddress"),
                rs.getString("position"),
                rs.getBoolean("isOnline"),
                rs.getInt("batteryPercentage"),
                rs.getDouble("outputActivePower"),
                rs.getDouble("outputVoltage"),
                rs.getInt("outputSourcePriority"),
                rs.getBoolean("inverterFault"),
                rs.getBoolean("lineFail"),
                rs.getBoolean("voltageTooLow"),
                rs.getBoolean("voltageTooHigh"),
                rs.getBoolean("overTemperature"),
                rs.getBoolean("fanLocked"),
                rs.getBoolean("batteryLowAlarm"),
                rs.getBoolean("softFail"),
                rs.getBoolean("batteryTooLowToCharge")
            )
        }
        return valeur
    }
}