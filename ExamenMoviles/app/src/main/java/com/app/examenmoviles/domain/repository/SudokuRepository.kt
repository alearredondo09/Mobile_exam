package com.app.examenmoviles.domain.repository

import com.app.examenmoviles.domain.model.Sudoku

interface SudokuRepository {
    suspend fun getSudoku(
        width: Int,
        height: Int,
        difficulty: String,
    ): Sudoku
}
