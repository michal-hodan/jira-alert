package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class Priority(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "icon_url") val iconUrl: String
)

@Dao
interface PriorityDao {

    @Query("SELECT * FROM Priority WHERE id = :id LIMIT 1")
    fun retrieve(id: Int): Priority?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(priority: Priority)

    @Query("DELETE FROM Priority")
    fun wipe()
}