package com.seanagueiro.a107602510014_assigmentweek3vp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

// Enum
enum class ReactionState {
    INITIAL,
    WAITING,
    READY,
    TRIAL_RESULT,
    FAIL,
    FINAL
}
enum class ReactionCategory(val message: String, val bgColor: Color, val imageRes: Int) {
    EXCELLENT("DANG YOU ARE SO FAST BRO!", Color(0xFF00E676), R.drawable.hormatt),
    GOOD("YOUR REFLEX IS GOOD", Color(0xFF2196F3), R.drawable.sip),
    AVERAGE("MEH LIKE OTHER PERSON", Color(0xFFFF9800), R.drawable.mehh),
    SLOW("YOU LIKE A SNAIL BRO", Color(0xFFFF5722), R.drawable.elek)
}

fun getCategory(average: Int): ReactionCategory {
    return when {
        average < 180 -> ReactionCategory.EXCELLENT
        average < 280 -> ReactionCategory.GOOD
        average < 450 -> ReactionCategory.AVERAGE
        else -> ReactionCategory.SLOW
    }
}
data class ScreenText(
    val title: String,
    val icon: String,
    val line1: String,
    val line2: String
)

@Composable
fun Soal1View() {
    var state by remember { mutableStateOf(ReactionState.INITIAL) }
    val results = remember { mutableStateListOf<Int?>() }
    var startTime by remember { mutableStateOf(0L) }
    var lastTime by remember { mutableStateOf(0) }

    // Buat kasi efek delay
    LaunchedEffect(state) {
        if (state == ReactionState.WAITING) {
            delay(Random.nextLong(500, 4501))
            startTime = System.currentTimeMillis()
            state = ReactionState.READY
        }
    }

    // Buat rata-rata skornya nanti
    val validResults = results.filterNotNull()
    val average: Int? = if (validResults.isEmpty()) null else validResults.sum() / validResults.size

    fun onScreenClick() {
        when (state) {
            ReactionState.INITIAL -> state = ReactionState.WAITING

            // Kalo belum ijo sudah di klik fail
            ReactionState.WAITING -> {
                results.add(null)
                state = ReactionState.FAIL
            }

            //Kalo sudah hijau
            ReactionState.READY -> {
                val time = (System.currentTimeMillis() - startTime).toInt()
                lastTime = time
                results.add(time)
                state = if (results.size == 3) ReactionState.FINAL else ReactionState.TRIAL_RESULT
            }

            ReactionState.TRIAL_RESULT -> state = ReactionState.WAITING

            ReactionState.FAIL -> {
                state = if (results.size == 3) ReactionState.FINAL else ReactionState.INITIAL
            }

            ReactionState.FINAL -> {
                results.clear()
                state = ReactionState.INITIAL
            }
        }
    }

    val category = if (average == null) ReactionCategory.SLOW else getCategory(average)
    val bgColor = when (state) {
        ReactionState.INITIAL -> Color(0xFF6FCFDD)
        ReactionState.WAITING -> Color(0xFFD6D6D6)
        ReactionState.READY -> Color(0xFF4CAF50)
        ReactionState.TRIAL_RESULT -> Color(0xFF4CAF50)
        ReactionState.FAIL -> Color(0xFFFA4444)
        ReactionState.FINAL -> category.bgColor
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onScreenClick() }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state == ReactionState.FINAL) {
            Text(
                category.message,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(category.imageRes),
                contentDescription = category.message,
                modifier = Modifier.size(160.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text("Average: ${average ?: "-"}ms", color = Color.White, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("Click to Start New Test", color = Color.White, fontSize = 13.sp)
        } else {
            val text = when (state) {
                ReactionState.INITIAL -> ScreenText("Reaction", "⚡", "Test", "Click to Start")
                ReactionState.WAITING -> ScreenText("Get Ready", "⚠\uFE0F", "Wait for green light...", "DON'T CLICK YET!")
                ReactionState.READY -> ScreenText("GO!", "🏃", "CLICK NOW!", "TAP AS FAST AS YOU CAN!")
                ReactionState.TRIAL_RESULT -> ScreenText(
                    "Trial ${results.size} Complete!", "✅",
                    "Time: ${lastTime}ms", "Continue to Trial ${results.size + 1}"
                )
                else -> ScreenText(
                    "FAIL!", "👎",
                    "You clicked too early, TRY TO READ THE RULE BRO", "TRY AGAIN"
                )
            }
            Text(text.title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Text(text.icon, fontSize = 96.sp)
            Spacer(Modifier.height(24.dp))
            Text(text.line1, color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(text.line2, color = Color.White, fontSize = 13.sp, textAlign = TextAlign.Center)
        }

        val showCard = state != ReactionState.WAITING && state != ReactionState.READY && results.isNotEmpty()
        if (showCard) {
            Spacer(Modifier.height(24.dp))
            ResultCard(
                results = results,
                average = average,
                showAverage = state == ReactionState.FINAL
            )
        }
    }
}

@Composable
fun ResultCard(results: List<Int?>, average: Int?, showAverage: Boolean) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Trial Results", color = Color(0xFF1565C0), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                for (i in 0..2) {
                    val label = when {
                        i >= results.size -> "-"
                        results[i] == null -> "FAIL"
                        else -> "${results[i]}ms"
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${i + 1}", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(label, color = Color.Black, fontSize = 12.sp)
                    }
                }
            }

            if (showAverage) {
                Spacer(Modifier.height(12.dp))
                Text("Average Score", color = Color(0xFF1565C0), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("${average ?: "-"}ms", color = Color(0xFFE65100), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal1Preview() {
    Soal1View()
}