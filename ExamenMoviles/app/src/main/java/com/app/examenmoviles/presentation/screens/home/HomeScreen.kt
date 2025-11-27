package com.app.examenmoviles.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.examenmoviles.domain.model.Sudoku

@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
) {
    println("Entro al Home Screen")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> {
            println("Entro al loading")
            LoadingView()
        }
        uiState.error != null -> {
            println("Entro al error")
            ErrorView(uiState.error!!)
        }
        uiState.sudoku != null -> {
            println("Entro al horoscope")
            SudokuView(uiState.sudoku!!, onButtonClick, viewModel)
        }
        else -> {
            println("Entro al emptyview")
            EmptyView()
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoadingView() {
    androidx.compose.material3.Text("Cargando sudoku...")
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ErrorView(message: String) {
    androidx.compose.material3.Text("Error: $message")
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SudokuView(
    sudoku: Sudoku,
    onButtonClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val board by viewModel.boardState.collectAsStateWithLifecycle()

    var showOptionsDialog by remember { mutableStateOf(false) }
    var showVerifyDialog by remember { mutableStateOf(false) }
    var verifyMessage by remember { mutableStateOf("") }
    var verifyColor by remember { mutableStateOf(Color.White) }

    if (showOptionsDialog) {
        SudokuOptionsDialog(
            onOptionSelected = { width, height, difficulty ->
                viewModel.newSudoku(width, height, difficulty)
                showOptionsDialog = false
            },
            onDismiss = { showOptionsDialog = false },
        )
    }

    if (showVerifyDialog) {
        VerifyDialog(
            message = verifyMessage,
            color = verifyColor,
            onDismiss = { showVerifyDialog = false },
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sudoku ${board.size} x ${board.size}",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier =
                Modifier
                    .heightIn(max = 500.dp),
        ) {
            SudokuGridInteractive(
                board = board,
                originalPuzzle = sudoku.puzzle,
                onCellValueChange = { row, col, value ->
                    viewModel.updateCell(row, col, value)
                },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(onClick = { resetBoard(board, sudoku.puzzle) }) {
                Text("Reiniciar")
            }

            Button(onClick = { showOptionsDialog = true }) {
                Text("Nuevo Sudoku")
            }
        }
        Button(onClick = {
            val correct = verifySolution(board, sudoku.solution)

            if (correct) {
                verifyMessage = "¡Correcto! Sudoku completado"
                verifyColor = Color(0xFF22C55E) // verde
            } else {
                verifyMessage = "La solución tiene errores. Revisa nuevamente."
                verifyColor = Color(0xFFEF4444) // rojo
            }

            showVerifyDialog = true // mostrar dialog
        }) {
            Text("Verificar solución")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun EmptyView() {
    androidx.compose.material3.Text("Sin datos aún...")
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SudokuGrid(puzzle: List<List<Int>>) {
    val size = puzzle.size // 4 o 9
    val cellSize = if (size == 4) 60.dp else 40.dp

    Column {
        puzzle.forEach { row ->
            Row {
                row.forEach { cell ->
                    Box(
                        modifier =
                            Modifier
                                .size(cellSize)
                                .border(1.dp, MaterialTheme.colorScheme.primary)
                                .padding(4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (cell == 0) "" else cell.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SudokuGridInteractive(
    board: List<MutableList<Int>>,
    originalPuzzle: List<List<Int>>,
    onCellValueChange: (row: Int, col: Int, value: Int) -> Unit,
) {
    val size = board.size
    val columns = size

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        items(size * size) { index ->
            val row = index / size
            val col = index % size

            val value = board[row][col]
            val isFixed = originalPuzzle[row][col] != 0

            SudokuCell(
                value = value,
                editable = !isFixed,
                maxNumber = size,
                onValueSelected = { num ->
                    onCellValueChange(row, col, num)
                },
            )
        }
    }
}

@Composable
fun SudokuCell(
    value: Int,
    editable: Boolean,
    maxNumber: Int,
    onValueSelected: (Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .padding(2.dp)
                .aspectRatio(1f)
                .background(
                    if (editable) Color(0xFF1E293B) else Color(0xFF8B5CF6),
                ).border(
                    width = 1.dp,
                    color = if (editable) Color(0xFF8B5CF6) else Color(0xFF6D28D9),
                ).clickable(enabled = editable) {
                    showDialog = true
                },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (value == 0) "" else value.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (showDialog) {
        NumberPickerDialog(
            maxNumber = maxNumber,
            onSelect = {
                onValueSelected(it)
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
fun NumberPickerDialog(
    maxNumber: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.DarkGray,
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Selecciona un número", color = Color.White)

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(maxNumber) { n ->
                        Box(
                            modifier =
                                Modifier
                                    .padding(6.dp)
                                    .size(50.dp)
                                    .background(Color(0xFF4CAF50))
                                    .clickable { onSelect(n + 1) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "${n + 1}",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun resetBoard(
    board: List<MutableList<Int>>,
    original: List<List<Int>>,
) {
    board.forEachIndexed { row, rowList ->
        rowList.forEachIndexed { col, _ ->
            if (original[row][col] == 0) {
                rowList[col] = 0
            }
        }
    }
}

@Composable
fun SudokuOptionsDialog(
    onOptionSelected: (Int, Int, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var size by remember { mutableStateOf(4) }
    var difficulty by remember { mutableStateOf("easy") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.DarkGray,
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Nuevo Sudoku", color = Color.White, style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(16.dp))

                //  TAMAÑO
                Text("Tamaño del tablero", color = Color.White)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // --- BOTÓN 4x4 ---
                    Button(
                        colors =
                            androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (size == 4) Color(0xFF6366F1) else Color(0xFF374151),
                                contentColor = Color.White,
                            ),
                        onClick = { size = 4 },
                    ) {
                        Text("4x4")
                    }

                    // --- BOTÓN 9x9 ---
                    Button(
                        colors =
                            androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (size == 9) Color(0xFF6366F1) else Color(0xFF374151),
                                contentColor = Color.White,
                            ),
                        onClick = { size = 9 },
                    ) {
                        Text("9x9")
                    }
                }

                Spacer(Modifier.height(16.dp))

                //  DIFICULTAD
                Text("Dificultad", color = Color.White)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        colors =
                            androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (difficulty == "easy") Color(0xFF22C55E) else Color(0xFF374151),
                                contentColor = Color.White,
                            ),
                        onClick = { difficulty = "easy" },
                    ) { Text("Easy") }

                    Button(
                        colors =
                            androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (difficulty == "medium") Color(0xFFFACC15) else Color(0xFF374151),
                                contentColor = Color.White,
                            ),
                        onClick = { difficulty = "medium" },
                    ) { Text("Medium") }

                    Button(
                        colors =
                            androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (difficulty == "hard") Color(0xFFEF4444) else Color(0xFF374151),
                                contentColor = Color.White,
                            ),
                        onClick = { difficulty = "hard" },
                    ) { Text("Hard") }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Salir
                    Button(
                        onClick = onDismiss,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                            ),
                    ) {
                        Text("Salir")
                    }

                    //  CONFIRMAR
                    Button(onClick = {
                        val (w, h) = if (size == 4) (2 to 2) else (3 to 3)
                        onOptionSelected(w, h, difficulty)
                    }) {
                        Text("Generar")
                    }
                }
            }
        }
    }
}

fun verifySolution(
    board: List<List<Int>>,
    solution: List<List<Int>>,
): Boolean {
    for (row in board.indices) {
        for (col in board[row].indices) {
            if (board[row][col] != solution[row][col]) {
                return false
            }
        }
    }
    return true
}

@Composable
fun VerifyDialog(
    message: String,
    color: Color,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = Color(0xFF1E293B), // azul gris elegante
            tonalElevation = 6.dp,
            shadowElevation = 12.dp,
            modifier = Modifier.padding(24.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Círculo con color (verde/rojo)
                Box(
                    modifier =
                        Modifier
                            .size(70.dp)
                            .background(color.copy(alpha = 0.2f), shape = MaterialTheme.shapes.extraLarge),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (color == Color(0xFF22C55E)) "✓" else "✕",
                        color = color,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Mensaje
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón elegante
                Button(
                    onClick = onDismiss,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6), // azul bonito
                            contentColor = Color.White,
                        ),
                    modifier = Modifier.fillMaxWidth(0.6f),
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}
