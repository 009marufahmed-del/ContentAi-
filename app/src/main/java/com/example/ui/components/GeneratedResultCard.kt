package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeneratedContent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeneratedResultCard(
    content: GeneratedContent,
    isSaved: Boolean,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit,
    onRegenerateClick: () -> Unit,
    onCopyFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        onCopyFeedback("$label copied! ($label কপি করা হয়েছে)")
    }

    fun shareContent() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content.getFullTextForSharing())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Viral Content")
        context.startActivity(shareIntent)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.5.dp,
                color = content.platform.brandColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        // 1. Header with Platform Badge & Metadata
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = content.platform.brandColor
                ) {
                    Text(
                        text = content.platform.title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = content.category.accentColor.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = "${content.category.titleBn} • ${content.subCategory.titleBn}",
                        style = MaterialTheme.typography.labelSmall,
                        color = content.category.accentColor,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }

            Row {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.testTag("action_edit_script")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Script",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { shareContent() },
                    modifier = Modifier.testTag("action_share_script")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(14.dp))

        // 2. Section 1: TITLE (টাইটেল)
        ContentSectionBox(
            tagLabel = "🎯 TITLE (টাইটেল)",
            tagColor = MaterialTheme.colorScheme.primary,
            onCopy = { copyToClipboard(content.title, "Title") },
            testTagSuffix = "title"
        ) {
            Text(
                text = content.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 26.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Section 2: HOOK (প্রথম ৩ সেকেন্ডের হুক) - Special Highlighted Box
        ContentSectionBox(
            tagLabel = "🪝 HOOK (প্রথম ৩ সেকেন্ডের হুক)",
            tagColor = Color(0xFFEF4444),
            onCopy = { copyToClipboard(content.hook, "Hook") },
            highlighted = true,
            testTagSuffix = "hook"
        ) {
            Text(
                text = content.hook,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Section 3: FULL SCRIPT (সম্পূর্ণ স্ক্রিপ্ট)
        ContentSectionBox(
            tagLabel = "📜 FULL SCRIPT (ভিডিওর সম্পূর্ণ স্ক্রিপ্ট)",
            tagColor = MaterialTheme.colorScheme.secondary,
            onCopy = { copyToClipboard(content.script, "Script") },
            testTagSuffix = "script"
        ) {
            Text(
                text = content.script,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.95f),
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Section 4: CAPTION (ক্যাপশন ও কল-টু-অ্যাকশন)
        ContentSectionBox(
            tagLabel = "✍️ CAPTION (ক্যাপশন ও CTA)",
            tagColor = MaterialTheme.colorScheme.tertiary,
            onCopy = { copyToClipboard(content.caption, "Caption") },
            testTagSuffix = "caption"
        ) {
            Text(
                text = content.caption,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 21.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 6. Section 5: HASHTAGS (হ্যাশট্যাগ)
        ContentSectionBox(
            tagLabel = "🏷️ HASHTAGS (ভাইরাল হ্যাশট্যাগ)",
            tagColor = Color(0xFF10B981),
            onCopy = { copyToClipboard(content.hashtags.joinToString(" "), "Hashtags") },
            testTagSuffix = "hashtags"
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                content.hashtags.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.12f),
                        modifier = Modifier.clickable {
                            copyToClipboard(tag, "Tag $tag")
                        }
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF059669),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 7. Bottom Actions Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    copyToClipboard(content.getFullTextForSharing(), "Entire Package")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .testTag("button_copy_all")
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy All", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy All (সব কপি)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = onSaveClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.testTag("button_save_draft")
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save Draft",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSaved) "Saved" else "Save",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            IconButton(
                onClick = onRegenerateClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .testTag("button_regenerate")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Regenerate",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ContentSectionBox(
    tagLabel: String,
    tagColor: Color,
    onCopy: () -> Unit,
    highlighted: Boolean = false,
    testTagSuffix: String,
    content: @Composable () -> Unit
) {
    val bgBrush = if (highlighted) {
        Brush.verticalGradient(
            listOf(
                tagColor.copy(alpha = 0.12f),
                tagColor.copy(alpha = 0.04f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgBrush)
            .border(
                width = if (highlighted) 1.5.dp else 1.dp,
                color = if (highlighted) tagColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tagLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = tagColor
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = tagColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .clickable(onClick = onCopy)
                        .testTag("copy_button_$testTagSuffix")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy $tagLabel",
                            tint = tagColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Copy",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = tagColor,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            content()
        }
    }
}
