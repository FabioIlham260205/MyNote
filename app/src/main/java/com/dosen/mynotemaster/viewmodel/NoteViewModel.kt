package com.dosen.mynotemaster.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.dosen.mynotemaster.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * NoteViewModel = "otak" aplikasi yang menyimpan dan mengelola semua catatan.
 * Menggunakan SharedPreferences untuk persistensi data.
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // _notes bersifat PRIVATE dan MUTABLE — hanya ViewModel yang boleh mengubah.
    private val _notes = MutableStateFlow<List<Note>>(loadNotes())

    // notes bersifat PUBLIC dan READ-ONLY — UI hanya boleh MEMBACA.
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    // Ambil nextId terakhir dari prefs atau mulai dari 1
    private var nextId = prefs.getLong("next_id", 1L)

    private fun loadNotes(): List<Note> {
        val json = prefs.getString("notes_list", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Note>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveNotesToPrefs(notes: List<Note>) {
        val json = gson.toJson(notes)
        prefs.edit().putString("notes_list", json).apply()
    }

    private fun updateNextId(id: Long) {
        nextId = id
        prefs.edit().putLong("next_id", nextId).apply()
    }

    /** Mencari catatan berdasarkan id — dipakai EditorScreen saat mode edit. */
    fun getNoteById(id: Long): Note? =
        _notes.value.find { it.id == id }

    /**
     * Satu fungsi untuk DUA skenario (prinsip DRY):
     * - id == null  → CREATE : buat catatan baru
     * - id != null  → UPDATE : perbarui catatan yang sudah ada
     */
    fun saveNote(id: Long?, content: String, color: Long) {
        // Validasi: catatan kosong tidak layak disimpan
        if (content.isBlank()) return

        _notes.update { currentList ->
            val newList = if (id == null) {
                // CREATE: catatan baru ditaruh PALING ATAS
                val newId = nextId
                updateNextId(nextId + 1)
                listOf(Note(id = newId, content = content.trim(), color = color)) + currentList
            } else {
                // UPDATE: gunakan copy()
                currentList.map { note ->
                    if (note.id == id) {
                        note.copy(
                            content = content.trim(),
                            color = color,
                            updatedAt = System.currentTimeMillis()
                        )
                    } else {
                        note
                    }
                }
            }
            saveNotesToPrefs(newList)
            newList
        }
    }

    /** Bonus: hapus catatan */
    fun deleteNote(id: Long) {
        _notes.update { currentList ->
            val newList = currentList.filterNot { it.id == id }
            saveNotesToPrefs(newList)
            newList
        }
    }
}