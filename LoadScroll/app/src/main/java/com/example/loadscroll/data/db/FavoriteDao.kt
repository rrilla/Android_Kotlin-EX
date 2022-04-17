package com.example.loadscroll.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAll(): MutableList<Favorite>

    @Insert
    fun insert(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE gifId = :gifId")
    fun delete(gifId: String)
}