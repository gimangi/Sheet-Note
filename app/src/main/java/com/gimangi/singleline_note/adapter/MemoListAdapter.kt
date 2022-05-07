package com.gimangi.singleline_note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.data.MemoPreviewData
import com.gimangi.singleline_note.databinding.ItemMemoListBinding

class MemoListAdapter() : RecyclerView.Adapter<MemoListAdapter.MemoHolder>() {
    private val dataList = mutableListOf<MemoPreviewData>()

    class MemoHolder(private val binding: ItemMemoListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: MemoPreviewData) {

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

        }
    }

    override fun getItemCount(): Int = dataList.size
}