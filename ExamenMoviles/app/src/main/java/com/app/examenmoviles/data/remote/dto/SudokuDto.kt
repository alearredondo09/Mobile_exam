package com.app.examenmoviles.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SudokuDto(
    @SerializedName("puzzle") val puzzle: List<List<Int?>>,
    @SerializedName("solution") val solution: List<List<Int>>,
)
