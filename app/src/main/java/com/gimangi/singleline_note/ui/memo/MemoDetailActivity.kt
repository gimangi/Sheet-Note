package com.gimangi.singleline_note.ui.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.databinding.ActivityMainBinding
import com.gimangi.singleline_note.databinding.ActivityMemoDetailBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoDetailActivity :
    BaseActivity<ActivityMemoDetailBinding>(R.layout.activity_memo_detail) {

    private val memoDetailViewModel: MemoDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}