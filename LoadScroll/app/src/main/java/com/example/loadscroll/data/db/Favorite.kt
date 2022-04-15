package com.example.loadscroll.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
//    @PrimaryKey val id: String,
    @PrimaryKey val gifId: String
)
