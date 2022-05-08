package com.gimangi.singleline_note.ui.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.databinding.ActivityMemoCreateBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import com.gimangi.singleline_note.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoCreateActivity :
    BaseActivity<ActivityMemoCreateBinding>(R.layout.activity_memo_create) {

    private val memoCreateViewModel: MemoCreateViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}