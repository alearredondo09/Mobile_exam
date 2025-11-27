package com.app.examenmoviles.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examenmoviles.data.local.preferences.SudokuPreferences
import com.app.examenmoviles.domain.common.Result
import com.app.examenmoviles.domain.model.Sudoku
import com.app.examenmoviles.domain.model.SudokuCache
import com.app.examenmoviles.domain.usecase.GetSudokuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getSudokuUseCase: GetSudokuUseCase,
        private val sudokuPrefs: SudokuPreferences,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        private val _boardState = MutableStateFlow<List<MutableList<Int>>>(emptyList())
        val boardState = _boardState.asStateFlow()
        private val _currentDifficulty = MutableStateFlow("easy")
        val currentDifficulty = _currentDifficulty.asStateFlow()

        init {
            val saved = loadSavedGame()

            if (saved != null) {
                // recuperar la partida en progreso
                _boardState.value = saved.currentBoard.map { it.toMutableList() }
                _uiState.value =
                    HomeUiState(
                        sudoku =
                            Sudoku(
                                puzzle = saved.puzzle,
                                solution = saved.solution,
                            ),
                    )
            } else {
                // no hay partida guardada → generar nuevo sudoku
                loadSudoku(2, 2, "easy")
            }
        }

        fun saveGame(
            puzzle: List<List<Int>>,
            board: List<List<Int>>,
            solution: List<List<Int>>,
            width: Int,
            height: Int,
            difficulty: String,
        ) {
            sudokuPrefs.saveSudoku(
                SudokuCache(
                    puzzle = puzzle,
                    solution = solution,
                    currentBoard = board,
                    width = width,
                    height = height,
                    difficulty = difficulty,
                ),
            )
        }

        fun loadSavedGame(): SudokuCache? = sudokuPrefs.getSudokuCache()

        fun clearSaved() = sudokuPrefs.clearSudoku()

        private fun loadSudoku(
            width: Int,
            height: Int,
            difficulty: String,
        ) {
            viewModelScope.launch {
                getSudokuUseCase(width, height, difficulty).collect { result ->
                    println("Entro al get sudoku use case en el view model")
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )

                            is Result.Success -> {
                                val puzzle =
                                    result.data!!
                                        .puzzle
                                        .map { it.toMutableList() }
                                        .toMutableList()
                                _boardState.value = puzzle
                                state.copy(
                                    sudoku = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            }

                            is Result.Error ->
                                state.copy(
                                    error = "No se pudo generar un nuevo Sudoku. Verifica tu conexión e inténtalo de nuevo.",
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }

        fun newSudoku(
            width: Int,
            height: Int,
            difficulty: String,
        ) {
            _currentDifficulty.value = difficulty
            loadSudoku(width, height, difficulty)
        }

        fun updateCell(
            row: Int,
            col: Int,
            value: Int,
        ) {
            val updated = _boardState.value.map { it.toMutableList() }.toMutableList()
            updated[row][col] = value
            _boardState.value = updated

            // Guardar automáticamente el progreso
            uiState.value.sudoku?.let { sudoku ->
                saveGame(
                    puzzle = sudoku.puzzle,
                    board = updated,
                    solution = sudoku.solution,
                    width = sudoku.puzzle.size,
                    height = sudoku.puzzle.size,
                    difficulty = currentDifficulty.value,
                )
            }
        }

        fun clearError() {
            _uiState.update { state ->
                state.copy(error = null)
            }
        }

        fun resetBoardToOriginal() {
            val original = uiState.value.sudoku?.puzzle ?: return

            val reset = original.map { it.toMutableList() }.toMutableList()

            // Actualizamos el estado observable
            _boardState.value = reset

            // También guardamos el progreso reseteado
            uiState.value.sudoku?.let { sudoku ->
                saveGame(
                    puzzle = sudoku.puzzle,
                    board = reset,
                    solution = sudoku.solution,
                    width = sudoku.puzzle.size,
                    height = sudoku.puzzle.size,
                    difficulty = currentDifficulty.value,
                )
            }
        }
    }
