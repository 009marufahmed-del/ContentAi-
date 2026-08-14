package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedContentDao {
    @Query("SELECT * FROM saved_contents ORDER BY timestamp DESC")
    fun getAllSavedContents(): Flow<List<SavedContentEntity>>

    @Query("SELECT * FROM saved_contents WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteContents(): Flow<List<SavedContentEntity>>

    @Query("SELECT * FROM saved_contents WHERE platformId = :platformId ORDER BY timestamp DESC")
    fun getContentsByPlatform(platformId: String): Flow<List<SavedContentEntity>>

    @Query("SELECT * FROM saved_contents WHERE title LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' OR script LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchContents(query: String): Flow<List<SavedContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(entity: SavedContentEntity): Long

    @Update
    suspend fun updateContent(entity: SavedContentEntity)

    @Query("UPDATE saved_contents SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM saved_contents WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteContent(entity: SavedContentEntity)

    @Query("DELETE FROM saved_contents")
    suspend fun clearAll()
}
