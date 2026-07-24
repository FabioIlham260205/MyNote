package com.dosen.mynotemaster.model

/**
 * Note adalah "cetakan" (blueprint) satu catatan.
 *
 * MENGAPA data class?
 * - equals()/hashCode() otomatis → Compose bisa mendeteksi perubahan data
 *   secara efisien saat recomposition.
 * - copy() otomatis → mendukung prinsip IMMUTABILITY: kita tidak mengubah
 *   objek lama, melainkan membuat salinan baru dengan nilai berbeda.
 *   Analogi: seperti kertas sticky note — kita tidak menghapus tulisan lama
 *   dengan tip-ex, melainkan menempel kertas baru menggantikannya.
 */
data class Note(
    val id: Long,                 // Identitas unik — kunci untuk fitur edit
    val content: String,          // Isi catatan yang ditulis pengguna
    val color: Long = 0xFFFFF9C4, // Warna catatan (default kuning sticky note)
    val updatedAt: Long = System.currentTimeMillis() // Kapan terakhir diubah
)