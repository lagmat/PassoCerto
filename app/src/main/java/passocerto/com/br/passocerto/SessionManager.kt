package passocerto.com.br.passocerto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

public class SessionManager {

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATE_MODE: Int = 0

    constructor(con: Context){
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {

        val PREF_NAME: String = "SessaoKotlin"
        val IS_LOGIN: String = "isLoggedIn"
        val KEY_NOME: String = "nome"
        val KEY_LOGIN: String = "login"
        val KEY_EMAIL: String = "email"
    }

    fun criateLoginSession(nome: String, login: String, email: String) {

        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_NOME, nome)
        editor.putString(KEY_LOGIN, login)
        editor.putString(KEY_EMAIL, email)
        editor.commit()
    }

    fun checkLogin(){
        if(!this.isLoggedIn()) {

            var i : Intent = Intent(con, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)

        }
    }

    fun getUserDetails(): HashMap<String, String> {

        var user: Map<String, String> = HashMap<String, String>()
        (user as HashMap).put(KEY_NOME, pref.getString(KEY_NOME, null))
        (user as HashMap).put(KEY_LOGIN, pref.getString(KEY_LOGIN, null))
        (user as HashMap).put(KEY_EMAIL, pref.getString(KEY_EMAIL, null))
        return user
    }

    fun logoutUser(){

        editor.clear()
        editor.commit()

        var i : Intent = Intent(con, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }

    fun isLoggedIn(): Boolean {

        return pref.getBoolean(IS_LOGIN, false)
    }

}