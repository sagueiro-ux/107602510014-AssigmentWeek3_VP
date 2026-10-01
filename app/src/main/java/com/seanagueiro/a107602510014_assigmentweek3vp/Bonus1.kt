package com.seanagueiro.a107602510014_assigmentweek3vp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

const val COFFEE_PRICE = 25000
const val TAX_PERCENT = 10

// Colour
private val CoffeeBg = Color(0xFFF9F4F1)
private val CoffeeDark = Color(0xFF4A1F0E)
private val CoffeeOrange = Color(0xFFD9622B)
private val CoffeeBorder = Color(0xFFF5D9BC)
private val CoffeeSoft = Color(0xFFFCE6D0)

// Pilihan ukuran
enum class CoffeeSize(val label: String, val extraPrice: Int) {
    REGULAR("Regular (+0)", 0),
    LARGE("Large (+6k)", 6000)
}
data class CoffeeOrder(
    val quantity: Int = 1,
    val size: CoffeeSize = CoffeeSize.REGULAR
) {
    val unitPrice: Int
        get() = COFFEE_PRICE + size.extraPrice
    val subtotal: Int
        get() = unitPrice * quantity
    val tax: Int
        get() = subtotal * TAX_PERCENT / 100
    val total: Int
        get() = subtotal + tax
    fun increase(): CoffeeOrder {
        return copy(quantity = quantity + 1)
    }
    fun decrease(): CoffeeOrder {
        if (quantity <= 1) return this
        return copy(quantity = quantity - 1)
    }
    fun changeSize(newSize: CoffeeSize): CoffeeOrder {
        return copy(size = newSize)
    }
}

// Format penulisan harga biar Rp didepan
fun formatRupiah(amount: Int): String {
    val text = String.format(Locale.US, "%,d", amount).replace(",", ".")
    return "Rp $text"
}
@Composable
fun Bonus1View() {
    var order by remember { mutableStateOf(CoffeeOrder()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoffeeBg)
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Kopi Kenangan Senja",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CoffeeDark
            )
            Text("☕\uFE0E", fontSize = 30.sp)
        }

        Spacer(Modifier.height(16.dp))

        //Isi
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CoffeeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF6D3319), Color(0xFF8E3B1B))
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("☕", fontSize = 80.sp)
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Caramel Latte",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoffeeDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Espresso shot, steamed milk & caramel syrup",
                        fontSize = 15.sp,
                        color = Color(0xFF8A4B2D),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${formatRupiah(COFFEE_PRICE)} / cup",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoffeeOrange
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "PILIHAN UKURAN:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A2F0E)
            )
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (size in CoffeeSize.values()) {
                    val isSelected = order.size == size
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) Color.White else CoffeeSoft)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) CoffeeOrange else CoffeeBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { order = order.changeSize(size) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            size.label,
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) CoffeeOrange else CoffeeDark
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Jumlah Pesanan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CoffeeBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Jumlah Pesanan:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoffeeDark
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        QuantityButton(
                            symbol = "-",
                            enabled = order.quantity > 1,
                            filled = false,
                            onClick = { order = order.decrease() }
                        )
                        Text(
                            "${order.quantity}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoffeeDark,
                            modifier = Modifier.width(48.dp),
                            textAlign = TextAlign.Center
                        )

                        QuantityButton(
                            symbol = "+",
                            enabled = true,
                            filled = true,
                            onClick = { order = order.increase() }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Total harga
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFBF3E8))
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFE0B48A),
                            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 14f), 0f)
                            )
                        )
                    }
                    .padding(16.dp)
            ) {
                SummaryRow("Subtotal:", formatRupiah(order.subtotal), bold = false)
                Spacer(Modifier.height(6.dp))
                SummaryRow("Pajak Resto ($TAX_PERCENT%):", formatRupiah(order.tax), bold = false)
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0B48A))
                )
                Spacer(Modifier.height(10.dp))
                SummaryRow("Total Tagihan:", formatRupiah(order.total), bold = true)
            }

            Spacer(Modifier.height(16.dp))
        }

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Ditambahkan ke keranjang: ${order.quantity}x Caramel Latte " +
                            "(${order.size.label}) - Total ${formatRupiah(order.total)}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CoffeeOrange,
                contentColor = Color.White
            )
        ) {
            Text("TAMBAH KE KERANJANG", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Tombol + dan -
@Composable
private fun QuantityButton(
    symbol: String,
    enabled: Boolean,
    filled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (filled) CoffeeSoft else Color.White)
            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(14.dp))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            symbol,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) CoffeeOrange else Color.LightGray
        )
    }
}
@Composable
private fun SummaryRow(label: String, value: String, bold: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = if (bold) 22.sp else 16.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = CoffeeDark
        )

        Text(
            value,
            fontSize = if (bold) 22.sp else 16.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (bold) CoffeeOrange else CoffeeDark
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Bonus1Preview(){
    Bonus1View()
}