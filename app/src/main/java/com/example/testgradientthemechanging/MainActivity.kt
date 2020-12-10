package com.example.testgradientthemechanging


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import kotlin.random.Random


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.test_button).setOnClickListener {
            ThemeUtils.changeToTheme(this, Random.nextInt(0, 3))
        }

        findViewById<Button>(R.id.test_change_activity).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

}