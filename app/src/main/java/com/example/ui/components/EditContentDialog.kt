package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.GeneratedContent

@Composable
fun EditContentDialog(
    content: GeneratedContent,
    onSave: (GeneratedContent) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(content.title) }
    var hook by remember { mutableStateOf(content.hook) }
    var script by remember { mutableStateOf(content.script) }
    var caption by remember { mutableStateOf(content.caption) }
    var hashtagsStr by remember { mutableStateOf(content.hashtags.joinToString(" ")) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Edit Content (কাস্টমাইজ করুন)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title Input
                Text("Title (টাইটেল)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth().testTag("edit_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Hook Input
                Text("Hook (প্রথম ৩ সেকেন্ডের হুক)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = hook,
                    onValueChange = { hook = it },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth().testTag("edit_hook_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Script Input
                Text("Script (সম্পূর্ণ স্ক্রিপ্ট)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = script,
                    onValueChange = { script = it },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth().testTag("edit_script_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Caption Input
                Text("Caption (ক্যাপশন)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("edit_caption_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Hashtags Input
                Text("Hashtags (হ্যাশট্যাগ)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = hashtagsStr,
                    onValueChange = { hashtagsStr = it },
                    modifier = Modifier.fillMaxWidth().testTag("edit_hashtags_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val tags = hashtagsStr.split(" ", "\n", ",")
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }
                                .map { if (it.startsWith("#")) it else "#$it" }
                            onSave(
                                content.copy(
                                    title = title.trim(),
                                    hook = hook.trim(),
                                    script = script.trim(),
                                    caption = caption.trim(),
                                    hashtags = tags
                                )
                            )
                        },
                        modifier = Modifier.testTag("button_save_edited_script")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Update (আপডেট)")
                    }
                }
            }
        }
    }
}
