package com.example.safetrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class ContactModel(
    val name: String,
    @PrimaryKey
    val number: String
    )
