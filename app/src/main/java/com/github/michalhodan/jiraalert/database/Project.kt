package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class Project(
    @PrimaryKey val id: Int,
    val key: String,
    val name: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String
)

@Dao
interface ProjectDao {

    @Query("SELECT * FROM Project WHERE id = :id LIMIT 1")
    fun retrieve(id: Int): Project?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: Project)

    @Query("DELETE FROM Project")
    fun wipe()
}