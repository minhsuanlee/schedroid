package edu.washington.minhsuan.schedroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class MultiActivity : AppCompatActivity() , CalendarFragment.ViewDay, OnedayFragment.AddEvent, OnedayFragment.DeleteEvent, CreateNewEventFragment.DoneCreate, DeleteOneEventFragment.DoneDelete{
    private val fragmentManager = supportFragmentManager

    var userM = ""
    var day = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)
        var fragment : Fragment? = null
        userM = intent.extras[USERNAME].toString()
        fragment = CalendarFragment.newInstance(userM)
    }

    override fun ViewDay() {
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.frameLayout, OnedayFragment.newInstance(userM, day))
        ft.commit()
    }
    override fun AddEvent() {
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.frameLayout, CreateNewEventFragment.newInstance(userM))
        ft.commit()
    }
    override fun DeleteEvent() {
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.frameLayout, DeleteOneEventFragment.newInstance(userM, day))
        ft.commit()
    }
    override fun DoneCreate() {
        ViewDay()
    }
    override fun DoneDelete() {
        ViewDay()
    }

    companion object {
        val USERNAME = "USERNAME"
    }
}
