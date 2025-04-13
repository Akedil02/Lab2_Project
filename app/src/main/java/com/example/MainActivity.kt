package com.example

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.chatlibrarie.ChatLibrary
import com.example.lab2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStartChat = findViewById<Button>(R.id.buttonStartChat)
        buttonStartChat.setOnClickListener {

            ChatLibrary.start(this)
        }
    }
}
