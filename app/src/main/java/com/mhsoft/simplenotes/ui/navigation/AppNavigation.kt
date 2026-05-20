package com.mhsoft.simplenotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mhsoft.simplenotes.ui.NoteViewModel
import com.mhsoft.simplenotes.ui.screens.EditNoteScreen
import com.mhsoft.simplenotes.ui.screens.NotesListScreen
import com.mhsoft.simplenotes.ui.screens.TrashScreen

object Routes {
    const val NOTES = "notes"
    const val TRASH = "trash"
    const val EDIT = "edit"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: NoteViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.NOTES
    ) {
        composable(Routes.NOTES) {
            NotesListScreen(
                viewModel = viewModel,
                onAddNote = {
                    navController.navigate("${Routes.EDIT}/0")
                },
                onEditNote = { noteId ->
                    navController.navigate("${Routes.EDIT}/$noteId")
                },
                onOpenTrash = {
                    navController.navigate(Routes.TRASH)
                }
            )
        }

        composable(
            route = "${Routes.EDIT}/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L

            EditNoteScreen(
                noteId = noteId,
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.TRASH) {
            TrashScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}