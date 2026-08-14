package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContentCategory
import com.example.data.model.ContentLanguage
import com.example.data.model.SocialPlatform
import com.example.ui.components.CategorySelector
import com.example.ui.components.EditContentDialog
import com.example.ui.components.GeneratedResultCard
import com.example.ui.components.PlatformSelector
import com.example.ui.components.SavedContentBottomSheet
import com.example.ui.viewmodel.ContentViewModel
import com.example.ui.viewmodel.CreationMode

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: ContentViewModel,
    modifier: Modifier = Modifier
) {
    val creationMode by viewModel.creationMode.collectAsStateWithLifecycle()
    val selectedPlatform by viewModel.selectedPlatform.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedSubCategory by viewModel.selectedSubCategory.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedTone by viewModel.selectedTone.collectAsStateWithLifecycle()
    val selectedDuration by viewModel.selectedDuration.collectAsStateWithLifecycle()
    val topicInput by viewModel.topicInput.collectAsStateWithLifecycle()
    val quickTopicInput by viewModel.quickTopicInput.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val currentResult by viewModel.currentResult.collectAsStateWithLifecycle()
    val copiedFeedback by viewModel.copiedFeedback.collectAsStateWithLifecycle()
    val showSavedSheet by viewModel.showSavedSheet.collectAsStateWithLifecycle()
    val editingContent by viewModel.editingContent.collectAsStateWithLifecycle()
    val savedContents by viewModel.savedContents.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterCategory by viewModel.filterCategory.collectAsStateWithLifecycle()
    val isSavedSuccess by viewModel.isSavedSuccess.collectAsStateWithLifecycle()

    val quickPresets = remember {
        listOf(
            "ক্যাম্পাস জীবনের ৫টি মজার ভুল 😂",
            "বাবার নিঃশব্দ ভালোবাসা ও আত্মত্যাগ ❤️",
            "১ মিনিটে সেলস ৩ গুণ করার ট্রিকস 📈",
            "ভৌতিক ডায়েরির রহস্যময় ঘটনা 😱",
            "বিখ্যাত এআই টুলস যা কাজ সহজ করবে 🤖",
            "যেদিন সবাই সন্দেহ করবে, সেদিনই জিততে হবে 🔥",
            "১ বনাম ৪ অসম্ভব ক্লাচ গেমপ্লে 🎮",
            "ভালোবাসার না-বলা কিছু আবেগপূর্ণ কথা ✨"
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "App Logo",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "ContentCraft AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "ভাইরাল কনটেন্ট স্টুডিও",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.setShowSavedSheet(true) },
                        modifier = Modifier.testTag("button_open_saved")
                    ) {
                        BadgedBox(
                            badge = {
                                if (savedContents.isNotEmpty()) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(text = "${savedContents.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Saved Drafts",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 1. Hero Title Card ("What do you want to create?")
                HeroHeaderCard()

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Mode Switcher (Quick Generate vs Detailed Studio)
                ModeSwitcher(
                    currentMode = creationMode,
                    onModeSelected = { viewModel.setCreationMode(it) }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 3. Mode Dependent Form
                if (creationMode == CreationMode.QUICK_GENERATE) {
                    // Quick Generate Mode
                    QuickGenerateSection(
                        selectedPlatform = selectedPlatform,
                        onPlatformSelected = { viewModel.selectPlatform(it) },
                        quickTopic = quickTopicInput,
                        onTopicChange = { viewModel.setQuickTopicInput(it) },
                        presets = quickPresets,
                        onPresetClick = { viewModel.applyQuickTopicPreset(it) },
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { viewModel.selectLanguage(it) },
                        isGenerating = isGenerating,
                        onGenerate = { viewModel.generateContent(isQuick = true) }
                    )
                } else {
                    // Detailed Studio Mode
                    DetailedStudioSection(
                        selectedPlatform = selectedPlatform,
                        onPlatformSelected = { viewModel.selectPlatform(it) },
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        selectedSubCategory = selectedSubCategory,
                        onSubCategorySelected = { viewModel.selectSubCategory(it) },
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { viewModel.selectLanguage(it) },
                        selectedTone = selectedTone,
                        onToneSelected = { viewModel.selectTone(it) },
                        selectedDuration = selectedDuration,
                        onDurationSelected = { viewModel.selectDuration(it) },
                        topicInput = topicInput,
                        onTopicChange = { viewModel.setTopicInput(it) },
                        isGenerating = isGenerating,
                        onGenerate = { viewModel.generateContent(isQuick = false) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Loading Indicator / Generating Pulse
                AnimatedVisibility(
                    visible = isGenerating,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    GeneratingProgressCard(
                        platform = selectedPlatform,
                        category = selectedCategory
                    )
                }

                // 5. Generated Result View
                AnimatedVisibility(
                    visible = currentResult != null && !isGenerating,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    currentResult?.let { content ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Generated Content (তৈরি করা কনটেন্ট)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Tap Copy or Save",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            GeneratedResultCard(
                                content = content,
                                isSaved = isSavedSuccess,
                                onSaveClick = { viewModel.saveCurrentContent() },
                                onEditClick = { viewModel.openEditDialog(content) },
                                onRegenerateClick = {
                                    viewModel.generateContent(
                                        isQuick = creationMode == CreationMode.QUICK_GENERATE
                                    )
                                },
                                onCopyFeedback = { viewModel.notifyCopied(it) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            // Floating Copied Notification Banner
            AnimatedVisibility(
                visible = copiedFeedback != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) {
                copiedFeedback?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shadowElevation = 8.dp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Copied",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    // Saved Drafts BottomSheet
    if (showSavedSheet) {
        SavedContentBottomSheet(
            savedList = savedContents,
            searchQuery = searchQuery,
            onSearchChange = { viewModel.setSearchQuery(it) },
            filterCategory = filterCategory,
            onFilterCategoryChange = { viewModel.setFilterCategory(it) },
            onDraftSelect = { viewModel.loadDraftToViewer(it) },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onDeleteDraft = { viewModel.deleteSavedContent(it) },
            onDismiss = { viewModel.setShowSavedSheet(false) }
        )
    }

    // Edit Script Dialog
    editingContent?.let { contentToEdit ->
        EditContentDialog(
            content = contentToEdit,
            onSave = { updated -> viewModel.saveEditedContent(updated) },
            onDismiss = { viewModel.dismissEditDialog() }
        )
    }
}

@Composable
private fun HeroHeaderCard() {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "AI SCRIPT WRITER",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "What do you want to create?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "আজ আপনি কি কনটেন্ট বানাতে চান? ফেসবুক, টিকটক, ইউটিউব শর্টস ও ইনস্টাগ্রামের জন্য স্ক্রিপ্ট, হুক, ক্যাপশন ও হ্যাশট্যাগ তৈরি করুন নিমেষেই।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ModeSwitcher(
    currentMode: CreationMode,
    onModeSelected: (CreationMode) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            // Quick Generate Tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (currentMode == CreationMode.QUICK_GENERATE)
                            MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onModeSelected(CreationMode.QUICK_GENERATE) }
                    .testTag("tab_quick_generate")
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Quick Generate",
                        tint = if (currentMode == CreationMode.QUICK_GENERATE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Quick Generate (কুইক)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (currentMode == CreationMode.QUICK_GENERATE) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentMode == CreationMode.QUICK_GENERATE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Detailed Studio Tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (currentMode == CreationMode.DETAILED_STUDIO)
                            MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onModeSelected(CreationMode.DETAILED_STUDIO) }
                    .testTag("tab_detailed_studio")
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Detailed Studio",
                        tint = if (currentMode == CreationMode.DETAILED_STUDIO) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Detailed Studio (ডিটেইল্ড)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (currentMode == CreationMode.DETAILED_STUDIO) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentMode == CreationMode.DETAILED_STUDIO) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickGenerateSection(
    selectedPlatform: SocialPlatform,
    onPlatformSelected: (SocialPlatform) -> Unit,
    quickTopic: String,
    onTopicChange: (String) -> Unit,
    presets: List<String>,
    onPresetClick: (String) -> Unit,
    selectedLanguage: ContentLanguage,
    onLanguageSelected: (ContentLanguage) -> Unit,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Platform Picker
        PlatformSelector(
            selectedPlatform = selectedPlatform,
            onPlatformSelected = onPlatformSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Topic Box with AI Sparkle
        Text(
            text = "Your Topic or Idea (আপনার বিষয় বা আইডিয়া)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = quickTopic,
            onValueChange = onTopicChange,
            placeholder = {
                Text("যেমন: 'ক্যাম্পাস জীবনের মজার মুহূর্ত', '৫ মিনিটে এআই দিয়ে টাকা আয়', 'বাবার ত্যাগ'...")
            },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_quick_topic")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Preset Ideas Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Idea Suggestions",
                    tint = Color(0xFFFFB703),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Instant Viral Ideas (আইডিয়া সাজেশন)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            presets.forEach { preset ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clickable { onPresetClick(preset) }
                        .testTag("preset_chip_${preset.take(8)}")
                ) {
                    Text(
                        text = preset,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Language quick select
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            ContentLanguage.values().forEach { lang ->
                val isSel = lang == selectedLanguage
                FilterChip(
                    selected = isSel,
                    onClick = { onLanguageSelected(lang) },
                    label = { Text(text = lang.labelBn, fontSize = 11.sp) },
                    modifier = Modifier.testTag("quick_lang_${lang.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Big Generate Button
        Button(
            onClick = onGenerate,
            enabled = !isGenerating,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("button_quick_generate")
        ) {
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "Generate",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isGenerating) "AI জেনারেট হচ্ছে..." else "⚡ Quick Generate (ম্যাজিক জেনারেট)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailedStudioSection(
    selectedPlatform: SocialPlatform,
    onPlatformSelected: (SocialPlatform) -> Unit,
    selectedCategory: ContentCategory,
    onCategorySelected: (ContentCategory) -> Unit,
    selectedSubCategory: com.example.data.model.SubCategory,
    onSubCategorySelected: (com.example.data.model.SubCategory) -> Unit,
    selectedLanguage: ContentLanguage,
    onLanguageSelected: (ContentLanguage) -> Unit,
    selectedTone: com.example.data.model.ContentTone,
    onToneSelected: (com.example.data.model.ContentTone) -> Unit,
    selectedDuration: com.example.data.model.VideoDuration,
    onDurationSelected: (com.example.data.model.VideoDuration) -> Unit,
    topicInput: String,
    onTopicChange: (String) -> Unit,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Platform Picker
        PlatformSelector(
            selectedPlatform = selectedPlatform,
            onPlatformSelected = onPlatformSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categories, Sub-categories, Language, Tone & Duration
        CategorySelector(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            selectedSubCategory = selectedSubCategory,
            onSubCategorySelected = onSubCategorySelected,
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageSelected,
            selectedTone = selectedTone,
            onToneSelected = onToneSelected,
            selectedDuration = selectedDuration,
            onDurationSelected = onDurationSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Topic or Key points field
        Text(
            text = "Custom Topic or Key Points (ঐচ্ছিক বিষয়বস্তু)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = topicInput,
            onValueChange = onTopicChange,
            placeholder = {
                Text("যেমন: 'প্রথম বেতন পেয়ে মাকে সারপ্রাইজ দেওয়া' বা খালি রাখলে AI স্বয়ংক্রিয় থিম নেবে...")
            },
            minLines = 2,
            maxLines = 3,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_detailed_topic")
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Detailed Generate Button
        Button(
            onClick = onGenerate,
            enabled = !isGenerating,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("button_detailed_generate")
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Generate",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isGenerating) "AI স্ক্রিপ্ট তৈরি করছে..." else "✨ Generate Content (কনটেন্ট তৈরি করুন)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GeneratingProgressCard(
    platform: SocialPlatform,
    category: ContentCategory
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generating",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(rotation)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Writing viral script for ${platform.title}...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "AI হুক, সম্পূর্ণ স্ক্রিপ্ট, ক্যাপশন ও ভাইরাল হ্যাশট্যাগ তৈরি করছে...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
