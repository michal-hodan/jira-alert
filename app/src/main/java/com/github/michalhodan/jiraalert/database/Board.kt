package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class Board (
    @PrimaryKey val id: Int,
    val name: String,
    val type: String
)

@Dao
interface BoardsDao {
    @Query("SELECT * FROM Board")
    fun retrieveAll(): List<Board>

    @Insert
    fun insertAll(board: List<Board>)

    @Query("DELETE FROM Board")
    fun wipe()
}

