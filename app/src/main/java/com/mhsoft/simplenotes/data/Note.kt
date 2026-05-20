package com.mhsoft.simplenotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String = "",
    val text: String = "",

    val colorHex: String = "#FFF8E1",

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)