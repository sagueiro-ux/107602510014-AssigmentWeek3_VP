package com.seanagueiro.a107602510014_assigmentweek3vp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class ColorGameState { INITIAL, COUNTDOWN, RUNNING, GAME_OVER }
enum class ColorMode(val label: String, val clue: String) {
    COLOR("COLOR", "Pilih WARNA TINTA"),
    TEXT("TEXT", "Pilih NAMA KATA")
}
enum class ColorChoice { INK, WORD }
enum class ColorName(val label: String, val color: Color) {
    RED("RED", Color(0xFFF44336)),
    BLUE("BLUE", Color(0xFF2196F3)),
    GREEN("GREEN", Color(0xFF4CAF50)),
    ORANGE("ORANGE", Color(0xFFFF9800))
}

//Class
data class Question(
    val mode: ColorMode,
    val word: ColorName,
    val ink: ColorName,
    val inkOnLeft: Boolean
) {
    val correctChoice: ColorChoice
        get() = if (mode == ColorMode.COLOR) ColorChoice.INK else ColorChoice.WORD
    val leftChoice: ColorChoice
        get() = if (inkOnLeft) ColorChoice.INK else ColorChoice.WORD
    val rightChoice: ColorChoice
        get() = if (inkOnLeft) ColorChoice.WORD else ColorChoice.INK
    fun labelOf(choice: ColorChoice): String {
        return if (choice == ColorChoice.INK) ink.label else word.label
    }
}

// Membuat soal baru biar bisa acak
fun makeQuestion(): Question {
    val word = ColorName.values().random()
    // warna tinta dibuat berbeda dari kata supaya soal tidak ambigu
    val ink = ColorName.values().filter { it != word }.random()
    val mode = ColorMode.values().random()
    return Question(mode, word, ink, Random.nextBoolean())
}

const val TIME_LIMIT_MS = 5000
const val MAX_STRIKES = 3
@Composable
fun Soal3View(){
    // ---- STATE ----
    var gameState by remember { mutableStateOf(ColorGameState.INITIAL) }
    var score by remember { mutableStateOf(0) }
    var strikes by remember { mutableStateOf(0) }
    var bestScore by remember { mutableStateOf(0) }
    var countdownText by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(TIME_LIMIT_MS) }
    var question by remember { mutableStateOf(makeQuestion()) }
    var questionId by remember { mutableStateOf(0) }

    fun handleAnswer(isCorrect: Boolean) {
        if (isCorrect) {
            score += 1
        } else {
            strikes += 1
        }

        if (strikes >= MAX_STRIKES) {
            if (score > bestScore) bestScore = score
            gameState = ColorGameState.GAME_OVER
        } else {
            question = makeQuestion()
            timeLeft = TIME_LIMIT_MS
            questionId += 1
        }
    }

    //Countdown
    LaunchedEffect(gameState) {
        if (gameState == ColorGameState.COUNTDOWN) {
            for (n in 3 downTo 1) {
                countdownText = n.toString()
                delay(1000)
            }
            countdownText = "Start!"
            delay(700)

            question = makeQuestion()
            timeLeft = TIME_LIMIT_MS
            questionId += 1
            gameState = ColorGameState.RUNNING
        }
    }

    // Timer tiap soalnya
    LaunchedEffect(questionId, gameState) {
        if (gameState == ColorGameState.RUNNING) {
            timeLeft = TIME_LIMIT_MS
            while (timeLeft > 0) {
                delay(50)
                timeLeft -= 50
            }
            handleAnswer(false)
        }
    }

    //View
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (gameState) {
            ColorGameState.INITIAL -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome\nto\nColor Word Matching", fontSize = 40.sp, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(32.dp))
                    Button(onClick = {
                        score = 0
                        strikes = 0
                        gameState = ColorGameState.COUNTDOWN
                    }) {
                        Text("Start Game")
                    }
                }
            }

            ColorGameState.COUNTDOWN -> {
                Text(countdownText, fontSize = 50.sp)
            }

            ColorGameState.RUNNING -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Mode: ${question.mode.label}", fontSize = 20.sp)
                        Text("✅ $score   ❌ $strikes/$MAX_STRIKES", fontSize = 20.sp)
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(question.mode.clue, fontSize = 20.sp, color = Color.Gray)
                    Spacer(Modifier.height(24.dp))

                    Text("${(timeLeft + 999) / 1000} s", fontSize = 30.sp)
                    Spacer(Modifier.height(16.dp))

                    Text(question.word.label, color = question.ink.color, fontSize = 48.sp)
                    Spacer(Modifier.height(80.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            handleAnswer(question.leftChoice == question.correctChoice)
                        }) {
                            Text(question.labelOf(question.leftChoice))
                        }
                        Button(onClick = {
                            handleAnswer(question.rightChoice == question.correctChoice)
                        }) {
                            Text(question.labelOf(question.rightChoice))
                        }
                    }
                }
            }

            // Gameover
            ColorGameState.GAME_OVER -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Game Over!", fontSize = 40.sp)
                    Spacer(Modifier.height(24.dp))
                    Text("Your Score", fontSize = 25.sp)
                    Text("$score", fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Best Score", fontSize = 22.sp)
                    Text("$bestScore", fontSize = 20.sp)
                    Spacer(Modifier.height(24.dp))

                    Button(onClick = {
                        score = 0
                        strikes = 0
                        gameState = ColorGameState.COUNTDOWN
                    }) {
                        Text("Restart Game")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { gameState = ColorGameState.INITIAL }) {
                        Text("Exit")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal3Preview(){
    Soal3View()
}