package com.gimangi.singleline_note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.data.model.MemoPreviewData
import com.gimangi.singleline_note.databinding.ItemMemoListBinding

class MemoListAdapter() : RecyclerView.Adapter<MemoListAdapter.MemoHolder>() {
    private var dataList = mutableListOf<MemoPreviewData>()

    class MemoHolder(private val binding: ItemMemoListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: MemoPreviewData) {
            binding.prevData = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        val binding = ItemMemoListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return MemoHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        holder.onBind(dataList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setDataList(dataList: MutableList<MemoPreviewData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun getDataList() = this.dataList

}