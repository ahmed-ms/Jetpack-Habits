package com.example.mjet.ui.fragments.createhabit


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mjet.R
import com.example.mjet.data.models.Habit
import com.example.mjet.utils.ActionFragment
import com.example.mjet.utils.Calculations
import com.maltaisn.icondialog.IconDialog
import com.maltaisn.icondialog.IconDialogSettings
import com.maltaisn.icondialog.data.Icon
import kotlinx.android.synthetic.main.fragment_create_habit_item.*
import java.util.*

class CreateHabitItem : ActionFragment(R.layout.fragment_create_habit_item),
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_confirm.setOnClickListener {
            addHabitToDB()
        }
        //Pick a date and time
        pickDateAndTime()

        //Selected and image to put into our list
        drawableSelectedFun()

        // If dialog is already added to fragment manager, get it. If not, create a new instance.
        val iconDialog =
            this.childFragmentManager.findFragmentByTag(ActionFragment.ICON_DIALOG_TAG) as IconDialog?
                ?: IconDialog.newInstance(IconDialogSettings())

        btn_pickIcon.setOnClickListener {
            iconDialog.show(childFragmentManager, ICON_DIALOG_TAG)

        }
    }

    private fun addHabitToDB() {
        //Get text from editTexts
        title = et_habitTitle.text.toString()
        description = et_habitDescription.text.toString()
        //Create a timestamp string for our recyclerview
        timeStamp = "$cleanDate $cleanTime"
        //Check that the form is complete before submitting data to the database
        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(0, iconID, title, description, timeStamp, drawableSelected)
            //add the habit if all the fields are filled
            habitViewModel.addHabit(habit)
            Toast.makeText(context, "Habit created successfully!", Toast.LENGTH_SHORT).show()
            //navigate back to our home fragment
            findNavController().navigateUp()
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawableSelectedFun() {
        iv_fastFoodSelected.setOnClickListener {
            iv_fastFoodSelected.isSelected = !iv_fastFoodSelected.isSelected
            drawableSelected = R.drawable.ic_fastfood
            //de-select the other options when we pick an image
            iv_smokingSelected.isSelected = false
            iv_teaSelected.isSelected = false


        }
        iv_smokingSelected.setOnClickListener {
            iv_smokingSelected.isSelected = !iv_smokingSelected.isSelected
            drawableSelected = R.drawable.ic_smoking2

            //de-select the other options when we pick an image
            iv_fastFoodSelected.isSelected = false
            iv_teaSelected.isSelected = false
        }

        iv_teaSelected.setOnClickListener {
            iv_teaSelected.isSelected = !iv_teaSelected.isSelected
            drawableSelected = R.drawable.ic_tea

            //de-select the other options when we pick an image
            iv_fastFoodSelected.isSelected = false
            iv_smokingSelected.isSelected = false
        }
    }


    // @RequiresApi(Build.VERSION_CODES.N)
    //set on click listeners for our data and time pickers
    private fun pickDateAndTime() {
        btn_pickDate.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        btn_pickTime.setOnClickListener {
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
        tv_timeSelected.text = "Time: $cleanTime"
    }

    override fun onDateSet(view: DatePicker?, yearX: Int, monthX: Int, dayOfMonth: Int) {
        cleanDate = Calculations.cleanDate(dayOfMonth, monthX, yearX)
        tv_dateSelected.text = "Date: $cleanDate"
    }


    override fun onIconDialogIconsSelected(dialog: IconDialog, icons: List<Icon>) {
        "Icon: ${icons[0].tags[0]} ".also { tv_iconSelected.text = it }
        val iconIds = icons.map { it.id }
        val pack = iconDialogIconPack
        val icon = pack?.getIcon(iconIds[0])
        iconID = iconIds[0]
        icon.let {
            if (it != null) {
                selectedIconView.setImageDrawable(it.drawable)
            }
        }
    }
}