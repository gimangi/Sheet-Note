package com.gimangi.singleline_note.ui.memo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.adapter.MemoItemListAdapter
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoStatus
import com.gimangi.singleline_note.data.database.dto.getStatusString
import com.gimangi.singleline_note.data.mapper.MemoDataMapper
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.data.model.SelectableData
import com.gimangi.singleline_note.databinding.ActivityMemoDetailBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import com.gimangi.singleline_note.ui.shared.SlnDropDown
import com.gimangi.singleline_note.ui.shared.showDropDown
import com.gimangi.singleline_note.util.NumeralUtil
import com.gimangi.singleline_note.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class MemoDetailActivity :
    BaseActivity<ActivityMemoDetailBinding>(R.layout.activity_memo_detail) {

    private val memoDetailViewModel: MemoDetailViewModel by viewModel()

    private lateinit var memoItemListAdapter: MemoItemListAdapter

    private var dropdown: SlnDropDown? = null

    companion object {
        val SUMMARY_LIST = arrayOf(
            R.string.memo_status_sum,
            R.string.memo_status_avg,
            R.string.memo_status_max,
            R.string.memo_status_min)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        observeMemoData()
        initMemoListAdapter()
        getIntentData()
        initClickListener()
        setCommaNumberText()
    }

    override fun onPause() {
        super.onPause()
        // focus를 잃도록 -> 자동저장
        currentFocus?.clearFocus()
    }

    private fun initBinding() {
        binding.viewModel = memoDetailViewModel
    }

    private fun observeMemoData() {

        memoDetailViewModel.memoTableData.observe(this) {

            if (it != null) {
                // 뷰모델 데이터 갱신
                memoDetailViewModel.memoTableName.set(it.memoName)
                memoDetailViewModel.selectedSummary.set(getStatusString(it.status))
                memoDetailViewModel.summary.set(it.summary)
                memoDetailViewModel.suffix.set(it.suffix)
                // 리사이클러뷰 데이터 갱신
                val list = it.rowList.map { entity ->
                    MemoDataMapper.getMemoItemData(entity)
                } as MutableList<MemoItemData>

                memoItemListAdapter.setDataList(list)
            }
        }
    }

    private fun getIntentData() {
        memoDetailViewModel.memoId = intent.getIntExtra("memoId", 0)

        // load data from DB
        memoDetailViewModel.getMemoData().observe(this) {
            memoDetailViewModel.memoTableData.value = it
        }
    }

    private fun initMemoListAdapter() {
        memoItemListAdapter = MemoItemListAdapter()
        binding.rvMemoItemList.adapter = memoItemListAdapter

        // focus 해제된 item -> 자동저장
        memoItemListAdapter.changedData.observe(this) {
            autoSaveRow(it)
            updateSummary()
        }
    }

    private fun initClickListener() {
        // 뒤로 가기
        binding.ibToolbarBack.setOnClickListener {
            finish()
        }

        // 행 추가
        binding.clAddRow.setOnClickListener {
            val table = memoDetailViewModel.memoTableData.value

            if (table != null) {
                val newRow = MemoItemEntity(
                    order = table.rowList.size + 1,
                    item = "",
                    value = 0,
                    tableId = table.memoId
                )

                memoDetailViewModel.insertMemoItem(table, newRow).observe(this) {
                    if (it != null)
                        memoDetailViewModel.memoTableData.value = it
                }
            }
        }

        // summary 선택
        binding.clSelectSummary.setOnClickListener {

            if (dropdown != null) {
                dropdown!!.dismiss()
            }

            val dropdownList = mutableListOf<SelectableData>()

            for (i in SUMMARY_LIST.indices) {
                dropdownList.add(
                    SelectableData(i, getString(SUMMARY_LIST[i]), false)
                )
            }

            dropdown = showDropDown(
                binding.clSelectSummary,
                100.dpToPx,
                null,
                dropdownList
            )

            dropdown!!.selected.observe(this) {

                val tableData = memoDetailViewModel.memoTableData.value ?: return@observe

                val memoStatus = when (it.name) {
                    getString(R.string.memo_status_sum) -> MemoStatus.SUM
                    getString(R.string.memo_status_avg) -> MemoStatus.AVG
                    getString(R.string.memo_status_max) -> MemoStatus.MAX
                    getString(R.string.memo_status_min) -> MemoStatus.MIN
                    else -> MemoStatus.SUM
                }

                memoDetailViewModel.updateMemoTable(
                    tableData.apply {
                        this.status = memoStatus
                    }
                ).observe(this) { newTable ->
                    memoDetailViewModel.memoTableData.value = newTable
                    updateSummary()
                }
            }
        }

    }

    private fun setCommaNumberText() {
        binding.tvSummaryValue.apply {
            addTextChangedListener(CommaTextWatcher(this))
        }
    }

    private fun autoSaveRow(data: MemoItemData?) {
        val tableEntity = memoDetailViewModel.memoTableData.value
        if (data != null && tableEntity != null) {

            tableEntity.rowList.filter {
                it.order == data.number
            }.forEach {
                it.item = data.name
                it.value = data.value
            }

            memoDetailViewModel.updateMemoTable(tableEntity)
        }
    }

    private fun updateSummary() {
        val tableEntity = memoDetailViewModel.memoTableData.value

        if (tableEntity != null) {
            val summary = NumeralUtil.getSummary(tableEntity)

            memoDetailViewModel.memoTableData.value?.summary = summary
            memoDetailViewModel.summary.set(summary)
        }
    }

    inner class CommaTextWatcher(val textView: TextView) : TextWatcher {
        var before = ""

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            before = textView.text.toString()
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!s.isNullOrEmpty() && s.toString() != before) {
                val strNumber = s.toString().replace(",","").toDoubleOrNull() ?: 0
                textView.text = DecimalFormat("#,###").format(strNumber)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }
}