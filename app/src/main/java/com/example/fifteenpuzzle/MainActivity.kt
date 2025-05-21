package com.example.fifteenpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.random.Random
import androidx.compose.animation.core.animateDpAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the UI using Compose
        setContent {
            PuzzleGame()
        }
    }
}

@Composable
fun PuzzleGame() {
    // Game state
    var board by remember { mutableStateOf(generateSolvableBoard()) }
    var moves by remember { mutableStateOf(0) }
    var isWin by remember { mutableStateOf(false) }
    var secondsElapsed by remember { mutableStateOf(0) }

    val tileSize = 64.dp

    // Simple game timer - runs until the player wins
    LaunchedEffect(key1 = isWin) {
        while (isActive && !isWin) {
            delay(1000)
            secondsElapsed++
        }
    }

    // Format timer as MM:SS
    val formattedTime = remember(secondsElapsed) {
        val minutes = secondsElapsed / 60
        val seconds = secondsElapsed % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Moves: $moves", fontSize = 20.sp)
        Text("Time: $formattedTime", fontSize = 20.sp)

        if (isWin) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("🎉 You won!", fontSize = 20.sp, color = Color.Green)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game board 4x4 with animated tiles
        Box(modifier = Modifier.size(tileSize * 4)) {
            board.forEachIndexed { index, value ->
                if (value != 0) {
                    val targetRow = index / 4
                    val targetCol = index % 4

                    // Animate tile position
                    val offsetX by animateDpAsState(
                        targetValue = tileSize * targetCol,
                        animationSpec = tween(durationMillis = 150),
                        label = "offsetX"
                    )
                    val offsetY by animateDpAsState(
                        targetValue = tileSize * targetRow,
                        animationSpec = tween(durationMillis = 150),
                        label = "offsetY"
                    )

                    // Tile UI
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = offsetX, y = offsetY)
                            .size(tileSize)
                            .background(Color.LightGray)
                            .clickable {
                                val emptyIndex = board.indexOf(0)
                                if (isAdjacent(emptyIndex, index)) {
                                    val newBoard = board.toMutableList()
                                    newBoard[emptyIndex] = value
                                    newBoard[index] = 0
                                    board = newBoard
                                    moves++
                                    if (isWinningBoard(newBoard)) isWin = true
                                }
                            }
                    ) {
                        Text("$value", fontSize = 20.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset the game
        Button(onClick = {
            board = generateSolvableBoard()
            moves = 0
            isWin = false
            secondsElapsed = 0
        }) {
            Text("New Game")
        }

        Button(onClick = {
            board = generateAlmostWinningBoard()
            moves = 0
            isWin = false
            secondsElapsed = 0
        }) {
            Text("Almost Win")
        }
    }
}

// Check if two tiles are adjacent (can be swapped)
fun isAdjacent(index1: Int, index2: Int): Boolean {
    val row1 = index1 / 4
    val col1 = index1 % 4
    val row2 = index2 / 4
    val col2 = index2 % 4
    return (row1 == row2 && abs(col1 - col2) == 1) ||
            (col1 == col2 && abs(row1 - row2) == 1)
}

// Generate a solvable shuffled board
fun generateSolvableBoard(): List<Int> {
    var board: List<Int>
    do {
        board = (0..15).shuffled(Random(System.currentTimeMillis()))
    } while (!isSolvable(board))
    return board
}

// Generate almost finished board for testing victory
fun generateAlmostWinningBoard(): List<Int> {
    return listOf(
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 0, 15
    )
}

// Check if the current board is solvable
fun isSolvable(board: List<Int>): Boolean {
    val inversions = board.filter { it != 0 }.flatMapIndexed { i, v ->
        board.drop(i + 1).filter { it != 0 && it < v }
    }.count()
    val emptyRow = 4 - board.indexOf(0) / 4
    return (inversions + emptyRow) % 2 == 0
}

// Check if the board is in winning order
fun isWinningBoard(board: List<Int>): Boolean {
    return board.dropLast(1) == (1..15).toList() && board.last() == 0
}
