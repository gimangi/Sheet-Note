package com.gimangi.singleline_note.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.databinding.ItemMemoItemsListBinding
import java.text.DecimalFormat

class MemoItemListAdapter() : RecyclerView.Adapter<MemoItemListAdapter.MemoItemHolder>() {
    private var dataList = mutableListOf<MemoItemData>()

    class MemoItemHolder(private val binding: ItemMemoItemsListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: MemoItemData) {
            binding.item = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoItemHolder {
        val binding = ItemMemoItemsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        binding.etMemoItemValue.apply {
            addTextChangedListener(CommaTextWatcher(this))
        }

        return MemoItemHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MemoItemHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun getDataList() = this.dataList

    fun setDataList(dataList: MutableList<MemoItemData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    inner class CommaTextWatcher(val editText: EditText) : TextWatcher {
        var before = ""

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            before = editText.text.toString()
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!s.isNullOrEmpty() && s.toString() != before) {
                val strNumber = s.toString().replace(",","").toDoubleOrNull() ?: 0
                editText.setText(DecimalFormat("#,###").format(strNumber))
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

}