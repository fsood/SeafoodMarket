package com.example.test.dashboardbuyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.test.R
import com.example.test.dashboardseller.PostSeafood
import com.example.test.recipes.RecipeActivity
import com.example.test.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val postSeafoodCard: CardView = findViewById(R.id.postseafoodCard)
        postSeafoodCard.setOnClickListener {
            // Handle button click, navigate to Post Seafood activity
            val intent = Intent(this@MainActivity, OrderStatusActivity::class.java)
            startActivity(intent)
        }
        val findseafoodCard: CardView = findViewById(R.id.findseafoodCard)
        findseafoodCard.setOnClickListener {
            // Handle button click, navigate to Post Seafood activity
            val intent = Intent(this@MainActivity, FindSeaFood::class.java)
            startActivity(intent)

        }

        val welcomeCard: CardView = findViewById(R.id.welcomeCard)
        welcomeCard.setOnClickListener {
            // Handle button click, navigate to Post Seafood activity
            val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            startActivity(intent)
        }

        val recipeCard: CardView = findViewById(R.id.recipesCard)
        recipeCard.setOnClickListener {
            val  intent = Intent(this@MainActivity, RecipeActivity::class.java)
            startActivity(intent)
        }
    }
}
