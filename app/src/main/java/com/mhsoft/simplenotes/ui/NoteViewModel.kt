package com.mhsoft.simplenotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhsoft.simplenotes.data.Note
import com.mhsoft.simplenotes.data.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val activeNotes: StateFlow<List<Note>> =
        repository.activeNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val deletedNotes: StateFlow<List<Note>> =
        repository.deletedNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }

    fun saveNote(
        id: Long?,
        title: String,
        text: String,
        colorHex: String,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            repository.saveNote(
                id = id,
                title = title,
                text = text,
                colorHex = colorHex
            )
            onSaved()
        }
    }

    fun moveToTrash(note: Note) {
        viewModelScope.launch {
            repository.moveToTrash(note)
        }
    }

    fun restoreNote(note: Note) {
        viewModelScope.launch {
            repository.restoreNote(note)
        }
    }

    fun deleteNoteForever(note: Note) {
        viewModelScope.launch {
            repository.deleteNoteForever(note)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            repository.emptyTrash()
        }
    }
}