package com.gimangi.singleline_note.di

import com.gimangi.singleline_note.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel { MainViewModel(get()) }
}