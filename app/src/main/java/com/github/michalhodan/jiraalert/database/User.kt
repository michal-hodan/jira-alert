package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class User (
    @PrimaryKey val name: String,
    val email: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String
)

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE name = :name LIMIT 1")
    fun retrieve(name: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("DELETE FROM user")
    fun wipe()
}
