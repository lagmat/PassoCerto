package passocerto.com.br.passocerto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.telecom.DisconnectCause.LOCAL
import android.util.Log
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NOME TEXT, $LOCAL TEXT, $DATA TEXT, $HORA TEXT, $DDD TEXT, $TELEFONE);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun cadastrarEvento(evento: Evento): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOME, evento.nome)
        values.put(LOCAL, evento.local)
        values.put(DATA, evento.data)
        values.put(HORA, evento.hora)
        values.put(DDD, evento.ddd)
        values.put(TELEFONE, evento.telefone)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getEvento(_id: Int): Evento {
        val evento = Evento()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        evento.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        evento.nome = cursor.getString(cursor.getColumnIndex(NOME))
        evento.local = cursor.getString(cursor.getColumnIndex(LOCAL))
        evento.data = cursor.getString(cursor.getColumnIndex(DATA))
        evento.hora = cursor.getString(cursor.getColumnIndex(HORA))
        evento.ddd = cursor.getString(cursor.getColumnIndex(DDD))
        evento.telefone = cursor.getString(cursor.getColumnIndex(TELEFONE))
        cursor.close()
        return evento
    }

    fun evento(): List<Evento> {
        val eventoList = ArrayList<Evento>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val eventos = Evento()
                    eventos.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    eventos.nome = cursor.getString(cursor.getColumnIndex(NOME))
                    eventos.local = cursor.getString(cursor.getColumnIndex(LOCAL))
                    eventos.data = cursor.getString(cursor.getColumnIndex(DATA))
                    eventos.hora = cursor.getString(cursor.getColumnIndex(HORA))
                    eventos.ddd = cursor.getString(cursor.getColumnIndex(DDD))
                    eventos.telefone = cursor.getString(cursor.getColumnIndex(TELEFONE))
                    eventoList.add(eventos)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return eventoList
    }

    fun atualizarEvento(evento: Evento): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOME, evento.nome)
        values.put(LOCAL, evento.local)
        values.put(DATA, evento.data)
        values.put(HORA, evento.hora)
        values.put(DDD, evento.ddd)
        values.put(TELEFONE, evento.telefone)

        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(evento.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun excluirEvento(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun excluirTodosEventos(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "PassoCerto"
        private val TABLE_NAME = "Eventos"
        private val ID = "Id"
        private val NOME = "Nome"
        private val LOCAL = "Local"
        private val DATA = "Data"
        private val HORA = "Hora"
        private val DDD = "Ddd"
        private val TELEFONE = "Telefone"
    }
}