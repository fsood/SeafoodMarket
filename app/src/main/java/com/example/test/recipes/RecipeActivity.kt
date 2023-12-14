package com.example.test.recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.test.R
import com.squareup.picasso.Picasso

class RecipeActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView
    private lateinit var requestQueue: RequestQueue
    private val recipeImageUrlList = ArrayList<String>() // List to hold recipe image URLs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        recipeListView = findViewById(R.id.recipeListView)
        requestQueue = Volley.newRequestQueue(this)

        fetchRecipes()
    }

    private fun fetchRecipes() {
        val recipeURL = "https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, recipeURL, null,
            { response ->
                val recipes = response.getJSONArray("meals")
                val recipeNames = ArrayList<String>()

                for (i in 0 until recipes.length()) {
                    val recipe = recipes.getJSONObject(i)
                    val recipeName = recipe.getString("strMeal")
                    val imageUrl = recipe.getString("strMealThumb") // Extracting image URL
                    recipeImageUrlList.add(imageUrl) // Storing image URLs
                    recipeNames.add(recipeName)
                }

                val adapter = object : ArrayAdapter<String>(
                    this@RecipeActivity,
                    R.layout.list_item_recipe,
                    R.id.recipeNameTextView,
                    recipeNames
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent)

                        val recipeImageView: ImageView = view.findViewById(R.id.recipeImageView)
                        val recipeNameTextView: TextView = view.findViewById(R.id.recipeNameTextView)

                        // Load recipe image using Picasso/Glide or set a placeholder
                        // Replace 'placeholder_image' with the image resource to display as a placeholder
                        Picasso.get().load(recipeImageUrlList[position]).placeholder(R.drawable.default_image).into(recipeImageView)

                        // Set recipe name in the TextView
                        recipeNameTextView.text = recipeNames[position]

                        return view
                    }
                }

                recipeListView.adapter = adapter
                recipeListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedRecipe = recipes.getJSONObject(position)
                    val mealId = selectedRecipe.getString("idMeal")
                    fetchFullRecipeDetails(mealId)
                }
            },
            { error ->
                // Handle error
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun fetchFullRecipeDetails(mealId: String) {
        val fullRecipeURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$mealId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, fullRecipeURL, null,
            { response ->
                val fullRecipe = response.getJSONArray("meals")
                if (fullRecipe.length() > 0) {
                    val selectedRecipe = fullRecipe.getJSONObject(0)

                    // Extract recipe details
                    val recipeName = selectedRecipe.getString("strMeal")
                    val instructions = selectedRecipe.getString("strInstructions")
                    val imageUrl = selectedRecipe.getString("strMealThumb")

                    // Extract ingredients
                    val ingredients = ArrayList<String>()
                    for (i in 1..20) { // Assuming maximum 20 ingredients
                        val ingredient = selectedRecipe.getString("strIngredient$i")
                        val measure = selectedRecipe.getString("strMeasure$i")
                        if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                            val ingredientText = "$ingredient - $measure"
                            ingredients.add(ingredientText)
                        }
                    }

                    // Update UI with recipe details
                    val intent = Intent(this@RecipeActivity, FullRecipeActivity::class.java)
                    intent.putExtra("recipeName", recipeName)
                    intent.putExtra("instructions", instructions)
                    intent.putStringArrayListExtra("ingredients", ingredients)
                    intent.putExtra("imageUrl", imageUrl) // Pass the image URL
                    startActivity(intent)
                }
            },
            { error ->
                // Handle error
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}
