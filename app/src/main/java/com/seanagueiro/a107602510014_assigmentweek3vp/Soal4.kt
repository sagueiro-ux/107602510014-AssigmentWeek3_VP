package com.seanagueiro.a107602510014_assigmentweek3vp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class Rps(val emoji: String, val label: String) {
    ROCK("✊", "Rock"),
    PAPER("✋", "Paper"),
    SCISSOR("✌️", "Scissor");
    fun beats(other: Rps): Boolean {
        return (this == ROCK && other == SCISSOR) ||
                (this == PAPER && other == ROCK) ||
                (this == SCISSOR && other == PAPER)
    }
}
enum class RpsState { INITIAL, PICK, REVEAL, FINISHED }
enum class RoundResult(val text: String) {
    WIN("You Win!"),
    LOSE("You Lose"),
    DRAW("Draw")
}
@Composable
fun Soal4View() {
    val bestOf = 5
    val target = bestOf / 2 + 1

    // ---- STATE ----
    // rememberSaveable = nilainya tetap ada walaupun layar diputar
    var state by rememberSaveable { mutableStateOf(RpsState.INITIAL) }
    var yourScore by rememberSaveable { mutableStateOf(0) }
    var cpuScore by rememberSaveable { mutableStateOf(0) }
    var bestScore by rememberSaveable { mutableStateOf<Int?>(null) }  // null = belum ada
    var playerPick by rememberSaveable { mutableStateOf<Rps?>(null) }
    var cpuPick by rememberSaveable { mutableStateOf<Rps?>(null) }
    var result by rememberSaveable { mutableStateOf<RoundResult?>(null) }

    // Urutan tombol (diacak tiap ronde)
    var options by remember { mutableStateOf(Rps.values().toList().shuffled()) }

    // ---- FUNGSI ----

    // Mulai / restart match
    fun startMatch() {
        yourScore = 0
        cpuScore = 0
        playerPick = null
        cpuPick = null
        result = null
        options = Rps.values().toList().shuffled()
        state = RpsState.PICK
    }

    // Exit -> kembali ke tampilan awal
    fun exitMatch() {
        yourScore = 0
        cpuScore = 0
        playerPick = null
        cpuPick = null
        result = null
        state = RpsState.INITIAL
    }

    // Pemain memilih
    fun pickMove(choice: Rps) {
        val cpu = Rps.values().random()   // komputer memilih acak

        val roundResult = if (choice == cpu) {
            RoundResult.DRAW
        } else if (choice.beats(cpu)) {
            RoundResult.WIN
        } else {
            RoundResult.LOSE
        }

        if (roundResult == RoundResult.WIN) yourScore += 1
        if (roundResult == RoundResult.LOSE) cpuScore += 1

        playerPick = choice
        cpuPick = cpu
        result = roundResult
        state = RpsState.REVEAL
    }

    // ---- REVEAL: tunggu 700 ms lalu lanjut otomatis ----
    LaunchedEffect(state) {
        if (state == RpsState.REVEAL) {
            delay(700)

            if (yourScore >= target || cpuScore >= target) {
                // Match selesai. Best score = selisih skor terbaik.
                val diff = yourScore - cpuScore
                val oldBest = bestScore
                if (oldBest == null || diff > oldBest) {
                    bestScore = diff
                }
                state = RpsState.FINISHED
            } else {
                // Ronde berikutnya (reset pilihan & acak tombol)
                playerPick = null
                cpuPick = null
                result = null
                options = Rps.values().toList().shuffled()
                state = RpsState.PICK
            }
        }
    }

    // ---- TAMPILAN ----
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Header skor (selalu tampil)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "🧑 $yourScore — $cpuScore 🤖",
                fontSize = 18.sp,
                modifier = Modifier.semantics {
                    contentDescription = "Your score $yourScore, computer score $cpuScore"
                }
            )
            Text("Best of $bestOf", fontSize = 18.sp)
        }

        Spacer(Modifier.height(80.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {

                // ===== Tampilan awal =====
                RpsState.INITIAL -> {
                    Text("Rock • Paper • Scissors", fontSize = 25.sp)
                    Spacer(Modifier.height(25.dp))
                    Button(
                        onClick = { startMatch() },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("Start")
                    }
                }

                // ===== Pemain memilih =====
                RpsState.PICK -> {
                    Text("Pick your move!", fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("❔ VS ❔", fontSize = 30.sp)
                    Spacer(Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // options sudah diacak, jadi urutan tombol berubah tiap ronde
                        for (rps in options) {
                            Button(
                                onClick = { pickMove(rps) },
                                modifier = Modifier.semantics { contentDescription = "Pick ${rps.label}" }
                            ) {
                                Text("${rps.emoji} ${rps.label}", fontSize = 15.sp)
                            }
                        }
                    }
                }

                // ===== Reveal =====
                RpsState.REVEAL -> {
                    Text(
                        "${playerPick?.emoji} VS ${cpuPick?.emoji}",
                        fontSize = 32.sp,
                        modifier = Modifier.semantics {
                            contentDescription =
                                "You chose ${playerPick?.label}, computer chose ${cpuPick?.label}"
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        result?.text ?: "",
                        fontSize = 16.sp,
                        modifier = Modifier.semantics {
                            contentDescription = "Round result: ${result?.text}"
                        }
                    )
                }

                // ===== Match selesai =====
                RpsState.FINISHED -> {
                    val youWon = yourScore > cpuScore
                    Text(
                        if (youWon) "You Win the Match!" else "You Lose the Match",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics {
                            contentDescription = if (youWon) "You won the match" else "You lost the match"
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Final: 🧑 $yourScore - $cpuScore 🤖", fontSize = 20.sp)
                    Text("Best Score: ${bestScore ?: "-"}", fontSize = 18.sp)
                    Spacer(Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(180.dp)) {
                        Button(
                            onClick = { startMatch() },
                            modifier = Modifier.size(width = 110.dp, height = 45.dp)
                        ) {
                            Text("Restart")
                        }
                        Button(
                            onClick = { exitMatch() },
                            modifier = Modifier.size(width = 80.dp, height = 45.dp)
                        ) {
                            Text("Exit")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal4Preview(){
    Soal4View()
}