package com.example.myapplication

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.AlignmentSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import android.text.Layout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlEditText: EditText = findViewById(R.id.urlEditText)
        val scrapeButton: Button = findViewById(R.id.scrapeButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        scrapeButton.setOnClickListener {
            val url = urlEditText.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val doc = Jsoup.connect(url).get()
                    val spanElements = doc.select("span.chr-text")
                    val pElements = doc.select("p")
                    
                    val scrapedText = SpannableStringBuilder()
                    spanElements.forEach { 
                        val start = scrapedText.length
                        scrapedText.append(it.text()).append("\n\n")
                        scrapedText.setSpan(
                            RelativeSizeSpan(1.5f),
                            start,
                            scrapedText.length - 2,
                            0
                        )
                        scrapedText.setSpan(
                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            start,
                            scrapedText.length - 2,
                            0
                        )
                    }
                    pElements.forEach { scrapedText.append(it.text()).append("\n\n") }

                    launch(Dispatchers.Main) {
                        resultTextView.text = scrapedText
                    }
                } catch (e: Exception) {
                    launch(Dispatchers.Main) {
                        resultTextView.text = "Error: ${e.message}"
                    }
                }
            }
        }
    }
}