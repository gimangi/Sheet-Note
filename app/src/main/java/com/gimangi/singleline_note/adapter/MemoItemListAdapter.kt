package com.gimangi.singleline_note.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.databinding.ItemMemoItemsListBinding
import java.text.DecimalFormat

class MemoItemListAdapter() : RecyclerView.Adapter<MemoItemListAdapter.MemoItemHolder>() {
    private var dataList = mutableListOf<MemoItemData>()

    // 변경된 아이템 자동저장을 위한 Observable
    private val _changedData = MutableLiveData<MemoItemData>()
    val changedData : LiveData<MemoItemData>
        get() = _changedData

    inner class MemoItemHolder(private val binding: ItemMemoItemsListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: MemoItemData) {
            binding.item = data

            // focus 해제 시 자동 저장
            val autoSaveListener =
                View.OnFocusChangeListener { _, b ->
                    /// focus 해제
                    if (!b) {

                        val newName = binding.etMemoItemName.text.toString()
                        val valueStr = binding.etMemoItemValue.text.toString().replace(",", "")
                        var newValue = 0L
                        if (valueStr.isNotEmpty()) {
                            newValue = valueStr.toLong()
                        }

                        // observer 에게 알림
                        _changedData.value = MemoItemData(
                            number = data.number,
                            name = newName,
                            value = newValue,
                            itemId = data.itemId,
                            tableId = data.tableId
                        )

                        // data list 수정
                        dataList.filter {
                            it.number == data.number
                        }.forEach {
                            it.name = newName
                            it.value = newValue
                        }

                    }
                }

            binding.etMemoItemName.onFocusChangeListener = autoSaveListener
            binding.etMemoItemValue.onFocusChangeListener = autoSaveListener

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoItemHolder {
        val binding = ItemMemoItemsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        binding.etMemoItemValue.apply {
            addTextChangedListener(CommaTextWithLimitWatcher(this))
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

    inner class CommaTextWithLimitWatcher(val editText: EditText) : TextWatcher {
        var before = ""

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            before = editText.text.toString()
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!s.isNullOrEmpty() && s.toString() != before) {
                val strValue = s.toString().replace(",","")
                if (strValue.length > 18) {
                    editText.setText(before)
                } else {
                    val strNumber = strValue.toDoubleOrNull() ?: 0
                    editText.setText(DecimalFormat("#,###").format(strNumber))
                }

            }
        }

        override fun afterTextChanged(s: Editable?) {
            editText.setSelection(editText.text.length)
        }

    }


}