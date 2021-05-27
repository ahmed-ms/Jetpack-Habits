package com.example.mjet.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mjet.App
import com.example.mjet.ui.viewmodels.HabitViewModels
import com.maltaisn.icondialog.IconDialog
import com.maltaisn.icondialog.data.Icon
import com.maltaisn.icondialog.pack.IconPack

abstract class ActionFragment(layout: Int) : Fragment(layout),
    IconDialog.Callback{
    override val iconDialogIconPack: IconPack?
        get() = (requireActivity().application as App).iconPack


    var iconID:Int? = null
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
    companion object {
        const val ICON_DIALOG_TAG = "icon-dialog"
    }
}