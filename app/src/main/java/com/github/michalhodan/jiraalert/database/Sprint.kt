package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(
        entity = Board::class,
        parentColumns = ["id"],
        childColumns = ["board_id"]
    )
])
data class Sprint(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "board_id") val boardId: Int,
    val name: String,
    val state: String
)

@Dao
interface SprintDao {

    @Query("Select * FROM Sprint WHERE board_id = :boardId")
    fun boardSprints(boardId: Int): List<Sprint>

    @Insert
    fun insertAll(sprints: List<Sprint>)
}