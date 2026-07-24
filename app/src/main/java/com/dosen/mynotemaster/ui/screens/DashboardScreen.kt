package com.dosen.mynotemaster.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dosen.mynotemaster.model.Note
import com.dosen.mynotemaster.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: NoteViewModel,
    onAddNote: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onAboutClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Catatanku 🌈", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp) 
                },
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(Icons.Filled.Info, contentDescription = "Tentang Aplikasi")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB3E5FC), // Biru langit cerah
                    titleContentColor = Color(0xFF01579B),
                    actionIconContentColor = Color(0xFF01579B)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNote,
                containerColor = Color(0xFFFFCC80), // Oranye ceria
                contentColor = Color(0xFF5D4037),
                shape = RoundedCornerShape(20.dp),
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Tulis Baru ✨", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Layar masih kosong nih...\nAyo buat catatan pertamamu! ✍️🌟",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = notes,
                    key = { note -> note.id }
                ) { note ->
                    NoteCard(note = note, onClick = { onNoteClick(note.id) })
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(note.color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = note.content,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
            color = Color(0xFF3E2723),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "🕒 ${formatTanggal(note.updatedAt)}",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF3E2723).copy(alpha = 0.6f)
        )
    }
}

private fun formatTanggal(millis: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    return formatter.format(Date(millis))
}
