package com.dosen.mynotemaster.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    val initialNote = if (isEditMode) viewModel.getNoteById(noteId) else null

    var textContent by rememberSaveable {
        mutableStateOf(initialNote?.content.orEmpty())
    }
    
    var selectedColor by rememberSaveable {
        mutableStateOf(initialNote?.color ?: 0xFFFFF9C4)
    }

    val noteColors = listOf(
        0xFFFFF9C4, // Kuning ceria
        0xFFFFCCBC, // Oranye lembut
        0xFFC8E6C9, // Hijau muda
        0xFFB3E5FC, // Biru langit
        0xFFF8BBD0  // Pink lucu
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Edit Catatan ✏️" else "Tulis Baru ✨",
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
                                color = selectedColor
                            )
                            onNavigateBack()
                        },
                        enabled = textContent.isNotBlank()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Simpan")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFCE93D8), // Ungu pastel
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF3E5F5)) // Background ungu sangat muda
                .padding(16.dp)
        ) {
            // Pemilih Warna
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    noteColors.forEach { colorVal ->
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(colorVal))
                                .border(
                                    width = if (selectedColor == colorVal) 4.dp else 1.dp,
                                    color = if (selectedColor == colorVal) Color(0xFF9C27B0) else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = colorVal }
                        )
                    }
                }
            }

            // Area Menulis
            TextField(
                value = textContent,
                onValueChange = { newText -> textContent = newText },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(selectedColor)),
                placeholder = { Text("Ayo tulis ceritamu di sini... 🌈") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = Color(0xFF3E2723)
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(selectedColor),
                    unfocusedContainerColor = Color(selectedColor),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}
