package com.gimangi.singleline_note.ui.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
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

        initBinding()
        initTextWatcher()
        initClickListener()
    }

    private fun initBinding() {
        binding.viewModel = memoCreateViewModel
    }

    private fun initTextWatcher() {
        binding.etNewMemoTitle.editText.addTextChangedListener(AllTextNotEmptyWatcher())
        binding.etNewMemoSuffix.editText.addTextChangedListener(AllTextNotEmptyWatcher())
    }

    inner class AllTextNotEmptyWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            // 완료 버튼 활성화/비활성화
            val title = binding.etNewMemoTitle.editText.text
            val suffix = binding.etNewMemoSuffix.editText.text
            memoCreateViewModel.title = title.toString()
            memoCreateViewModel.suffix = suffix.toString()
            memoCreateViewModel.allTextNotEmpty.set(
                title.isNotEmpty() && suffix.isNotEmpty()
            )
        }
    }

    private fun initClickListener() {
        binding.ibToolbarBack.setOnClickListener {
            finish()
        }
        binding.btnMemoCreate.setOnClickListener {
            memoCreateViewModel.insertNewMemo(
                memoName = memoCreateViewModel.title,
                suffix = memoCreateViewModel.suffix
            )
            finish()
        }
    }
}