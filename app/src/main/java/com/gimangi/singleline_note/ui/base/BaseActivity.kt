package com.gimangi.singleline_note.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

abstract class BaseActivity<T: ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
): AppCompatActivity() {
    private var _binding: T? = null
    val binding get() = _binding!!

    protected lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResId)
        initAdmob()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun initAdmob()
}