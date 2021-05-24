package com.example.mjet.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mjet.ui.viewmodels.HabitViewModels

open class ActionFragment(layout:Int):Fragment(layout) {
    var title = ""
     var description = ""
     var drawableSelected = 0
     var timeStamp = ""

     val habitViewModel: HabitViewModels by viewModels()

     var day = 0
     var month = 0
     var year = 0
     var hour = 0
     var minute = 0

     var cleanDate = ""
     var cleanTime = ""
}