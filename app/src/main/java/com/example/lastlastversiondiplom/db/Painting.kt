package com.example.lastlastversiondiplom.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paintings")
data class Painting(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var year: String = "",
    var author: String = "",
    var history: String = "",
    var artistFact: String = "",
    var paintingFact: String = "",
    var image: ByteArray? = null
)