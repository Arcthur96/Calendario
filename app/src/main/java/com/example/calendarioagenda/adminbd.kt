package com.example.calendarioagenda

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

//crear clase de base de datos

class adminbd(context: Context): SQLiteOpenHelper(context,DataBase,null,1) {
    companion object{
        val DataBase = "Contactos"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //crear tablas
        db?.execSQL("create table usuario (correoUsr text primary key,nomusr text,contrasena text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    //Función para mandar ejecutar un Insert,update o delete
    fun Ejecuta(sentencia: String) : Int{
        try {
            val db = this.writableDatabase
            db.execSQL(sentencia)
            return 1
        }
        catch (ex: Exception){
            return 0 //Terminación no exitosa
        }
    }

    //funcion para mandar ejecutar una consulta SQL (Select)
    fun Consulta(query: String): Cursor?{
        try {
            val db = this.readableDatabase
            return db.rawQuery(query,null)

        }
        catch (ex:java.lang.Exception) {
            return null
        }
    }
}
