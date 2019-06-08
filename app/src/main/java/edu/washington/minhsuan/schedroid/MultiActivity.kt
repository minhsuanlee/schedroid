package edu.washington.minhsuan.schedroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.Serializable

class MultiActivity : AppCompatActivity() {

    val TAG = "MultiActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)

        val db = DatabaseHelper(applicationContext)

        var type: String
        var username: String

        intent.extras.apply {
            type = this.getString("Type")
            if (type == "Login") {
                username = this.getString("Username")
                Toast.makeText(this@MultiActivity, "Welcome Back ${db.getName(username)}!",
                    Toast.LENGTH_LONG).show()

                // Calendar View Fragment
                val calendarFragment = CalendarFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, calendarFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val signUpFragment = SignUpFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, signUpFragment, "SIGN_UP_FRAGMENT")
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
