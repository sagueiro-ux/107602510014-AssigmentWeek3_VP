package com.seanagueiro.a107602510014_assigmentweek3vp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContent {
//            Soal4View()
//        }
        setContent {
            Bonus2View()
        }
    }
}