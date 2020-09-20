package com.example.gitstarscounter.data.to_rename_2.local.entity.database.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gitstarscounter.data.to_rename_2.local.entity.LocalUser
import com.example.gitstarscounter.entity.Repository

@Entity(
    tableName = "Repositories", foreignKeys = [
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION
        )]
)
data class TableRepository(
    @PrimaryKey
    val id: Long,

    val name: String?,

    @ColumnInfo(name = "all_stars_count")
    val allStarsCount: Int?,

    @ColumnInfo(name = "user_id")
    val userId: Long
) {
    constructor(repository: Repository) : this(
        id = repository.id,
        name = repository.name,
        allStarsCount = repository.allStarsCount,
        userId = repository.user.id
    )
}
