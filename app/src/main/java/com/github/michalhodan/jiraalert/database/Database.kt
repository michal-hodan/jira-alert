package com.github.michalhodan.jiraalert.database

import android.content.Context
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.github.michalhodan.jiraalert.database.Database as AppDatabase

@Database(
    entities = [
        User::class, Board::class, Sprint::class,
        Issue::class, Project::class, IssueType::class,
        Priority::class, Configuration::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun user(): UserDao

    abstract fun issue(): IssueDao

    abstract fun board(): BoardsDao

    abstract fun sprint(): SprintDao

    abstract fun project(): ProjectDao

    abstract fun priority(): PriorityDao

    abstract fun issueType(): IssueTypeDao

    abstract fun configuration(): ConfigurationDao

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