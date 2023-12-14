package com.example.test.welcome

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R

class CleaningTipsActivity : AppCompatActivity() {

    private lateinit var stepsTitleTextView: TextView
    private lateinit var stepsInstructionsTextView: TextView
    private lateinit var tipsTitleTextView: TextView
    private lateinit var tipsWarningsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning_tips)

        stepsTitleTextView = findViewById(R.id.textViewStepsTitle)
        stepsInstructionsTextView = findViewById(R.id.textViewStepsInstructions)
        tipsTitleTextView = findViewById(R.id.textViewTipsTitle)
        tipsWarningsTextView = findViewById(R.id.textViewTipsWarnings)

        displayInstructions()
    }

    private fun displayInstructions() {
        // Define the instructions for cleaning a fish (steps and tips/warnings)
        val stepsInstructions = "\n 1. To begin, rest the fish on the table or cutting board. Insert the knife tip into the fish's belly near the anal opening and move the blade up along the belly, cutting to the head.\n" +
                "\n 2. Keep the knife blade shallow so you don't puncture the intestines.\n" +
                "\n 3. Spread the body open and remove all of the entrails, locate the fish's anus and cut this out in a \"V\" or notch shape.\n" +
                "\n 4. Some fish have a kidney by the backbone. Remove it by scraping it out with a spoon or your thumbnail.\n" +
                "\n 5. Rinse the cavity out with a good stream of water and wash the skin. Some fish have a dark tissue lining the abdominal cavity that can be scraped off to prevent a strong, oily flavor.\n" +
                "\n 6. Remove the head if you like, trout are often cooked with the head on.\n" +
                "\n 7. Clean your fish-cleaning table immediately, collect the guts, heads, and scales, and discard them properly.\n" +
                "\n 8. Your clean fish is now ready to be cooked." // Define your cleaning steps instructions
        val tipsAndWarningsInstructions = "\n 1. Fish fins can be very sharp and cause serious puncture wounds, so be very careful when learning how to gut a fish, fillet a fish or clean a fish whole.\n" +
                "\n 2. Some fish are too bony or strong flavored to be considered edible.\n" +
                "\n 3. Some fish have very sharp teeth, be careful if you're holding a fish by the head while cleaning your fish. \n" // Define your tips and warnings instructions

        // Set the text for steps and tips/warnings sections
        stepsInstructionsTextView.text = stepsInstructions
        tipsWarningsTextView.text = tipsAndWarningsInstructions

        // Show the corresponding sections
        stepsTitleTextView.visibility = View.VISIBLE
        stepsInstructionsTextView.visibility = View.VISIBLE

        tipsTitleTextView.visibility = View.VISIBLE
        tipsWarningsTextView.visibility = View.VISIBLE
    }
}
