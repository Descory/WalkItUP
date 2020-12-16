package com.example.stepgame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_User = "com.example.stepgame.User"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var firstLog = prefs.getBoolean("firstLog", true)
        if(!firstLog) {
            val intent = Intent(this, Homepage::class.java).apply {
                putExtra(EXTRA_User, "")
            }
            startActivity(intent)
        }

    }

    fun loginUser(view: View) {
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var firstLog = prefs.getBoolean("firstLog", true)
        if (firstLog) {
            val editText = findViewById<EditText>(R.id.editTextTextPersonName)
            val name = editText.text.toString()

            if (name.isNotEmpty()) {
                with(prefs.edit()){
                    putBoolean("firstLog", false)
                    apply()
                }
                val intent = Intent(this, Homepage::class.java).apply {
                    putExtra(EXTRA_User, name)
                }
                val winan: ViewGroup = findViewById(R.id.mainwindow)
                val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
                winan.startAnimation(fadeOut)

                startActivity(intent)
            } else
                Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}