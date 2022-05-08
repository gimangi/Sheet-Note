package com.gimangi.singleline_note.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.adapter.MemoListAdapter
import com.gimangi.singleline_note.data.database.room.MemoDatabase
import com.gimangi.singleline_note.data.model.MemoPreviewData
import com.gimangi.singleline_note.databinding.ActivityMainBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import com.gimangi.singleline_note.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity() :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var memoListAdapter: MemoListAdapter
    private val searchResultData = MutableLiveData<String>()
    private lateinit var memoPreviewList: MutableList<MemoPreviewData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMemoAdapter()
        initDummy()
        initSearchLiveData()
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
        binding.etSearchMemo.registerLiveData(searchResultData)

        searchResultData.observe(this) {
            val searching = it
            val filtered = memoPreviewList.filter { it ->
                it.title.startsWith(searching)
            }
            memoListAdapter.setDataList(filtered as MutableList<MemoPreviewData>)
        }
    }

    private fun initDummy() {
        setMemoList(
            mutableListOf(
                MemoPreviewData(
                    title = "memo title",
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