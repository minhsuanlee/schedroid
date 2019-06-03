package edu.washington.minhsuan.schedroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db = DatabaseHelper(applicationContext)

        val usernameE = findViewById<EditText>(R.id.etxtUsername)
        val passwordE = findViewById<EditText>(R.id.etxtPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val signUpBtn = findViewById<Button>(R.id.btnSignup)

        loginBtn.setOnClickListener {
            var error = ""
            if (usernameE.text.isEmpty()) { error = "$error Please enter your username;\n"}
            if (passwordE.text.isEmpty()) { error = "$error Please enter your password;\n"}

            val username = usernameE.text.toString()
            val password = passwordE.text.toString()

            if (error.isNotEmpty()) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            } else {
                if (db.isExistUser(username) && db.isCorrectPassword(username, password)) {
                    logInIntent(username, password)
                } else {
                    Toast.makeText(this@MainActivity, "You might have entered the wrong username or " +
                            "password. Please try again!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signUpBtn.setOnClickListener{
            signUpIntent()
        }

        // To delete later (below)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener{
            db.deleteAll("Users")
        }
        // To delete later (above)
    }

    private fun logInIntent(username: String, password: String) {
        val intent = Intent(this@MainActivity, MultiActivity::class.java)
        intent.putExtra("Type", "Login")
        intent.putExtra("Username", username)
        startActivity(intent)
    }

    private fun signUpIntent() {
        val intent = Intent(this@MainActivity, MultiActivity::class.java)
        intent.putExtra("Type", "Signup")
        startActivity(intent)
    }
}
