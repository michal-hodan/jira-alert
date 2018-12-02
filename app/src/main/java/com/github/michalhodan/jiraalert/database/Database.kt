package com.github.michalhodan.jiraalert.database

import android.content.Context
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.github.michalhodan.jiraalert.database.Database as AppDatabase

@Database(entities = [User::class, Board::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun user(): UserDao

    abstract fun boards(): BoardsDao

    companion object {
        private const val DATABASE_NAME = "jira-alert"

        private lateinit var applicationContext: Context

        fun bootstrap(applicationContext: Context) {
            this.applicationContext = applicationContext
        }

        fun dropDatabase() {
            applicationContext.deleteDatabase(DATABASE_NAME)
        }

        val instance by lazy {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

}