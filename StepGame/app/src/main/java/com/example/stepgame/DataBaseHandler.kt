package com.example.stepgame

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "Db"
const val TABLE_NAME = "User"
const val COL_ID = "id"
const val COL_USERNAME = "userName"
const val COL_AGE = "age"
const val COL_UNITS = "units"
const val COL_GENDER = "gender"
const val COL_WEIGHT = "weight"
const val COL_STEPDIST = "stepdist"
const val COL_TOTALSTEPS = "totalsteps"
const val COL_LVL = "level"
const val COL_EXP = "exp"
const val COL_DAILY = "day"
const val COL_WEEKLY = "week"
const val COL_MONTHLY = "month"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME +" (" + COL_ID + " INTEGER," + COL_USERNAME+" VARCHAR(256),"+ COL_AGE + " INTEGER,"+ COL_UNITS+" INTEGER,"+ COL_GENDER+" INTEGER,"+ COL_WEIGHT+" INTEGER,"+COL_STEPDIST+" INTEGER,"+COL_TOTALSTEPS+ " FLOAT,"+COL_LVL+" INTEGER,"+ COL_DAILY+" INTEGER,"+ COL_WEEKLY+" INTEGER,"+ COL_MONTHLY+" INTEGER,"+COL_EXP+" INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun updateData(user: User){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID,1)
        cv.put(COL_USERNAME,user.userName)
        cv.put(COL_AGE,user.age)
        cv.put(COL_UNITS,user.units)
        cv.put(COL_GENDER,user.gender)
        cv.put(COL_WEIGHT,user.weight)
        cv.put(COL_STEPDIST, user.stepdist)
        cv.put(COL_TOTALSTEPS, user.totalsteps)
        cv.put(COL_LVL, user.level)
        cv.put(COL_DAILY, user.dail)
        cv.put(COL_WEEKLY, user.week)
        cv.put(COL_MONTHLY, user.month)
        cv.put(COL_EXP, user.exp)
        db.update(TABLE_NAME, cv, "$COL_ID=1", null)
    }

    fun insertData(user: User){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID, 1)
        cv.put(COL_USERNAME,user.userName)
        cv.put(COL_AGE,user.age)
        cv.put(COL_UNITS,user.units)
        cv.put(COL_GENDER,user.gender)
        cv.put(COL_WEIGHT,user.weight)
        cv.put(COL_STEPDIST, user.stepdist)
        cv.put(COL_TOTALSTEPS, user.totalsteps)
        cv.put(COL_LVL, user.level)
        cv.put(COL_DAILY, user.dail)
        cv.put(COL_WEEKLY, user.week)
        cv.put(COL_MONTHLY, user.month)
        cv.put(COL_EXP, user.exp)
        db.insert(TABLE_NAME,null,cv)
    }

    fun readData():MutableList<User> {
        var list: MutableList<User> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()) {
            do {
                var user = User()
                user.id = result.getString(0).toInt()
                user.userName = result.getString(1)
                user.age = result.getString(2).toInt()
                user.units = result.getString(3).toInt()
                user.gender = result.getString(4).toInt()
                user.weight = result.getString(5).toInt()
                user.stepdist = result.getString(6).toInt()
                user.totalsteps = result.getString(7).toFloat()
                user.level = result.getString(8).toInt()
                user.dail = result.getString(9).toInt()
                user.week = result.getString(10).toInt()
                user.month = result.getString(11).toInt()
                user.exp = result.getString(12).toInt()
                list.add(user)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

}