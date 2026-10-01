package com.seanagueiro.a107602510014_assigmentweek3vp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val JournalBg = Color(0xFF0F1526)
private val JournalCard = Color(0xFF1B2438)
private val JournalBorder = Color(0xFF2C3A55)
private val JournalAccent = Color(0xFF5AB4F5)
private val JournalText = Color(0xFFF1F5F9)
private val JournalMuted = Color(0xFF94A3B8)
private val JournalTopOffset = 20.dp

enum class Satisfaction(val label: String) {
    BIASA("Biasa"),
    SERU("Seru"),
    LUAR_BIASA("Luar Biasa ★")
}
@Composable
fun Bonus2View() {
    var spot by remember { mutableStateOf("Fjellheisen Cable Car") }
    var experience by remember {
        mutableStateOf("Pemandangan langit malam hijau aurora spektakuler dari puncak gunung...")
    }
    var notes by remember { mutableStateOf("") }
    var satisfaction by remember { mutableStateOf(Satisfaction.LUAR_BIASA) }
    var status by remember { mutableStateOf("Draft Tersimpan") }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JournalBg)
    ) {
        // Isi Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .padding(top = JournalTopOffset)
                .padding(bottom = 100.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1D3A7A), Color(0xFF3B76D6))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Text(
                    "LOG PERJALANAN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFCFE3FF)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Tromsø, Norway ❄️",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Ekspedisi Aurora Borealis",
                    fontSize = 15.sp,
                    color = Color(0xFFE2EDFF)
                )
            }

            Spacer(Modifier.height(20.dp))

            // TextField 1
            JournalField(
                label = "SPOT WISATA FAVORIT",
                value = spot,
                onValueChange = { spot = it },
                singleLine = true,
                minLines = 1
            )

            Spacer(Modifier.height(16.dp))

            // TextField 2
            JournalField(
                label = "APA YANG PALING KAMU NIKMATI?",
                value = experience,
                onValueChange = { experience = it },
                singleLine = false,
                minLines = 3
            )

            Spacer(Modifier.height(16.dp))

            // TextField 3
            JournalField(
                label = "CATATAN TAMBAHAN / PERLENGKAPAN",
                value = notes,
                onValueChange = { notes = it },
                singleLine = false,
                minLines = 2,
                placeholderText = "(Ketik perlengkapan ekstra di sini...)"
            )

            Spacer(Modifier.height(24.dp))
            // Kepuasan
            Text(
                "TINGKAT KEPUASAN:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = JournalMuted
            )
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                for (item in Satisfaction.values()) {
                    val isSelected = satisfaction == item
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) JournalAccent else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) JournalAccent else JournalBorder,
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { satisfaction = item }
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            item.label,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) JournalBg else JournalText
                        )
                    }
                }
            }
        }

     // Bagian Status
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 34.dp)
        ) {
            Text("Status: ", fontSize = 15.sp, color = JournalMuted)
            Text(
                status,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = JournalAccent
            )
        }

        FloatingActionButton(
            onClick = {
                if (spot.isBlank()) {
                    Toast.makeText(
                        context,
                        "Isi dulu nama spot wisata favoritmu!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Jurnal tersimpan! Spot: $spot (${satisfaction.label})",
                        Toast.LENGTH_SHORT
                    ).show()
                    status = "Jurnal Tersimpan ✓"
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)
                .size(64.dp),
            shape = RoundedCornerShape(18.dp),
            containerColor = JournalAccent,
            contentColor = JournalBg
        ) {
            Text("+", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}
@Composable
private fun JournalField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    minLines: Int,
    placeholderText: String = ""
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(JournalCard)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) JournalAccent else JournalBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(top = 14.dp, bottom = 4.dp)
    ) {
        Text(
            label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isFocused) JournalAccent else JournalMuted,
            modifier = Modifier.padding(horizontal = 14.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            minLines = minLines,
            textStyle = TextStyle(fontSize = 18.sp),
            placeholder = {
                if (placeholderText.isNotEmpty()) {
                    Text(placeholderText, fontSize = 16.sp)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = JournalText,
                unfocusedTextColor = JournalText,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = JournalMuted,
                unfocusedPlaceholderColor = JournalMuted,
                cursorColor = JournalAccent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Bonus2Preview() {
    Bonus2View()
}