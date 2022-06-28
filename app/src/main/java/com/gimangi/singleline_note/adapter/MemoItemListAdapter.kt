package com.gimangi.singleline_note.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.data.model.Selectable
import com.gimangi.singleline_note.databinding.ItemMemoItemsListBinding
import java.text.DecimalFormat
import java.util.*

class MemoItemListAdapter : RecyclerView.Adapter<MemoItemListAdapter.MemoItemHolder>(),
    ItemTouchHelperListener {
    private var dataList = mutableListOf<Selectable<MemoItemData>>()

    // 변경된 아이템 자동저장을 위한 Observable
    private val _changedData = MutableLiveData<MemoItemData>()
    val changedData : LiveData<MemoItemData>
        get() = _changedData

    // 선택된 행 개수
    private var _selectCount = MutableLiveData(0)
    val selectCount: LiveData<Int>
        get() = _selectCount

    // 최근 선택된 행
    private var selectedRow: Selectable<MemoItemData>? = null

    // 행 편집 모드
    override val modifyMode = ObservableField(false)

    inner class MemoItemHolder(private val binding: ItemMemoItemsListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        val lineNum = ObservableField<Int>(0)
        lateinit var selected : ObservableField<Boolean>

        init {

            val etTouchListener = object : View.OnTouchListener {
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    if (modifyMode.get() == true) {
                        if (event?.action == MotionEvent.ACTION_UP) {
                            val num = lineNum.get()
                            if (num != null)
                                onClick(num)
                        }
                        return true
                    }
                    return false
                }

            }

            binding.etMemoItemName.setOnTouchListener (etTouchListener)

            binding.etMemoItemValue.setOnTouchListener (etTouchListener)
        }

        fun onBind(sData: Selectable<MemoItemData>) {
            val data = sData.data

            binding.item = data
            binding.adapter = this@MemoItemListAdapter
            binding.viewHolder = this
            lineNum.set(data.number)
            selected = sData.selected

            // focus 해제 시 자동 저장
            val autoSaveListener =
                View.OnFocusChangeListener { _, b ->
                    if (!b)
                        autoSave(data,
                            binding.etMemoItemName.text.toString(),
                            binding.etMemoItemValue.text.toString())
                }

            binding.etMemoItemName.onFocusChangeListener = autoSaveListener
            binding.etMemoItemValue.onFocusChangeListener = autoSaveListener
        }

        fun onClick(rowNum: Int) {
            val list = dataList.filter {
                it.data.number == rowNum
            }
            if (list != null && list.isNotEmpty()) {
                list[0].selected.set(!list[0].selected.get()!!)
                if (list[0].selected.get() == true) {
                    _selectCount.postValue(_selectCount.value!! + 1)
                    selectedRow = dataList.filter {
                        it.data.number == rowNum
                    }[0]
                }
                else
                    _selectCount.postValue(_selectCount.value!! - 1)
            }
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
        this.dataList = dataList.map {
            Selectable(
                data = it,
                selected = ObservableField(false)
            )
        } as MutableList<Selectable<MemoItemData>>
        notifyDataSetChanged()
    }

    private fun autoSave(data: MemoItemData, newName: String, newValueStr: String) {
        val temp = newValueStr.replace(",", "")
        var newValue = 0L
        if (temp.isNotEmpty()) {
            newValue = temp.toLong()
        }
        autoSave(data, newName, newValue)
    }

    private fun autoSave(data: MemoItemData, newName: String, newValue: Long) {
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
            it.data.number == data.number
        }.forEach {
            it.data.name = newName
            it.data.value = newValue
        }
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

    class MemoItemTouchHelperCallback(
        private val listener: ItemTouchHelperListener
    ) : ItemTouchHelper.Callback() {

        private var isMoved = false

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return if (listener.modifyMode.get() == true) {
                isMoved = true
                this.listener.onItemMove(
                    viewHolder.absoluteAdapterPosition,
                    target.absoluteAdapterPosition
                )
            } else
                false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun isLongPressDragEnabled(): Boolean {
            if (listener.modifyMode.get() == true)
                return true
            return false
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (isMoved) {
                isMoved = false
                listener.afterDragAndDrop()
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipe(position: Int) {
    }

    override fun afterDragAndDrop() {
        realign()
    }

    fun realign() {
        for (i in 0 until dataList.size) {
            val data = dataList[i].data
            data.number = i+1
        }

        for (d in dataList) {
            val data = d.data
            autoSave(data, data.name, data.value)
        }
        notifyDataSetChanged()
    }

    fun selectedPos(): Int {
        if (selectedRow == null)
            return -1
        return dataList.indexOf(selectedRow)
    }

}