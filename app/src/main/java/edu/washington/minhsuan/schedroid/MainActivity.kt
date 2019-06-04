package edu.washington.minhsuan.schedroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.RequestFuture.newFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val vol = VolleySingleton.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db = DatabaseHelper(applicationContext)

        val usernameE = findViewById<EditText>(R.id.etxtUsername)
        val passwordE = findViewById<EditText>(R.id.etxtPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val signUpBtn = findViewById<Button>(R.id.btnSignup)

        readAll()

        loginBtn.setOnClickListener {
            var error = ""
            if (usernameE.text.isEmpty()) { error = "$error Please enter your username;\n"}
            if (passwordE.text.isEmpty()) { error = "$error Please enter your password;\n"}

            val username = usernameE.text.toString()
            val password = passwordE.text.toString()

            if (error.isNotEmpty()) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            } else {
                val a = isExistUser(username)
                Log.v(TAG, "$a")
                if (a && isCorrectPassword(username, password)) {
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

    private fun isExistUser(username: String): Boolean {
//        val future = newFuture<JSONObject>()
//        val stringRequest = object : StringRequest (
//            Method.POST, EndPoints.URL_IS_USER_EXIST, null, future
//        ) {
//            override fun getParams(): HashMap<String, String> {
//                val params = HashMap<String, String>()
//                params.put("username", username)
//                return params
//            }
//        }
//
//        vol?.addToRequestQueue(stringRequest)
//        var obj = JSONObject()
//
//        try {
//            var response = ""
//            while (response == "") {
//                try {
//                    obj = future.get(10, TimeUnit.SECONDS) // Block thread, waiting for response, timeout after 30 seconds
//                } catch (e: InterruptedException) {
//                    // Received interrupt signal, but still don't have response
//                    // Restore thread's interrupted status to use higher up on the call stack
//                    Thread.currentThread().interrupt()
//                    // Continue waiting for response (unless you specifically intend to use the interrupt to cancel your request)
//                }
//            }
//        } catch (e: ExecutionException) {
//            e.printStackTrace()
//        } catch (e: TimeoutException) {
//            e.printStackTrace()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        return obj.getBoolean("isExist")

        var mStatusCode = 0
        val callback = object : VolleyCallback {
            fun onSuccess(obj: JSONObject): Boolean {
                var isExist = false
                if (!obj.getBoolean("error")) {
                    isExist = obj.getBoolean("isExist")
                    Log.v(TAG, "aa$isExist")
                } else {
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                }
                return isExist
            }
        }
        var isExist = false
        val stringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_IS_USER_EXIST,
            Response.Listener<String> { response ->
                try {
                    isExist = callback.onSuccess(JSONObject(response))
                    Log.v(TAG, "aaa$isExist")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("username", username)
                return params
            }
            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                mStatusCode = response.statusCode
                return super.parseNetworkResponse(response)
            }
        }

        vol?.addToRequestQueue(stringRequest)

        while (mStatusCode != 200) {}

        return isExist
    }

//    private fun isExistUser(username: String): Boolean {
//        var isExist = false
//        val stringRequest = StringRequest(Request.Method.GET,
//            EndPoints.URL_IS_USER_EXIST,
//            Response.Listener<String> { s ->
//                try {
//                    val obj = JSONObject(s)
//                    if (!obj.getBoolean("error")) {
//                        isExist = obj.getBoolean("isExist")
//                    } else {
//                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })
//
//        val requestQueue = Volley.newRequestQueue(this)
//        requestQueue.add(stringRequest)
//
//        return isExist
//    }

//    private fun isCorrectPassword(username: String, password: String): Boolean {
//        var isCorrect = false
//        val stringRequest = StringRequest(Request.Method.GET,
//            EndPoints.URL_IS_CORRECT_PASSWORD,
//            Response.Listener<String> { s ->
//                try {
//                    val obj = JSONObject(s)
//                    if (!obj.getBoolean("error")) {
//                        isCorrect = obj.getBoolean("correct")
//                    } else {
//                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })
//
//        val requestQueue = Volley.newRequestQueue(this)
//        requestQueue.add(stringRequest)
//
//        return isCorrect
//    }

    private fun isCorrectPassword(username: String, password: String): Boolean {
        var isCorrect = false
        val stringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_IS_CORRECT_PASSWORD,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        isCorrect = obj.getBoolean("correct")
                    } else {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params.put("username", username)
                params.put("password", password)
                return params
            }
        }

        //adding request to queue
        vol?.addToRequestQueue(stringRequest)
        Log.v(TAG, "correct: $isCorrect")
        return isCorrect
    }

    private fun readAll() {
        val stringRequest = object : StringRequest(
            Method.GET, EndPoints.URL_READ_USERS,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        val users = obj.getJSONArray("users")
                        for (i in 0 until users.length()) {
                            Log.v(TAG, users.getJSONObject(i).toString())
                        }
                    } else {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }) {
        }

        vol?.addToRequestQueue(stringRequest)
    }

}

class MyStringRequest (
    method: Int, url: String, listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
) : StringRequest(method, url, listener, errorListener) {

    var statusCode: Int = 0
        private set

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        statusCode = response.statusCode
        return super.parseNetworkResponse(response)
    }
}
