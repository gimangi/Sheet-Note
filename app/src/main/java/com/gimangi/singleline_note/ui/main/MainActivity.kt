package com.gimangi.singleline_note.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.adapter.MemoListAdapter
import com.gimangi.singleline_note.data.model.DialogData
import com.gimangi.singleline_note.data.model.MemoPreviewData
import com.gimangi.singleline_note.databinding.ActivityMainBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import com.gimangi.singleline_note.ui.memo.MemoCreateActivity
import com.gimangi.singleline_note.ui.memo.MemoDetailActivity
import com.gimangi.singleline_note.ui.shared.SlnGenericDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity() :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var memoListAdapter: MemoListAdapter
    private var memoPreviewList = mutableListOf<MemoPreviewData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initMemoAdapter()
        initSearchLiveData()
        initClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadMemoList()
    }

    private fun initBinding() {
        binding.viewModel = mainViewModel
    }

    private fun initMemoAdapter() {
        // init recycler view adpater
        memoListAdapter = MemoListAdapter()
        binding.rvMemoList.adapter = memoListAdapter

        // item click listener
        memoListAdapter.setItemClickListener(
            object: MemoListAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    if (mainViewModel.isEditMode.get() == true) {
                        // edit mode -> select
                        memoListAdapter.getDataList()[position].also {
                            val selected = it.selected.get() ?: false
                            it.selected.set(!selected)  // selected 토글

                            // 선택된 메모가 존재하는지 파악 (버튼 비활성화)
                            if (memoListAdapter.getNumSelected() > 0)
                                mainViewModel.selectedMemoExist.set(true)
                            else
                                mainViewModel.selectedMemoExist.set(false)
                        }
                    } else {
                        // normal mode -> detail view
                        val intent = Intent(this@MainActivity, MemoDetailActivity::class.java).apply {
                            putExtra("memoId", memoListAdapter.getDataList()[position].memoId)
                        }
                        startActivity(intent)
                    }
                }

            }
        )
    }

    private fun initSearchLiveData() {
        binding.etSearchMemo.registerLiveData(mainViewModel.searchResultData)

        mainViewModel.searchResultData.observe(this) {
            val searching = it
            val filtered = memoPreviewList.filter { it ->
                it.title.startsWith(searching)
            }
            memoListAdapter.setDataList(filtered.toMutableList())
        }
    }

    private fun initClickListeners() {
        // 새로운 메모 생성
        binding.ibNewMemo.setOnClickListener {
            val intent = Intent(this, MemoCreateActivity::class.java)
            startActivity(intent)
        }

        // 삭제할 메모 선택 (edit mode)
        binding.ibEditMemoList.setOnClickListener {
            switchEditMode(true)
        }

        // 삭제 취소
        binding.btnEditCancel.setOnClickListener {
            // 모든 메모 선택 해제
            memoListAdapter.setSelectAll(false)
            switchEditMode(false)
        }

        // 삭제 진행
        binding.btnDeleteConfirm.setOnClickListener {

            // 삭제 확인 다이얼로그
            SlnGenericDialog(this).show(
                DialogData(
                    title = getString(R.string.dialog_memo_delete_title),
                    cancel = getString(R.string.dialog_memo_delete_cancel),
                    confirm = getString(R.string.dialog_memo_delete_confirm)
                    ),
                onConfirm = {
                    deleteSelectedMemo()
                },
                onCancel = {
                }
            )


        }
    }

    private fun deleteSelectedMemo() {

        // 삭제할 메모 리스트
        val targets = memoListAdapter.getDataList().filter {
            it.selected.get() == true
        }.map {
            it.memoId
        }

        // DB에 요청
        mainViewModel.deleteMemoDataList(targets).observe(this) {
            // 진행 완료 -> 전체 리스트에서 제거
            val deletedList = memoListAdapter.getDataList().filter {
                it.selected.get() == true
            }
            memoPreviewList.removeAll(deletedList)
            memoListAdapter.deleteDataCollection(deletedList)

            switchEditMode(false)
        }
    }

    private fun loadMemoList() {
        mainViewModel.getMemoDataList().observe(this) {
            if (!it.isNullOrEmpty()) {
                memoPreviewList = it.toMutableList()
                memoListAdapter.setDataList(it.toMutableList())
            }
        }
    }

    private fun switchEditMode(flag: Boolean) {
        mainViewModel.isEditMode.set(flag)
    }

    override fun initAdmob() {
        MobileAds.initialize(this) {}
        mAdView = binding.adViewBanner
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}