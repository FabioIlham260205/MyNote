package com.dosen.mynotemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dosen.mynotemaster.ui.theme.MyNoteTheme
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dosen.mynotemaster.navigation.MyNoteNavGraph
import com.dosen.mynotemaster.ui.theme.MyNoteTheme

/**
 * MainActivity sengaja dibuat SANGAT TIPIS: tugasnya hanya
 * "menyalakan" Compose lalu menyerahkan semuanya ke NavGraph.
 * Semua logika ada di ViewModel, semua tampilan ada di Screen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // UI modern: menggambar hingga di balik status bar
        setContent {
            MyNoteTheme {
                MyNoteNavGraph()
            }
        }
    }
}