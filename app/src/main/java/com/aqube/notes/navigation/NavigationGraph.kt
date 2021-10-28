package com.aqube.notes.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.aqube.notes.core.navigation.MainActions
import com.aqube.notes.core.navigation.Screen
import com.aqube.notes.feature_note.presentation.add_edit_notes.components.AddEditNoteScreen
import com.aqube.notes.feature_note.presentation.notes.NotesScreen
import com.aqube.notes.feature_settings.presentation.SettingsScreen
import com.aqube.notes.navigation.NoteParams.NOTE_COLOR
import com.aqube.notes.navigation.NoteParams.NOTE_ID
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun NavigationGraph(isDarkTheme: Boolean, toggleTheme: (Boolean) -> Unit) {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { MainActions(navController) }

    AnimatedNavHost(navController, startDestination = Screen.NotesScreen.route) {
        // Notes List
        composable(Screen.NotesScreen.route) {
            NotesScreen(actions)
        }

        // Note Add Edit
        composable("${Screen.AddEditNoteScreen.route}/{noteId}/{noteColor}",
            arguments = listOf(
                navArgument(NOTE_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(NOTE_COLOR) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val color = it.arguments?.getInt(NOTE_COLOR) ?: -1
            AddEditNoteScreen(actions.upPress, noteColor = color)
        }

        // Settings
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(
                darkTheme = isDarkTheme,
                upPress = actions.upPress,
                onToggleTheme = { appTheme ->
                    toggleTheme(appTheme)
                }
            )
        }

    }
}

object NoteParams {
    const val NOTE_ID = "noteId"
    const val NOTE_COLOR = "noteColor"
}



