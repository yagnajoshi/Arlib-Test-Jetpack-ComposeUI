package com.arlibs.yagna.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LoginTable::class], version = 1, exportSchema = false)
abstract class LoginDatabase : RoomDatabase() {
    abstract fun loginDoa(): LoginDao?

    companion object {
        private var INSTANCE: LoginDatabase? = null
        fun getDatabase(context: Context?): LoginDatabase? {
            if (INSTANCE == null) {
                synchronized(LoginDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context!!, LoginDatabase::class.java, "ARLIB_DATABASE")
                            .fallbackToDestructiveMigration()
                            /*.addCallback(dbCallback)*/
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        var dbCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {

            }
        }
    }




}