package com.app.examenmoviles.domain.model

data class SudokuCache(
    val puzzle: List<List<Int>>,
    val solution: List<List<Int>>,
    val currentBoard: List<List<Int>>,
    val width: Int,
    val height: Int,
    val difficulty: String,
)
