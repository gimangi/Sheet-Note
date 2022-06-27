package com.gimangi.singleline_note.di

import com.gimangi.singleline_note.ui.main.MainViewModel
import com.gimangi.singleline_note.ui.memo.MemoCreateViewModel
import com.gimangi.singleline_note.ui.memo.MemoDetailViewModel
import com.gimangi.singleline_note.ui.memo.MemoEditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel { MainViewModel() }
    viewModel { MemoCreateViewModel() }
    viewModel { MemoDetailViewModel() }
    viewModel { MemoEditViewModel() }

}