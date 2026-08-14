package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.generator.ContentGeneratorEngine
import com.example.data.local.AppDatabase
import com.example.data.local.SavedContentRepository
import com.example.data.model.ContentCategory
import com.example.data.model.ContentLanguage
import com.example.data.model.ContentTone
import com.example.data.model.GeneratedContent
import com.example.data.model.SocialPlatform
import com.example.data.model.SubCategory
import com.example.data.model.VideoDuration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CreationMode {
    QUICK_GENERATE,
    DETAILED_STUDIO
}

class ContentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SavedContentRepository
    private val generatorEngine = ContentGeneratorEngine()

    init {
        val db = AppDatabase.getInstance(application)
        repository = SavedContentRepository(db.savedContentDao())
    }

    private val _creationMode = MutableStateFlow(CreationMode.DETAILED_STUDIO)
    val creationMode: StateFlow<CreationMode> = _creationMode.asStateFlow()

    private val _selectedPlatform = MutableStateFlow(SocialPlatform.FACEBOOK)
    val selectedPlatform: StateFlow<SocialPlatform> = _selectedPlatform.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ContentCategory.EMOTIONAL)
    val selectedCategory: StateFlow<ContentCategory> = _selectedCategory.asStateFlow()

    private val _selectedSubCategory = MutableStateFlow<SubCategory>(ContentCategory.EMOTIONAL.subCategories.first())
    val selectedSubCategory: StateFlow<SubCategory> = _selectedSubCategory.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(ContentLanguage.BENGALI)
    val selectedLanguage: StateFlow<ContentLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedTone = MutableStateFlow(ContentTone.VIRAL_ENGAGING)
    val selectedTone: StateFlow<ContentTone> = _selectedTone.asStateFlow()

    private val _selectedDuration = MutableStateFlow(VideoDuration.SEC_30)
    val selectedDuration: StateFlow<VideoDuration> = _selectedDuration.asStateFlow()

    private val _topicInput = MutableStateFlow("")
    val topicInput: StateFlow<String> = _topicInput.asStateFlow()

    private val _quickTopicInput = MutableStateFlow("")
    val quickTopicInput: StateFlow<String> = _quickTopicInput.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _currentResult = MutableStateFlow<GeneratedContent?>(null)
    val currentResult: StateFlow<GeneratedContent?> = _currentResult.asStateFlow()

    private val _copiedFeedback = MutableStateFlow<String?>(null)
    val copiedFeedback: StateFlow<String?> = _copiedFeedback.asStateFlow()

    private val _showSavedSheet = MutableStateFlow(false)
    val showSavedSheet: StateFlow<Boolean> = _showSavedSheet.asStateFlow()

    private val _editingContent = MutableStateFlow<GeneratedContent?>(null)
    val editingContent: StateFlow<GeneratedContent?> = _editingContent.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterCategory = MutableStateFlow<String?>(null)
    val filterCategory: StateFlow<String?> = _filterCategory.asStateFlow()

    private val _isSavedSuccess = MutableStateFlow(false)
    val isSavedSuccess: StateFlow<Boolean> = _isSavedSuccess.asStateFlow()

    val savedContents: StateFlow<List<GeneratedContent>> = combine(
        repository.allContents,
        _searchQuery,
        _filterCategory
    ) { all, query, catFilter ->
        var list = all
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.topic.lowercase().contains(q) ||
                it.script.lowercase().contains(q) ||
                it.hook.lowercase().contains(q)
            }
        }
        if (catFilter != null) {
            list = list.filter { it.category.id == catFilter }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var copyFeedbackJob: Job? = null

    fun setCreationMode(mode: CreationMode) {
        _creationMode.value = mode
    }

    fun selectPlatform(platform: SocialPlatform) {
        _selectedPlatform.value = platform
    }

    fun selectCategory(category: ContentCategory) {
        _selectedCategory.value = category
        _selectedSubCategory.value = category.subCategories.firstOrNull() 
            ?: SubCategory("general", "General", "সাধারণ")
    }

    fun selectSubCategory(subCategory: SubCategory) {
        _selectedSubCategory.value = subCategory
    }

    fun selectLanguage(language: ContentLanguage) {
        _selectedLanguage.value = language
    }

    fun selectTone(tone: ContentTone) {
        _selectedTone.value = tone
    }

    fun selectDuration(duration: VideoDuration) {
        _selectedDuration.value = duration
    }

    fun setTopicInput(topic: String) {
        _topicInput.value = topic
    }

    fun setQuickTopicInput(topic: String) {
        _quickTopicInput.value = topic
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterCategory(catId: String?) {
        _filterCategory.value = if (_filterCategory.value == catId) null else catId
    }

    fun setShowSavedSheet(show: Boolean) {
        _showSavedSheet.value = show
    }

    fun clearCurrentResult() {
        _currentResult.value = null
        _isSavedSuccess.value = false
    }

    fun generateContent(isQuick: Boolean = false) {
        if (_isGenerating.value) return

        viewModelScope.launch {
            _isGenerating.value = true
            _isSavedSuccess.value = false

            val platform = _selectedPlatform.value
            val category: ContentCategory
            val subCategory: SubCategory
            val topic: String
            val language = _selectedLanguage.value
            val tone = _selectedTone.value

            if (isQuick) {
                topic = _quickTopicInput.value.ifBlank { "Viral Trending Reel" }
                // Intelligent inference for category based on quick topic words
                category = inferCategory(topic)
                subCategory = category.subCategories.first()
            } else {
                topic = _topicInput.value
                category = _selectedCategory.value
                subCategory = _selectedSubCategory.value
            }

            try {
                val generated = generatorEngine.generateContent(
                    platform = platform,
                    category = category,
                    subCategory = subCategory,
                    topic = topic,
                    language = language,
                    tone = tone,
                    duration = _selectedDuration.value
                )
                _currentResult.value = generated
            } catch (e: Exception) {
                // Should not happen as engine has smart fallback
            } finally {
                _isGenerating.value = false
            }
        }
    }

    private fun inferCategory(topic: String): ContentCategory {
        val lower = topic.lowercase()
        return when {
            lower.contains("হাসি") || lower.contains("মজা") || lower.contains("funny") || lower.contains("meme") || lower.contains("joke") -> ContentCategory.FUNNY
            lower.contains("আবেগ") || lower.contains("কান্না") || lower.contains("কষ্ট") || lower.contains("বাবা") || lower.contains("মা") || lower.contains("friend") || lower.contains("sad") || lower.contains("love") -> ContentCategory.EMOTIONAL
            lower.contains("ভালোবাসা") || lower.contains("প্রেম") || lower.contains("romantic") || lower.contains("crush") || lower.contains("couple") -> ContentCategory.ROMANTIC
            lower.contains("সফল") || lower.contains("মোটিভেশন") || lower.contains("motivat") || lower.contains("hustle") || lower.contains("success") || lower.contains("hard work") -> ContentCategory.MOTIVATIONAL
            lower.contains("টিপস") || lower.contains("শিক্ষা") || lower.contains("ai") || lower.contains("tech") || lower.contains("facts") || lower.contains("hack") || lower.contains("learn") -> ContentCategory.EDUCATIONAL
            lower.contains("গল্প") || lower.contains("রহস্য") || lower.contains("ভূত") || lower.contains("story") || lower.contains("mystery") || lower.contains("horror") -> ContentCategory.STORYTELLING
            lower.contains("গেম") || lower.contains("game") || lower.contains("pubg") || lower.contains("free fire") || lower.contains("clutch") -> ContentCategory.GAMING
            lower.contains("ব্যবসা") || lower.contains("টাকা") || lower.contains("business") || lower.contains("sales") || lower.contains("marketing") -> ContentCategory.BUSINESS
            else -> ContentCategory.EMOTIONAL
        }
    }

    fun saveCurrentContent() {
        val current = _currentResult.value ?: return
        viewModelScope.launch {
            val id = repository.saveContent(current)
            _currentResult.value = current.copy(id = id)
            _isSavedSuccess.value = true
            notifyCopied("Saved to drafts! (ড্রাফটে সেভ হয়েছে)")
        }
    }

    fun toggleFavorite(content: GeneratedContent) {
        viewModelScope.launch {
            val newFav = !content.isFavorite
            repository.toggleFavorite(content.id, newFav)
            if (_currentResult.value?.id == content.id) {
                _currentResult.value = _currentResult.value?.copy(isFavorite = newFav)
            }
        }
    }

    fun deleteSavedContent(id: Long) {
        viewModelScope.launch {
            repository.deleteContent(id)
            if (_currentResult.value?.id == id) {
                _currentResult.value = _currentResult.value?.copy(id = 0)
                _isSavedSuccess.value = false
            }
            notifyCopied("Deleted (মুছে ফেলা হয়েছে)")
        }
    }

    fun openEditDialog(content: GeneratedContent) {
        _editingContent.value = content
    }

    fun dismissEditDialog() {
        _editingContent.value = null
    }

    fun saveEditedContent(updated: GeneratedContent) {
        viewModelScope.launch {
            if (updated.id > 0) {
                repository.updateContent(updated)
            }
            if (_currentResult.value?.id == updated.id || _currentResult.value?.title == updated.title) {
                _currentResult.value = updated
            }
            _editingContent.value = null
            notifyCopied("Script updated! (স্ক্রিপ্ট আপডেট হয়েছে)")
        }
    }

    fun notifyCopied(message: String) {
        copyFeedbackJob?.cancel()
        copyFeedbackJob = viewModelScope.launch {
            _copiedFeedback.value = message
            delay(2200)
            _copiedFeedback.value = null
        }
    }

    fun applyQuickTopicPreset(preset: String) {
        _quickTopicInput.value = preset
        generateContent(isQuick = true)
    }

    fun loadDraftToViewer(draft: GeneratedContent) {
        _currentResult.value = draft
        _selectedPlatform.value = draft.platform
        _selectedCategory.value = draft.category
        _selectedSubCategory.value = draft.subCategory
        _selectedLanguage.value = draft.language
        _selectedTone.value = draft.tone
        _isSavedSuccess.value = true
        _showSavedSheet.value = false
    }
}
