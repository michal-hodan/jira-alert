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

    @Query("SELECT * FROM Sprint WHERE id = :id")
    fun retrieve(id: Int): Sprint?

    @Query("SELECT * FROM Sprint")
    fun boardSprints(): List<Sprint>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sprints: List<Sprint>)
}