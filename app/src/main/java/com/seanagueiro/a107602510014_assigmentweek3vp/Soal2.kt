package com.seanagueiro.a107602510014_assigmentweek3vp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

data class CatState(
    val coins: Int = 0,
    val valuePerTap: Int = 1,
    val upgradeCost: Int = 10
) {
    val nextValue: Int
        get() = (valuePerTap * 1.5).roundToInt()
    val canUpgrade: Boolean
        get() = coins >= upgradeCost
    val missingCoins: Int
        get() = upgradeCost - coins
    fun tap(): CatState {
        return copy(coins = coins + valuePerTap)
    }
    fun upgrade(): CatState {
        if (!canUpgrade) return this
        return copy(
            coins = coins - upgradeCost,
            valuePerTap = nextValue,
            upgradeCost = upgradeCost * 2
        )
    }
}
@Composable
fun Soal2View() {
    var game by remember { mutableStateOf(CatState()) }
    var tapCount by remember { mutableStateOf(0) }
    var recentTap by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(tapCount) {
        if (tapCount > 0) {
            recentTap = true
            delay(150)
            recentTap = false
        }
    }
    val mouthOpen = isPressed || recentTap

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(R.drawable.cat_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Your Coins",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${game.coins}",
                        color = Color(0xFF00E676),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${game.valuePerTap} coins per tap", color = Color.White, fontSize = 13.sp)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Tap the Cat!",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                Image(
                    painter = painterResource(
                        if (mouthOpen) R.drawable.cat_open else R.drawable.cat_closed
                    ),
                    contentDescription = "Cat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            game = game.tap()
                            tapCount++
                        }
                )

                Spacer(Modifier.height(8.dp))
                Text(if (mouthOpen) "Meow!" else "Purr~", color = Color.White, fontSize = 16.sp)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Give Me Your Coin",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Next upgrade: +${game.nextValue} coins per tap",
                        fontSize = 13.sp,
                        color = Color(0xFF616161)
                    )
                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { game = game.upgrade() },
                        enabled = game.canUpgrade,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFBDBDBD),
                            disabledContentColor = Color.White
                        )
                    ) {
                        if (game.canUpgrade) {
                            Text("Pay for ${game.upgradeCost} coins", fontWeight = FontWeight.Bold)
                        } else {
                            Text(
                                "Find ${game.missingCoins} more coins",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal2Preview() {
    Soal2View()
}