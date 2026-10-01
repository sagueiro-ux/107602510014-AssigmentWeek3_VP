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

// data class = class untuk menyimpan data.
// Semua aturan game (tap & upgrade) ada di sini, jadi tampilan tinggal memanggilnya.
data class CatState(
    val coins: Int = 0,          // jumlah coin
    val valuePerTap: Int = 1,    // coin per tekan (awal 1)
    val upgradeCost: Int = 10    // harga upgrade (awal 10)
) {
    // value setelah upgrade = value sekarang x 1.5 (dibulatkan)
    val nextValue: Int
        get() = (valuePerTap * 1.5).roundToInt()

    // true kalau coin cukup untuk upgrade
    val canUpgrade: Boolean
        get() = coins >= upgradeCost

    // kekurangan coin untuk upgrade
    val missingCoins: Int
        get() = upgradeCost - coins

    // Kucing ditekan -> coin bertambah sebesar valuePerTap.
    // copy() = membuat salinan data dengan sedikit perubahan.
    fun tap(): CatState {
        return copy(coins = coins + valuePerTap)
    }

    // Upgrade -> bayar coin, value x1.5, harga x2
    fun upgrade(): CatState {
        if (!canUpgrade) return this   // coin kurang, tidak terjadi apa-apa
        return copy(
            coins = coins - upgradeCost,
            valuePerTap = nextValue,
            upgradeCost = upgradeCost * 2
        )
    }
}

/* =====================================================
   2. TAMPILAN
   ===================================================== */

// Gambar di res/drawable dengan nama:
// cat_background, cat_closed (mulut tertutup), cat_open (mulut terbuka)

@Composable   // <-- @Composable HARUS tepat di atas "fun"
fun Soal2View() {
    var game by remember { mutableStateOf(CatState()) }
    var tapCount by remember { mutableStateOf(0) }        // dihitung tiap kucing ditekan
    var recentTap by remember { mutableStateOf(false) }   // true sesaat setelah ditekan

    // Untuk mengetahui apakah kucing sedang ditahan jari
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Setiap tap, blok ini dijalankan ulang dari awal.
    // Jadi kalau di-spam, recentTap tidak sempat jadi false -> mulut tetap terbuka.
    LaunchedEffect(tapCount) {
        if (tapCount > 0) {
            recentTap = true
            delay(150)
            recentTap = false
        }
    }

    // Mulut terbuka kalau sedang ditahan ATAU baru saja ditekan
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
            // ---------- BAGIAN ATAS: coin ----------
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

            // ---------- BAGIAN TENGAH: kucing ----------
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
                            game = game.tap()   // tambah coin
                            tapCount++          // memicu LaunchedEffect di atas
                        }
                )

                Spacer(Modifier.height(8.dp))
                Text(if (mouthOpen) "Meow!" else "Purr~", color = Color.White, fontSize = 16.sp)
            }

            // ---------- BAGIAN BAWAH: upgrade ----------
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
                        enabled = game.canUpgrade,   // tidak bisa ditekan kalau coin kurang
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