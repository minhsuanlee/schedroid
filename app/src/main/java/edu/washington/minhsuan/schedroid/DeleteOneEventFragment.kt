package edu.washington.minhsuan.schedroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

//click an event  - go back to one day view
class DeleteOneEventFragment : Fragment() {
    private var listener: DoneDelete? = null
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
        return inflater.inflate(R.layout.delete_one_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<ListView>(R.id.delete_listview)
        val title = view.findViewById<TextView>(R.id.delete_title)
        title.text = "Delete an Event in " + day

        //todo: display
        // title time location description daily(y/n
        var sample = arrayOf<String>()
        val sample1 = "title" + " at time"
        val sample2 = "title2" + " at time"
        //sample.add(sample1)
        sample[0] = sample1
        sample[1] = sample2

        val adapter = ArrayAdapter<String>(this as Context, android.R.layout.simple_list_item_1, sample)
        list.adapter = adapter
        list.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            //val temp = list.getItemAtPosition(position).toString().split(" (")[0].trim()

            //todo: delete from database : use title and time

            listener?.DoneDelete()
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DoneDelete) {
            listener = context
        } else throw RuntimeException("$context must implement ...")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface DoneDelete {
        fun DoneDelete()
    }

    companion object {
        fun newInstance(userName: String, day: String) = DeleteOneEventFragment().apply {
            arguments = Bundle().apply {
                putString("user", userName)
                putString("day", day)
            }
        }
    }
}