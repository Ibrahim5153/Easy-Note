package com.aqube.notes.feature_note.presentation.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aqube.notes.core.R
import com.aqube.notes.core.navigation.MainActions
import com.aqube.notes.core.presentation.components.TextInputField
import com.aqube.notes.core.presentation.theme.White
import com.aqube.notes.core.presentation.theme.Yellow600
import com.aqube.notes.feature_note.presentation.notes.components.NoteItem
import com.aqube.notes.feature_note.presentation.notes.components.OrderSection
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun NotesScreen(
    actions: MainActions,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val noteSearchState = viewModel.search.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    actions.gotoNoteAddEditNote(-1, -1)
                },
                backgroundColor = Yellow600
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_new_note),
                    tint = White
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.my_notes),
                    style = MaterialTheme.typography.h1,
                )
                IconButton(
                    onClick = { actions.gotoSettings() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(id = R.string.settings)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextInputField(
                    label = noteSearchState.hint,
                    value = noteSearchState.text,
                    widthPercentage = 0.85f,
                    onValueChanged = { viewModel.onEvent(NotesEvent.SearchNotes(it)) }
                )
                IconButton(
                    onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSelection) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        modifier = Modifier.size(26.dp),
                        contentDescription = stringResource(id = R.string.sort)
                    )
                }
            }

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { -50 }, animationSpec =
                    tween(
                        durationMillis = 300,
                    )

                ),
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                actions.gotoNoteAddEditNote(note.id!!, note.color)
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo",
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}