package com.kuo.bookkeeping.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TableLayout
import com.kuo.bookkeeping.databinding.KeyboardCalculatorBinding

class CalculatorKeyboard : TableLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var _binding: KeyboardCalculatorBinding? = null
    val binding get() = _binding as KeyboardCalculatorBinding

    init {
        _binding = KeyboardCalculatorBinding.inflate(LayoutInflater.from(context), this)
    }
}