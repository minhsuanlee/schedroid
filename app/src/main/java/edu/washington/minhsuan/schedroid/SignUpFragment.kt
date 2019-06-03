package edu.washington.minhsuan.schedroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpFragment : Fragment() {
    private val TAG = "SignUpFragment"

    companion object {
        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val db = DatabaseHelper(context!!)

        val fullNameE = rootView.findViewById<EditText>(R.id.etxtFullName)
        val phoneE = rootView.findViewById<EditText>(R.id.etxtPhone)
        val usernameE = rootView.findViewById<EditText>(R.id.etxtUsername)
        val passwordE = rootView.findViewById<EditText>(R.id.etxtPassword)
        val createBtn = rootView.findViewById<Button>(R.id.btnCreate)

        createBtn.setOnClickListener {
            val full_name = fullNameE.text.toString()
            val phone_num = phoneE.text.toString()
            val username = usernameE.text.toString()
            val password = passwordE.text.toString()

            var error = ""
            if (full_name.isEmpty()) { error = "$error Please enter your full name;\n"}
            if (phone_num.isEmpty()) { error = "$error Please enter your phone number;\n"}
            if (username.isEmpty()) { error = "$error Please enter your username;\n"}
            if (password.isEmpty()) { error = "$error Please enter your password;\n"}

            if (error.isNotEmpty()) {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            } else {
                if (db.isExistUser(username)) {
                    Toast.makeText(activity,
                        "$username has already been used. Try again with a different username.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    val user = User(full_name, phone_num, username, password)
                    db.insertData(user)

                    Toast.makeText(activity, "Nice to meet you! $full_name", Toast.LENGTH_SHORT).show()
                    // Calendar View Fragment

                    readAll(db)
                }
            }
        }

        return rootView
    }

    private fun readAll(db: DatabaseHelper) {
        var dataUsers = db.readData()
        var result = ""
        for (i in 0 until (dataUsers.size - 1)) {
            val user = dataUsers.get(i)
            result = "$result $user\n"
        }
        Toast.makeText(activity, "$result", Toast.LENGTH_SHORT).show()

        var dataEvents = db.readEvents()
        var resultEvents = ""
        for (i in 0 until (dataEvents.size - 1)) {
            val event = dataEvents.get(i)
            resultEvents = "$resultEvents $event\n"
        }
        Toast.makeText(activity, "$resultEvents", Toast.LENGTH_SHORT).show()
    }
}
