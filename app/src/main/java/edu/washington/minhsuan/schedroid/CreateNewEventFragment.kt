package edu.washington.minhsuan.schedroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


class CreateNewEventFragment : Fragment() {
    private var listener: Back? = null
    var user : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString("user")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_new_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputTitle = view.findViewById<EditText>(R.id.edit_title)
        val inputTime = view.findViewById<EditText>(R.id.edit_time)
        val inputDescription = view.findViewById<EditText>(R.id.edit_description)
        val inputDaily = view.findViewById<EditText>(R.id.edit_Y_or_N)
        val btn_ok = view.findViewById<Button>(R.id.btn_ok)

        //TODO: back to which page?
        btn_ok.setOnClickListener {
            listener?.Back()
        }
    }

    interface fun Back() {
        fun Back()
    }

    companion object {
        fun newInstance(userName: String) = CreateNewEventFragment().apply {
            arguments = Bundle().apply { putString("user", userName) }
        }
    }
}