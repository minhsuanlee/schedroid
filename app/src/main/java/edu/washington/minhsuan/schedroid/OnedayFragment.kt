package edu.washington.minhsuan.schedroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class OnedayFragment:Fragment() {
    var user : String? = null
    var day : String? = null
    private var listener: AddEvent? = null
    private var listener1: DeleteEvent? = null
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

        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.txt_title)
        val createBtn = view.findViewById<Button>(R.id.btn_addNew)
        val deleteBtn = view.findViewById<Button>(R.id.btn_deleteEvent)
        val list = view.findViewById<ListView>(R.id.display_oneday_list)

        //TODO: get events display
        // title time location description daily(y/n
        var sample = arrayOf<String>()
        val sample1 = "title" + "\ntime" + "\nlocation" + "\ndescription" + "\ndaily"
        val sample2 = "title2" + "\ntime" + "\nlocation" + "\ndescription" + "\ndaily"
        //sample.add(sample1)
        sample[0] = sample1
        sample[1] = sample2

        val adapter = ArrayAdapter<String>(this as Context, android.R.layout.simple_list_item_1, sample)
        list.adapter = adapter


        title.text = "Schedule for " + day
        createBtn.setOnClickListener{
            listener?.AddEvent()
        }
        deleteBtn.setOnClickListener {
            listener1?.DeleteEvent()
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AddEvent) {
            listener = context
        } else if (context is DeleteEvent) {
            listener1 = context
        } else throw RuntimeException("$context must implement ...")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        listener1 = null
    }
    interface AddEvent {
        fun AddEvent()
    }

    interface DeleteEvent {
        fun DeleteEvent()
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