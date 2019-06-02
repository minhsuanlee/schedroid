package edu.washington.minhsuan.schedroid

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.database.DatabaseUtils
import android.util.Log


class DatabaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val TAG = "DatabaseHelper"

    override fun onCreate(db: SQLiteDatabase) {
        val creatUserTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_NAME TEXT," +
                "$COL_PHONE TEXT," +
                "$COL_USERNAME TEXT," +
                "$COL_PASSWORD TEXT);")
        db.execSQL(creatUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(user: User) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME, user.full_name)
        cv.put(COL_PHONE, user.phone_num)
        cv.put(COL_USERNAME, user.username)
        cv.put(COL_PASSWORD, user.password)

        var rowID = db.insert(TABLE_NAME, null, cv)

        if (rowID == -1L) {
            Log.v(TAG, "Failed to insert user into database!")
        } else {
            Log.v(TAG, "Successfully inserted user into database!")
        }
    }

    fun readData(): MutableList<User> {
        val userList: MutableList<User> = ArrayList()

        val db = this.readableDatabase
        val result = db.rawQuery(READALL_STATEMENT, null)
        if (result.moveToFirst()) {
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.full_name = result.getString(result.getColumnIndex(COL_NAME))
                user.phone_num = result.getString(result.getColumnIndex(COL_PHONE))
                user.username = result.getString(result.getColumnIndex(COL_USERNAME))
                user.password = result.getString(result.getColumnIndex(COL_PASSWORD))
                userList.add(user)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return userList
    }

    fun deleteAll() {
        val db = this.writableDatabase
        db.execSQL(DELETE_STATEMENT)
        db.close()
    }

    fun deleteData(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun isExistUser(username: String): Boolean {
        val db = this.readableDatabase
        val count = DatabaseUtils.queryNumEntries(db, TABLE_NAME, "username=?", arrayOf(username))
        db.close()
        return count != 0L
    }

    fun isCorrectPassword(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val result = db.query(TABLE_NAME, arrayOf(COL_PASSWORD), "username=?", arrayOf(username),
            null, null, null)
        var storedPass = ""
        if (result.moveToFirst()) {
            storedPass = result.getString(result.getColumnIndex(COL_PASSWORD))
        }
        result.close()
        db.close()
        return storedPass == password
    }

    fun getName(username: String): String {
        val db = this.readableDatabase
        val result = db.query(TABLE_NAME, arrayOf(COL_NAME), "username=?", arrayOf(username),
            null, null, null)
        var name = ""
        if (result.moveToFirst()) {
            name = result.getString(result.getColumnIndex(COL_NAME))
        }
        result.close()
        db.close()
        return name
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ScheDB"

        val TABLE_NAME = "Users"
        val COL_ID = "id"
        val COL_NAME = "full_name"
        val COL_PHONE = "phone_num"
        val COL_USERNAME = "username"
        val COL_PASSWORD = "password"

        val READALL_STATEMENT = "SELECT * FROM $TABLE_NAME;"
        val DELETE_STATEMENT = "DELETE FROM $TABLE_NAME;"
    }
}