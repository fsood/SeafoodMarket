package com.example.test.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val fishstoreTextView: TextView = findViewById(R.id.textViewFishstore)
        val postSeafoodTextView: TextView = findViewById(R.id.textViewPostSeafood)
        val cleaningTipsTextView: TextView = findViewById(R.id.textViewCleaningTips)

        fishstoreTextView.setOnClickListener {
            startActivityWithTextInformation("Fishstore", "Our goal with FishLine is to connect you with fresh local seafood directly from the fishermen themselves or from Fish Markets. \n" +
                    "\n We feel that knowing more about the seafood you buy and eat, where it comes from, and even how the fishery is managed, will help you make informed choices the next time you buy.\n" +
                    "\n Beyond simply telling you what seafood is available, we provide:\n" +
                    "\n 1. Where to find Fresh Local Seafood today\n" +
                    "\n 2. How to add where you Found Fresh Local Seafood today - if we don't already have it listed.\n " +
                    "\n 3. Recipes - what to do with that seafood when you get home. ")
        }

        postSeafoodTextView.setOnClickListener {
            startActivityWithTextInformation("Post Seafood", "Posting seafood is as easy as 1-2-3 on FishLine \n" +
                    "\n 1. Tap on Post Seafood. \n" +
                    "\n 2. Add the details - species, type of seller, seller name, etc.; \n" +
                    "\n 3. Provide contact info, hours, forms of payment; \n" +
                    "\n 4. You can add a picture; \n" +
                    "\n 5. Tap Submit! ")
        }

        cleaningTipsTextView.setOnClickListener {
            startActivity(Intent(this, CleaningTipsActivity::class.java))
        }
    }

    private fun startActivityWithTextInformation(title: String, content: String) {
        val intent = Intent(this, InformationActivity::class.java)
        intent.putExtra("TITLE", title)
        intent.putExtra("CONTENT", content)
        startActivity(intent)
    }
}
