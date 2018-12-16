package com.github.michalhodan.jiraalert.database

import android.arch.persistence.room.*

@Entity
data class IssueType(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "icon_url") val iconUrl: String
)

@Dao
interface IssueTypeDao {

    @Query("SELECT * FROM IssueType WHERE id = :id LIMIT 1")
    fun retrieve(id: Int): IssueType?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(issueType: IssueType)

    @Query("DELETE FROM IssueType")
    fun wipe()
}