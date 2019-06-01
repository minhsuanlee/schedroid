package edu.washington.minhsuan.schedroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class OnedayFragment:Fragment() {
    var user : String? = null
    var day : String? = null
    private var listener: AddEvent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString("user")
            day = it.getString("day")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.one_day_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO: get events from repo and display
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.txt_title)
        val createBtn = view.findViewById<Button>(R.id.btn_addNew)
        title.text = "Schedule for " + day
        createBtn.setOnClickListener{
            listener?.AddEvent()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AddEvent) {
            listener = context
        } else throw RuntimeException("$context must implement ...")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface AddEvent {
        fun AddEvent()
    }

    companion object {
        fun newInstance(user: String, day: String) = OnedayFragment().apply {
            arguments = Bundle().apply {
                putString("user", user)
                putString("day", day)
            }
        }
    }
}