package com.example.appwithregistration

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject

class DBhelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_LOGIN + " TEXT PRIMARY KEY," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_CRYPTOS + " TEXT" + ")")
        db.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put(COLUMN_LOGIN, user.login)
        values.put(COLUMN_EMAIL, user.email)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_CRYPTOS, JSONArray(user.cryptos.map { crypto ->
            JSONObject().apply {
                put("amount", crypto.amount)
                put("type", crypto.type)
            }
        }).toString())

        val db = this.writableDatabase
        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    fun getUser(login: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_LOGIN = ? AND $COLUMN_PASSWORD = ?", arrayOf(login, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun getUserCryptos(login: String): MutableList<CryptoCurrency> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CRYPTOS FROM $TABLE_USERS WHERE $COLUMN_LOGIN = ?", arrayOf(login))
        val cryptos = mutableListOf<CryptoCurrency>()
        if (cursor.moveToFirst()) {
            val cryptosJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CRYPTOS))
            val cryptosArray = JSONArray(cryptosJson)
            for (i in 0 until cryptosArray.length()) {
                val cryptoObject = cryptosArray.getJSONObject(i)
                val amount = cryptoObject.getDouble("amount")
                val type = cryptoObject.getString("type")
                cryptos.add(CryptoCurrency(amount, type))
            }
        }
        cursor.close()
        db.close()
        return cryptos
    }

    fun addCryptoToUser(login: String, crypto: CryptoCurrency) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CRYPTOS FROM $TABLE_USERS WHERE $COLUMN_LOGIN = ?", arrayOf(login))
        if (cursor.moveToFirst()) {
            val cryptosJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CRYPTOS))
            val cryptos = JSONArray(cryptosJson)
            cryptos.put(JSONObject().apply {
                put("amount", crypto.amount)
                put("type", crypto.type)
            })
            val values = ContentValues()
            values.put(COLUMN_CRYPTOS, cryptos.toString())
            db.update(TABLE_USERS, values, "$COLUMN_LOGIN = ?", arrayOf(login))
        }
        cursor.close()
        db.close()
    }
    fun deleteCryptoFromUser(login: String, crypto: CryptoCurrency) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CRYPTOS FROM $TABLE_USERS WHERE $COLUMN_LOGIN = ?", arrayOf(login))
        if (cursor.moveToFirst()) {
            val cryptosJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CRYPTOS))
            val cryptosArray = JSONArray(cryptosJson)
            var indexToDelete = -1
            for (i in 0 until cryptosArray.length()) {
                val cryptoObject = cryptosArray.getJSONObject(i)
                val amount = cryptoObject.getDouble("amount")
                val type = cryptoObject.getString("type")
                if (amount == crypto.amount && type == crypto.type) {
                    indexToDelete = i
                    break
                }
            }
            if (indexToDelete != -1) {
                cryptosArray.remove(indexToDelete)
                val values = ContentValues()
                values.put(COLUMN_CRYPTOS, cryptosArray.toString())
                db.update(TABLE_USERS, values, "$COLUMN_LOGIN = ?", arrayOf(login))
            }
        }
        cursor.close()
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "userDB.db"
        private const val TABLE_USERS = "users"

        private const val COLUMN_LOGIN = "login"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_CRYPTOS = "cryptos"
    }
}
