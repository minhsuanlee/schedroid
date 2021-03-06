package edu.washington.minhsuan.schedroid

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CalendarFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater.inflate(R.layout.calendar, container, false)
        val calendarView = rootview.findViewById<CalendarView>(R.id.calendar)
        calendarView.setOnDateChangeListener { view:View, year:Int, month:Int, dayOfMonth:Int ->
            val date = String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth) +
                    "-" + year.toString()
            App.instance.repo.currentDate = date
            val onedayFragment = OnedayFragment.newInstance(App.instance.repo.currentName!!, date)
            fragmentManager!!.beginTransaction()
                .replace(R.id.container, onedayFragment, "ONE_DAY_FRAGMENT")
                .addToBackStack(null)
                .commit()
            //Toast.makeText(context, "" + dayOfMonth, Toast.LENGTH_SHORT).show()
        }

        val logoutBtn = rootview.findViewById<Button>(R.id.btnLogout)
        logoutBtn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        return rootview
    }

}
