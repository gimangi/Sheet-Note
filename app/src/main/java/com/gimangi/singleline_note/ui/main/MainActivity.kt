package com.gimangi.singleline_note.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.adapter.MemoListAdapter
import com.gimangi.singleline_note.data.model.MemoPreviewData
import com.gimangi.singleline_note.databinding.ActivityMainBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import com.gimangi.singleline_note.ui.memo.MemoCreateActivity
import com.gimangi.singleline_note.ui.memo.MemoCreateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity() :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var memoListAdapter: MemoListAdapter
    private lateinit var memoPreviewList: MutableList<MemoPreviewData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMemoAdapter()
        initSearchLiveData()
        initClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadMemoList()
    }

    private fun setMemoList(list: MutableList<MemoPreviewData>) {
        this.memoPreviewList = list
        memoListAdapter.setDataList(this.memoPreviewList)
    }

    private fun initMemoAdapter() {
        // init recycler view adpater
        memoListAdapter = MemoListAdapter()
        binding.rvMemoList.adapter = memoListAdapter

        // item click listener
        memoListAdapter.setItemClickListener(
            object: MemoListAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    TODO("Not yet implemented")
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
            memoListAdapter.setDataList(filtered as MutableList<MemoPreviewData>)
        }
    }

    private fun initClickListeners() {
        binding.ibNewMemo.setOnClickListener {
            val intent = Intent(this, MemoCreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadMemoList() {
        mainViewModel.getMemoDataList().observe(this) {
            if (!it.isNullOrEmpty()) {
                // updatedAt에 관해 내림차순 정렬
                val res = it.map { entity ->
                    MemoPreviewData(
                        memoId = entity.memoId,
                        title = entity.memoName,
                        date = entity.updatedAt,
                        summary = entity.summary.toString(),
                        status = entity.status,
                        suffix = entity.suffix,
                        selected = false
                    )
                }.sortedBy { d -> d.date }.reversed()
                setMemoList(res as MutableList<MemoPreviewData>)
            } else {
                // TODO 아직 메모가 없습니다 표시
            }

        }
    }

}