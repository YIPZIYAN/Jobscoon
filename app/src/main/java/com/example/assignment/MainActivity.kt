package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_applicant_list)

//        val btn = findViewById<Button>(R.id.employer_login_btn)
//        btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
    }
}

