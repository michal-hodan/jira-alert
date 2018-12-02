package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class User (
    @PrimaryKey var email: String,
    @ColumnInfo(name = "display_name") var displayName: String,
    @ColumnInfo(name = "avatar_url") var avatarUrl: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun retrieve(): User?

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}