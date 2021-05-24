package com.example.mjet.ui.fragments.habitlist

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mjet.R
import com.example.mjet.data.models.Habit
import com.example.mjet.ui.fragments.habitlist.adaptor.HabitListAdapter
import com.example.mjet.ui.viewmodels.HabitViewModels
import kotlinx.android.synthetic.main.fragment_habit_list.*


class HabitList : Fragment(R.layout.fragment_habit_list) {
    private val habitViewModel: HabitViewModels by viewModels()
    private lateinit var habitList: List<Habit>
    private lateinit var adapter: HabitListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = HabitListAdapter()
        rv_habits.adapter = adapter
        rv_habits.layoutManager = LinearLayoutManager(context)
        loadModels()
        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_habitList_to_createHabitItem)
        }
        //Show the options menu in this fragment
        setHasOptionsMenu(true)
        swipeToRefresh.setOnRefreshListener {
            adapter.setData(habitList)
            swipeToRefresh.isRefreshing = false
        }

    }
    private fun loadModels() {

        habitViewModel.getAllHabits.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            habitList = it

            if (it.isEmpty()) {
                rv_habits.visibility = View.GONE
                tv_emptyView.visibility = View.VISIBLE
            } else {
                rv_habits.visibility = View.VISIBLE
                tv_emptyView.visibility = View.GONE
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteAll( ){

        val builder= AlertDialog.Builder(context)
        builder.setTitle("Delete confirm")
        builder.setMessage("Are you shore to delete this habit ? ")
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
            habitViewModel.deleteAll()
            Toast.makeText(context,"All Habits has been deleted !" , Toast.LENGTH_LONG ).show()

        })
        builder.setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(context,"No Action!" , Toast.LENGTH_LONG ).show()
        })
        builder.show()
        return

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem=menu.findItem(R.id.nav_delete)
        if(habitList.isEmpty()){
            menuItem.isEnabled = false
            menuItem.icon.alpha=130
        }else{
            menuItem.isEnabled = true
            menuItem.icon.alpha=255
        }
    }
}