package com.mhsoft.simplenotes.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {

    val activeNotes: Flow<List<Note>> = noteDao.getActiveNotes()
    val deletedNotes: Flow<List<Note>> = noteDao.getDeletedNotes()

    suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)
    }

    suspend fun saveNote(
        id: Long?,
        title: String,
        text: String,
        colorHex: String
    ) {
        val now = System.currentTimeMillis()

        if (id == null || id == 0L) {
            noteDao.insertNote(
                Note(
                    title = title,
                    text = text,
                    colorHex = colorHex,
                    createdAt = now,
                    updatedAt = now
                )
            )
        } else {
            val oldNote = noteDao.getNoteById(id) ?: return

            noteDao.updateNote(
                oldNote.copy(
                    title = title,
                    text = text,
                    colorHex = colorHex,
                    updatedAt = now
                )
            )
        }
    }

    suspend fun moveToTrash(note: Note) {
        noteDao.updateNote(
            note.copy(
                isDeleted = true,
                deletedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun restoreNote(note: Note) {
        noteDao.updateNote(
            note.copy(
                isDeleted = false,
                deletedAt = null,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteNoteForever(note: Note) {
        noteDao.deleteNoteForever(note)
    }

    suspend fun emptyTrash() {
        noteDao.emptyTrash()
    }
}