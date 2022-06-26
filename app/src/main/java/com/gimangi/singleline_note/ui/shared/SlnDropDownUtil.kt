package com.gimangi.singleline_note.ui.shared

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.data.model.SelectableData
import com.gimangi.singleline_note.util.dpToPxF

class SlnDropDown(
                  layoutInflater: LayoutInflater,
                  view: View,
                  width: Int?,
                  height: Int?,
                  xOff: Int?,
                  yOff: Int?,
                  overlapAnchor: Boolean,
                  dataList: MutableList<SelectableData>,
                  checkVisibility: Boolean)
{

    private lateinit var adapter: SlnDropDownAdapter
    private lateinit var popup: PopupWindow

    val selected: MutableLiveData<SelectableData>
        get() = adapter.selected

    init {
        val inflater = layoutInflater.inflate(R.layout.view_dropdown, null, false)

        popup = PopupWindow(
            inflater,
            width ?: RelativeLayout.LayoutParams.WRAP_CONTENT,
            height ?: RelativeLayout.LayoutParams.WRAP_CONTENT,
            false
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            popup.elevation = 10F

        val recyclerView: RecyclerView = inflater.findViewById(R.id.rv_drop_down)
        /*recyclerView.addItemDecoration(
            SlnDecoration(1.dpToPxF, 0f, context.getColor(R.color.gray1))
        )
         */

        // 리사이클러 뷰 어댑터
        adapter = SlnDropDownAdapter(checkVisibility)

        // 어댑터 리스너
        adapter.setItemClickListener(object : SlnDropDownAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                popup.dismiss()
            }
        })

        recyclerView.adapter = adapter

        adapter.setMenuList(dataList)
        adapter.notifyDataSetChanged()

        popup.overlapAnchor = overlapAnchor

        popup.showAsDropDown(view, xOff ?: 0, yOff ?: 0, Gravity.RIGHT)
    }

    fun dismiss() {
        popup.dismiss()
    }
}

fun Activity.showDropDown(
    view: View,
    width: Int?,
    height: Int?,
    dataList: MutableList<SelectableData>,
    checkVisibility: Boolean = false
): SlnDropDown = SlnDropDown(layoutInflater, view, width, height, null, null, false, dataList, checkVisibility)
