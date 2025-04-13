package com.example.chatlibrarie

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlibrarie.ChatAdapter
import com.example.chatlibrarie.Message
import com.example.chatlibrarie.WebSocketClient

internal class ChatActivity : AppCompatActivity(), WebSocketClient.MessageListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: ChatAdapter
    private lateinit var webSocketClient: WebSocketClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)

        adapter = ChatAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        webSocketClient = WebSocketClient(this)
        webSocketClient.connect()

        buttonSend.setOnClickListener {
            val text = editTextMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text, isSentByUser = true)

                webSocketClient.sendMessage(text)

                editTextMessage.setText("")
            }
        }
    }

    private fun addMessage(content: String, isSentByUser: Boolean) {
        messages.add(Message(content, isSentByUser))
        adapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }

    override fun onMessageReceived(message: String) {

        runOnUiThread {

            if (message == "203 = 0xcb") {
                addMessage("⚠️ Server command received", isSentByUser = false)
            } else {
                addMessage(message, isSentByUser = false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, ChatActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}
