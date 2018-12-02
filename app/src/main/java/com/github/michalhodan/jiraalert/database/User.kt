package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class User (
    @PrimaryKey val email: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun retrieve(): User?

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM user")
    fun wipe()
}