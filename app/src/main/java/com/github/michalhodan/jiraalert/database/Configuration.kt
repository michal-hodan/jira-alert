package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*
import java.util.ArrayList

data class Column(
    val name: String,
    val statuses: List<Int>,
    val min: Int?,
    val max: Int?
)

@Entity(foreignKeys = [
    ForeignKey(
        entity = Board::class,
        parentColumns = ["id"],
        childColumns = ["board_id"]
    )
])
data class Configuration(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "board_id") val boardId: Int,
    @Embedded val columns: ArrayList<Column>
)

@Dao
interface ConfigurationDao {

    @Query("SELECT * FROM Configuration WHERE board_id = :boardId LIMIT 1")
    fun retrieveByBoard(boardId: Int): Configuration?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(priority: Configuration)

    @Query("DELETE FROM Configuration")
    fun wipe()
}