package com.app.examenmoviles.data.mapper

import com.app.examenmoviles.data.remote.dto.SudokuDto
import com.app.examenmoviles.domain.model.Sudoku

fun SudokuDto.toDomain(): Sudoku =
    Sudoku(
        puzzle =
            puzzle.map { row ->
                row.map { it ?: 0 } // null → 0 para representar celdas vacías
            },
        solution = solution as List<List<Int>>,
    )
