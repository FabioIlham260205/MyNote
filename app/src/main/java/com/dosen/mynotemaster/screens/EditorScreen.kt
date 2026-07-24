package com.dosen.mynotemaster.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosen.mynotemaster.navigation.Screen
import com.dosen.mynotemaster.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: NoteViewModel,
    noteId: Long,
    onNavigateBack: () -> Unit
) {
    val isEditMode = noteId != Screen.Editor.NO_ID
    val existingNote = if (isEditMode) viewModel.getNoteById(noteId) else null

    var textContent by rememberSaveable {
        mutableStateOf(existingNote?.content.orEmpty())
    }

    val backgroundColor = Color(0xFFFFF9C4) // Warna kuning pastel lucu

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Edit Catatan ✏️" else "Catatan Baru ✨",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveNote(
                                id = if (isEditMode) noteId else null,
                                content = textContent,
                                color = existingNote?.color ?: 0xFFFFF9C4L
                            )
                            onNavigateBack()
                        },
                        enabled = textContent.isNotBlank()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Simpan")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFCC80), // Warna oranye pastel
                    titleContentColor = Color(0xFF5D4037),
                    navigationIconContentColor = Color(0xFF5D4037),
                    actionIconContentColor = Color(0xFF5D4037)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5)) // Background abu-abu sangat muda
                .padding(16.dp)
        ) {
            TextField(
                value = textContent,
                onValueChange = { newText -> textContent = newText },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(backgroundColor),
                placeholder = { Text("Ayo tulis ceritamu di sini... 🌈") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = Color(0xFF5D4037)
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}
