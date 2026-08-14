package com.example.data.local

import com.example.data.model.GeneratedContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedContentRepository(private val dao: SavedContentDao) {

    val allContents: Flow<List<GeneratedContent>> = dao.getAllSavedContents().map { list ->
        list.map { it.toDomainModel() }
    }

    val favoriteContents: Flow<List<GeneratedContent>> = dao.getFavoriteContents().map { list ->
        list.map { it.toDomainModel() }
    }

    fun searchContents(query: String): Flow<List<GeneratedContent>> = dao.searchContents(query).map { list ->
        list.map { it.toDomainModel() }
    }

    fun getContentsByPlatform(platformId: String): Flow<List<GeneratedContent>> = dao.getContentsByPlatform(platformId).map { list ->
        list.map { it.toDomainModel() }
    }

    suspend fun saveContent(content: GeneratedContent): Long {
        val entity = SavedContentEntity.fromDomainModel(content)
        return dao.insertContent(entity)
    }

    suspend fun updateContent(content: GeneratedContent) {
        val entity = SavedContentEntity.fromDomainModel(content)
        dao.updateContent(entity)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }

    suspend fun deleteContent(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
