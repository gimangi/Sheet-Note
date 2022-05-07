package com.gimangi.singleline_note.ui.main

import android.os.Bundle
import android.view.View
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.adapter.MemoListAdapter
import com.gimangi.singleline_note.data.MemoPreviewData
import com.gimangi.singleline_note.databinding.ActivityMainBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import java.util.*

class MainActivity() :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var memoListAdapter: MemoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMemoAdapter()
        initDummy()
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

    private fun initDummy() {
        memoListAdapter.setDataList(
            mutableListOf(
                MemoPreviewData(
                    title = "메모 제목",
                    date = Date(),
                    content = "1,000,000",
                    suffix = "원",
                    status = "N개 항목",
                    selected = false
                )
            )
        )
    }

}