package com.mhsoft.simplenotes.di

import android.content.Context
import androidx.room.Room
import com.mhsoft.simplenotes.data.NoteDao
import com.mhsoft.simplenotes.data.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "simple_notes_database"
        ).build()
    }

    @Provides
    fun provideNoteDao(
        database: NoteDatabase
    ): NoteDao {
        return database.noteDao()
    }
}