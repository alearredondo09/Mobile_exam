package com.app.examenmoviles.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examenmoviles.domain.common.Result
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
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        private val _boardState = MutableStateFlow<List<MutableList<Int>>>(emptyList())
        val boardState = _boardState.asStateFlow()

        init {
            // loadHoroscope("cancer")
            loadSudoku(2, 2, "easy")
        }

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

                            is Result.Error,
                            ->
                                state.copy(
                                    error = result.exception.message,
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
            loadSudoku(width, height, difficulty)
        }

        fun updateCell(
            row: Int,
            col: Int,
            value: Int,
        ) {
            val copy = _boardState.value.map { it.toMutableList() }.toMutableList()
            copy[row][col] = value
            _boardState.value = copy
        }
    }
