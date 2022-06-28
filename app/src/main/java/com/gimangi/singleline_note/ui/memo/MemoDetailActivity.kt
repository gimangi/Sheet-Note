package com.gimangi.singleline_note.ui.memo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.adapter.MemoItemListAdapter
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoStatus
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.database.dto.getStatusString
import com.gimangi.singleline_note.data.mapper.MemoDataMapper
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.data.model.DropdownItem
import com.gimangi.singleline_note.databinding.ActivityMemoDetailBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import com.gimangi.singleline_note.ui.shared.SlnDropDown
import com.gimangi.singleline_note.ui.shared.showDropDown
import com.gimangi.singleline_note.util.NumeralUtil
import com.gimangi.singleline_note.util.dpToPx
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class MemoDetailActivity :
    BaseActivity<ActivityMemoDetailBinding>(R.layout.activity_memo_detail) {

    private val memoDetailViewModel: MemoDetailViewModel by viewModel()

    private lateinit var memoItemListAdapter: MemoItemListAdapter
    private lateinit var memoItemHelper: ItemTouchHelper

    private var dropdown: SlnDropDown? = null

    private var mInterstitialAd: InterstitialAd? = null

    companion object {
        val SUMMARY_LIST = arrayOf(
            R.string.memo_status_sum,
            R.string.memo_status_avg,
            R.string.memo_status_max,
            R.string.memo_status_min)
        const val TAG = "DETAIL_VIEW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMemoListAdapter()
        initBinding()
        observeMemoData()
        getIntentData()
        loadData()
        initClickListener()
        setCommaNumberText()
        initAdmobFront()
    }

    override fun onPause() {
        super.onPause()
        // focus를 잃도록 -> 자동저장
        currentFocus?.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroy() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
        super.onDestroy()
    }

    private fun initBinding() {
        binding.viewModel = memoDetailViewModel
        binding.adapter = memoItemListAdapter
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

        memoItemListAdapter.selectCount.observe(this) {
            binding.btnInsertRow.isEnabled = it == 1
        }
    }

    private fun getIntentData() {
        memoDetailViewModel.memoId = intent.getIntExtra("memoId", 0)
    }

    private fun loadData() {
        // load data from DB
        memoDetailViewModel.getMemoData().observe(this) {
            memoDetailViewModel.memoTableData.value = it
        }
    }

    private fun initMemoListAdapter() {
        memoItemListAdapter = MemoItemListAdapter()
        memoItemHelper = ItemTouchHelper(MemoItemListAdapter.MemoItemTouchHelperCallback(memoItemListAdapter))
        memoItemHelper.attachToRecyclerView(binding.rvMemoItemList)
        val animator = binding.rvMemoItemList.itemAnimator as DefaultItemAnimator
        animator.supportsChangeAnimations = false
        animator.changeDuration = 0

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
            addMemoRow()
        }

        // summary 선택
        binding.clSelectSummary.setOnClickListener {
            showSummaryDropDown()
        }

        // 메모 정의 편집
        binding.ibEditMemoDefine.setOnClickListener {
            val intent = Intent(this@MemoDetailActivity, MemoEditActivity::class.java).apply {
                putExtra("memoId", memoDetailViewModel.memoId)
                putExtra("memoName", memoDetailViewModel.memoTableName.get())
                putExtra("memoSuffix", memoDetailViewModel.suffix.get())
            }
            startActivity(intent)
        }

        // 메모 행 편집
        binding.ibEditMemoList.setOnClickListener {
            memoItemListAdapter.modifyMode.set(true)
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }

        // 메모 행 편집 완료
        binding.btnModifyComplete.setOnClickListener {
            memoItemListAdapter.modifyMode.set(false)
        }

        // 메모 행 삽입
        binding.btnInsertRow.setOnClickListener {
            val selectedPos = memoItemListAdapter.selectedPos()
            memoItemListAdapter.clearSelected()
            insertMemoRowAt(selectedPos+1)
        }

        // 메모 행 삭제
        binding.btnRemoveRow.setOnClickListener {
            removeRows()
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
            memoDetailViewModel.updateMemoTable(memoDetailViewModel.memoTableData.value!!)
        }
    }

    private fun addMemoRow() {
        insertMemoRowAt(null)
    }

    private fun removeRows() {
        val removeList = memoItemListAdapter.selectedItems()
        memoItemListAdapter.clearSelected()

        val table = memoDetailViewModel.memoTableData.value
        if (table != null) {
            val rowList = table.rowList
            for (r in removeList) {
                rowList.remove(
                    rowList.filter {
                        it.order == r.number
                    }[0]
                )
            }
            val resultTable = realignTableItem(table)
            memoDetailViewModel.memoTableData.postValue(resultTable)
            memoDetailViewModel.updateMemoTable(resultTable)
        }
    }

    private fun insertMemoRowAt(index: Int?) {
        val table = memoDetailViewModel.memoTableData.value

        if (table != null) {
            val row = newRow(table)

            if (index == null)
                memoDetailViewModel.insertMemoItem(table, row).observe(this) {
                    if (it != null)
                        memoDetailViewModel.memoTableData.value = it
            }
            else
                memoDetailViewModel.insertMemoItemAt(index, table, row).observe(this) {
                    if (it != null)
                        memoDetailViewModel.memoTableData.value = realignTableItem(it)
                }
        }
    }

    private fun showSummaryDropDown() {
        if (dropdown != null) {
            dropdown!!.dismiss()
        }

        val dropdownList = mutableListOf<DropdownItem>()

        for (i in SUMMARY_LIST.indices) {
            dropdownList.add(
                DropdownItem(i, getString(SUMMARY_LIST[i]), false)
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

    private fun newRow(table: MemoTableEntity): MemoItemEntity = MemoItemEntity(
        order = table.rowList.size + 1,
        item = "",
        value = 0,
        tableId = table.memoId
    )

    private fun realignTableItem(tableEntity: MemoTableEntity): MemoTableEntity {
        val list = tableEntity.rowList
        for (i in 0 until list.size) {
            list[i].order = i+1
        }

        return tableEntity
    }

    override fun initAdmob() {
        MobileAds.initialize(this) {}
        mAdView = binding.adViewBanner
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun initAdmobFront() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,getString(R.string.admob_front_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }
}