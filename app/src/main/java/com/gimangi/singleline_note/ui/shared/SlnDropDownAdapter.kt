package com.gimangi.singleline_note.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.data.model.SelectableData
import com.gimangi.singleline_note.databinding.ItemDropdownBinding

class SlnDropDownAdapter(
    val selectedItem: Int,
    private val checkVisibility: Boolean = false
) : RecyclerView.Adapter<SlnDropDownAdapter.DropDownViewHolder>(){

    private val dataList = mutableListOf<SelectableData>()
    val selected = MutableLiveData<SelectableData>()

    class DropDownViewHolder(private val binding: ItemDropdownBinding, private val checkVisibility: Boolean) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: SelectableData) {
            if (data.isSelected && checkVisibility)
                binding.ivChecked.visibility = View.VISIBLE
            else
                binding.ivChecked.visibility = View.INVISIBLE

            binding.title = data.name
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DropDownViewHolder {
        val binding = ItemDropdownBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return DropDownViewHolder(binding, checkVisibility)
    }

    override fun onBindViewHolder(holder: DropDownViewHolder, position: Int) {
        holder.onBind(dataList[position])

        holder.itemView.setOnClickListener {
            releaseCheckAll()
            dataList[position].isSelected = true
            selected.postValue(dataList[position])
            itemClickListener.onClick(it, position)
        }
    }

    private fun releaseCheckAll() {
        for (d in dataList)
            d.isSelected = false
    }

    override fun getItemCount(): Int = dataList.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener)  {
        this.itemClickListener = itemClickListener
    }

}