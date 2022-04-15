package com.example.loadscroll.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DB_NAME = "FavoriteDB"
    }
}