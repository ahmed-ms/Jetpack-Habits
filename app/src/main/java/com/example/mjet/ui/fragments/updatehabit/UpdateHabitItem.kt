package com.example.mjet.ui.fragments.updatehabit

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mjet.R
import com.example.mjet.data.models.Habit
import com.example.mjet.utils.ActionFragment
import com.example.mjet.utils.Calculations
import com.maltaisn.icondialog.IconDialog
import com.maltaisn.icondialog.IconDialogSettings
import com.maltaisn.icondialog.data.Icon
import kotlinx.android.synthetic.main.fragment_create_habit_item.*
import kotlinx.android.synthetic.main.fragment_update_habit_item.*
import java.util.*


class UpdateHabitItem : ActionFragment(R.layout.fragment_update_habit_item),
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private val args by navArgs<UpdateHabitItemArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Retrieve data from our habit list
        et_habitTitle_update.setText(args.selectedhabit.habit_title)
        et_habitDescription_update.setText(args.selectedhabit.habit_description)

        setHasOptionsMenu(true)
        //Pick a date and time
        pickDateAndTime()

        //Selected and image to put into our list
        drawableSelectedFun()

        btn_confirm_update.setOnClickListener {
            updateHabit()
        }
        val iconDialog =
            this.childFragmentManager.findFragmentByTag(ActionFragment.ICON_DIALOG_TAG) as IconDialog?
                ?: IconDialog.newInstance(IconDialogSettings())

        ub_btn_pickIcon.setOnClickListener {
            iconDialog.show(childFragmentManager, ICON_DIALOG_TAG)
        }


    }

    private fun updateHabit() {
        //Get text from editTexts
        title = et_habitTitle_update.text.toString()
        description = et_habitDescription_update.text.toString()

        //Create a timestamp string for our recyclerview
        timeStamp = "$cleanDate $cleanTime"

        //Check that the form is complete before submitting data to the database
        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit =
                Habit(args.selectedhabit.id,iconID, title, description, timeStamp, drawableSelected)

            //add the habit if all the fields are filled
            habitViewModel.updateHabit(habit)
            Toast.makeText(context, "Habit updated! successfully!", Toast.LENGTH_SHORT).show()

            //navigate back to our home fragment
            findNavController().navigateUp()
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawableSelectedFun() {
        iv_fastFoodSelected_update.setOnClickListener{
            iv_fastFoodSelected_update.isSelected = !iv_fastFoodSelected_update.isSelected
            drawableSelected=R.drawable.ic_fastfood
            //de-select the other options when we pick an image
            iv_smokingSelected_update.isSelected = false
            iv_teaSelected_update.isSelected = false


        }
        iv_smokingSelected_update.setOnClickListener {
            iv_smokingSelected_update.isSelected = !iv_smokingSelected_update.isSelected
            drawableSelected = R.drawable.ic_smoking2

            //de-select the other options when we pick an image
            iv_fastFoodSelected_update.isSelected = false
            iv_teaSelected_update.isSelected = false
        }

        iv_teaSelected_update.setOnClickListener {
            iv_teaSelected_update.isSelected = !iv_teaSelected_update.isSelected
            drawableSelected = R.drawable.ic_tea

            //de-select the other options when we pick an image
            iv_fastFoodSelected_update.isSelected = false
            iv_smokingSelected_update.isSelected = false
        }
    }

    private fun pickDateAndTime() {
        btn_pickDate_update.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        btn_pickTime_update.setOnClickListener {
            getTimeCalendar()
            TimePickerDialog(context, this, hour, minute, true).show()
        }
    }
    //get the current time
    private fun getTimeCalendar() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }
    //get the current date
    private fun getDateCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }
    override fun onTimeSet(view: TimePicker?, p1: Int, p2: Int) {
        Log.d("Fragment", "Time: $p1:$p2")

        cleanTime = Calculations.cleanTime(p1, p2)
        tv_timeSelected_update.text = "Time: $cleanTime"
    }

    override fun onDateSet(view: DatePicker?, yearX: Int, monthX: Int, dayOfMonth: Int) {
        cleanDate = Calculations.cleanDate(dayOfMonth, monthX, yearX)
        tv_dateSelected_update.text = "Date: $cleanDate"
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.single_item_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> deleteHabit(args.selectedhabit)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onIconDialogIconsSelected(dialog: IconDialog, icons: List<Icon>) {
        "Icon: ${icons[0].tags[0]} ".also { up_tv_iconSelected.text = it }
        val iconIds = icons.map { it.id }
        val pack = iconDialogIconPack
        val icon = pack?.getIcon(iconIds[0])
        iconID = iconIds[0]
        icon.let {
            if (it != null) {
                up_selectedIconView.setImageDrawable(it.drawable)
            }
        }
    }

    private fun deleteHabit(habit: Habit){

        val builder=AlertDialog.Builder(context)
        builder.setTitle("Delete confirm")
        builder.setMessage("Are you shore to delete this habit ? ")
        builder.setPositiveButton("Ok",DialogInterface.OnClickListener { dialog, which ->
            habitViewModel.deleteHabit(habit)
            Toast.makeText(context,"Habit has been deleted !" ,Toast.LENGTH_LONG ).show()
            findNavController().navigate(R.id.action_updateHabitItem_to_habitList)
        })
        builder.setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(context,"No Action!" ,Toast.LENGTH_LONG ).show()
        })
        builder.show()
        return

    }

}