package com.cornuel.bdd.services

import com.cornuel.models.AllValues
import com.cornuel.models.IdClient
import com.cornuel.models.UserModel
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

    fun loginAvailable(user: UserModel): Boolean {

        val storedHashedPassword = getHashedPasswordFromDb(user.username) ?: return false
        return checkPassword(user.password, storedHashedPassword)
    }

    fun getHashedPasswordFromDb(username: String?): String? {
        val query = laConnexion
            .getConnexion()!!
            .prepareStatement("SELECT password FROM user WHERE login = ?")

        query.setString(1, username)
        val rs = query.executeQuery()

        if (rs.next()) {
            return rs.getString("password")
        }
        return null
    }

    fun checkPassword(plainPassword: String?, hashedPassword: String): Boolean {

        return BCrypt.checkpw(plainPassword, hashedPassword)
    }

    fun hashPassword(password: String?): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }


}