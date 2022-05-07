package com.gimangi.singleline_note.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val searchResultData = MutableLiveData<String>()

}