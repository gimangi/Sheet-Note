package com.gimangi.singleline_note.ui.memo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.databinding.ActivityMemoCreateBinding
import com.gimangi.singleline_note.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoEditActivity :
    BaseActivity<ActivityMemoCreateBinding>(R.layout.activity_memo_create) {

    private val memoEditViewModel: MemoEditViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadDataFromIntent()
        initBinding()
        initTextWatcher()
        initClickListener()
    }

    private fun loadDataFromIntent() {
        memoEditViewModel.id = intent.getIntExtra("memoId", 0)
        memoEditViewModel.name = intent.getStringExtra("memoName") ?: ""
        memoEditViewModel.suffix = intent.getStringExtra("memoSuffix") ?: ""
        binding.etNewMemoName.editText.setText(memoEditViewModel.name)
        binding.etNewMemoSuffix.editText.setText(memoEditViewModel.suffix)
    }

    private fun initBinding() {
        binding.viewModel = memoEditViewModel
    }

    private fun initTextWatcher() {
        binding.etNewMemoName.editText.addTextChangedListener(AllTextNotEmptyWatcher())
        binding.etNewMemoSuffix.editText.addTextChangedListener(AllTextNotEmptyWatcher())
    }

    inner class AllTextNotEmptyWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            // 완료 버튼 활성화/비활성화
            val title = binding.etNewMemoName.editText.text
            val suffix = binding.etNewMemoSuffix.editText.text
            memoEditViewModel.name = title.toString()
            memoEditViewModel.suffix = suffix.toString()
            memoEditViewModel.allTextNotEmpty.set(
                title.isNotEmpty() && suffix.isNotEmpty()
            )
        }
    }

    private fun initClickListener() {
        binding.ibToolbarBack.setOnClickListener {
            finish()
        }
        binding.btnMemoCreate.setOnClickListener {
//            memoEditViewModel.insertNewMemo(
//                memoName = memoEditViewModel.name,
//                suffix = memoEditViewModel.suffix
//            )
            finish()
        }
    }
}