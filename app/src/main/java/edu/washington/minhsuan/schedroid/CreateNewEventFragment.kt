package edu.washington.minhsuan.schedroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


class CreateNewEventFragment : Fragment() {
    private var listener: DoneCreate? = null
    val db = DatabaseHelper(applicationContext)
    var user : String? = null
    var day : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString("user")
            day = it.getString("day")
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
        var daily = 0
        if (inputDaily.text.toString() == "Y" || inputDaily.text.toString() ==  "y") {
            daily = 1
        }

        val btn_map = view.findViewById<Button>(R.id.btn_open_map)

        val eventObject = Event(user, inputTitle, day, inputTime, inputDescription, (long), (lat), daily)

        // TODO: open map


        btn_ok.setOnClickListener {
            //TODO: add the new event to the database

            db.insertEvent(eventObject)
            listener?.DoneCreate()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DoneCreate) {
            listener = context
        } else throw RuntimeException("$context must implement ...")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface DoneCreate {
        fun DoneCreate()
    }

    companion object {
        fun newInstance(userName: String, day: String) = CreateNewEventFragment().apply {
            arguments = Bundle().apply {
                putString("user", userName)
                putString("day", day)
            }
        }
    }
}