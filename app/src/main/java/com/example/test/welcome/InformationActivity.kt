package com.example.test.welcome

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R

class InformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val title = intent.getStringExtra("TITLE") ?: "Information"
        val content = intent.getStringExtra("CONTENT") ?: "No content available"


        val titleTextView: TextView = findViewById(R.id.textViewTitle)
        val contentTextView: TextView = findViewById(R.id.textViewContent)

        titleTextView.text = title
        contentTextView.text = content
    }
}
