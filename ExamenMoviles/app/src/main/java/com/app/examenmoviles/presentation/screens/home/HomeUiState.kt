package com.app.examenmoviles.presentation.screens.home

import com.app.examenmoviles.domain.model.Horoscope
import com.app.examenmoviles.domain.model.Sudoku

data class HomeUiState(
    val horoscope: Horoscope? = null,
    val sudoku: Sudoku? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
