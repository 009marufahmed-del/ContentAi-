package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ContentCategory
import com.example.data.model.ContentLanguage
import com.example.data.model.ContentTone
import com.example.data.model.GeneratedContent
import com.example.data.model.SocialPlatform
import com.example.data.model.SubCategory

@Entity(tableName = "saved_contents")
data class SavedContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val hook: String,
    val script: String,
    val caption: String,
    val hashtagsRaw: String, // Comma or space separated
    val platformId: String,
    val categoryId: String,
    val subCategoryId: String,
    val subCategoryTitleEn: String,
    val subCategoryTitleBn: String,
    val languageId: String,
    val toneId: String,
    val topic: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    fun toDomainModel(): GeneratedContent {
        val plat = SocialPlatform.values().find { it.id == platformId } ?: SocialPlatform.FACEBOOK
        val cat = ContentCategory.values().find { it.id == categoryId } ?: ContentCategory.FUNNY
        val sub = cat.subCategories.find { it.id == subCategoryId } 
            ?: SubCategory(subCategoryId, subCategoryTitleEn, subCategoryTitleBn)
        val lang = ContentLanguage.values().find { it.id == languageId } ?: ContentLanguage.BENGALI
        val ton = ContentTone.values().find { it.id == toneId } ?: ContentTone.VIRAL_ENGAGING

        val tags = hashtagsRaw.split(" ", ",").map { it.trim() }.filter { it.isNotEmpty() }

        return GeneratedContent(
            id = id,
            title = title,
            hook = hook,
            script = script,
            caption = caption,
            hashtags = tags,
            platform = plat,
            category = cat,
            subCategory = sub,
            language = lang,
            tone = ton,
            topic = topic,
            timestamp = timestamp,
            isFavorite = isFavorite
        )
    }

    companion object {
        fun fromDomainModel(content: GeneratedContent): SavedContentEntity {
            return SavedContentEntity(
                id = content.id,
                title = content.title,
                hook = content.hook,
                script = content.script,
                caption = content.caption,
                hashtagsRaw = content.hashtags.joinToString(" "),
                platformId = content.platform.id,
                categoryId = content.category.id,
                subCategoryId = content.subCategory.id,
                subCategoryTitleEn = content.subCategory.titleEn,
                subCategoryTitleBn = content.subCategory.titleBn,
                languageId = content.language.id,
                toneId = content.tone.id,
                topic = content.topic,
                timestamp = content.timestamp,
                isFavorite = content.isFavorite
            )
        }
    }
}
