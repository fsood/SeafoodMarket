package com.example.test.dashboardbuyer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.test.R

class FindSeaFood : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_sea_food)

        val backButton = findViewById<ImageButton>(R.id.backB)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val marketCardView = findViewById<CardView>(R.id.from_market)
        marketCardView.setOnClickListener {
            val intent = Intent(this, FindMarketActivity::class.java)
            startActivity(intent)
        }

        val boatCardView = findViewById<CardView>(R.id.from_boat)
        boatCardView.setOnClickListener {
            val intent = Intent(this, FindBoatActivity::class.java)
            startActivity(intent)
        }
    }
}
