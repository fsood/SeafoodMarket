package com.example.test.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.test.R
import com.squareup.picasso.Picasso

class FullRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_recipe)

        val recipeNameTextView: TextView = findViewById(R.id.recipeNameTextView)
        val instructionsTextView: TextView = findViewById(R.id.instructionsTextView)
        val ingredientsTextView: TextView = findViewById(R.id.ingredientsTextView)
        val recipeImageView: ImageView = findViewById(R.id.recipeImageView)

        val recipeName = intent.getStringExtra("recipeName")
        val instructions = intent.getStringExtra("instructions")
        val ingredients = intent.getStringArrayListExtra("ingredients")
        val imageUrl = intent.getStringExtra("imageUrl")

        recipeNameTextView.text = recipeName
        instructionsTextView.text = instructions
        ingredientsTextView.text = ingredients?.joinToString("\n")

        // Load recipe image using Picasso
        if (!imageUrl.isNullOrBlank()) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.default_image).into(recipeImageView)
        } else {
            recipeImageView.setImageResource(R.drawable.default_image) // Placeholder if no image available
        }
    }
}