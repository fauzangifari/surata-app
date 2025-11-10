package com.fauzangifari.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "letters",
    indices = [Index(value = ["id"], unique = true)]
)
data class LetterEntity(
    @PrimaryKey val id: String,
    val applicantName: String,
    val applicantEmail: String,
    val subject: String,
    val endDate: String,
    val letterContent: String,
    val isPrinted: Boolean,
    val beginDate: String,
    val createdAt: String,
    val attachment: String,
    val letterType: String,
    val status: String,
    val letterNumber: String,
)