package edu.washington.minhsuan.schedroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testBtn = findViewById<Button>(R.id.btn_test)

        var user = ""

        testBtn.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, MultiActivity::class.java)
            intent.putExtra(MultiActivity.USERNAME, user)
            startActivity(intent)
        }
    }
}
