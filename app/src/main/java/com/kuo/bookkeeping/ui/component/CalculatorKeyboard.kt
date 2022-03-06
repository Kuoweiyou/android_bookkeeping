package com.kuo.bookkeeping.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.TableLayout
import com.kuo.bookkeeping.databinding.KeyboardCalculatorBinding
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat

class CalculatorKeyboard : TableLayout, View.OnClickListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var _binding: KeyboardCalculatorBinding? = null
    private val binding get() = _binding as KeyboardCalculatorBinding

    private var inputConnection: InputConnection? = null

    init {
        _binding = KeyboardCalculatorBinding.inflate(LayoutInflater.from(context), this)
        binding.btn0.setOnClickListener {
            inputConnection?.commitText("0", 1)
        }
    }

    override fun onClick(v: View?) {
        println("click keyboard: ${v?.id}")
        when (v?.id) {
            binding.btnClear.id -> {
                inputConnection?.commitText("", 1)
            }
            binding.btn0.id -> {
                inputConnection?.commitText("0", 1)
            }
            binding.btn1.id -> {
                inputConnection?.commitText("1", 1)
            }
            binding.btn2.id -> {
                inputConnection?.commitText("2", 1)
            }
            binding.btn3.id -> {
                inputConnection?.commitText("3", 1)
            }
            binding.btn4.id -> {
                inputConnection?.commitText("4", 1)
            }
            binding.btn5.id -> {
                inputConnection?.commitText("5", 1)
            }
            binding.btn6.id -> {
                inputConnection?.commitText("6", 1)
            }
            binding.btn7.id -> {
                inputConnection?.commitText("7", 1)
            }
            binding.btn8.id -> {
                inputConnection?.commitText("8", 1)
            }
            binding.btn9.id -> {
                inputConnection?.commitText("9", 1)
            }
            binding.btnDot.id -> {
                inputConnection?.commitText(".", 1)
            }
            binding.btnAddition.id -> {
                inputConnection?.commitText("+", 1)
            }
            binding.btnSubtraction.id -> {
                inputConnection?.commitText("-", 1)
            }
            binding.btnMultiply.id -> {
                inputConnection?.commitText("x", 1)
            }
            binding.btnDivision.id -> {
                inputConnection?.commitText("÷", 1)
            }
            binding.btnEquals.id -> {
                calculateResult()
            }
        }
    }

    private fun calculateResult() {
        try {
            val expression = getInputExpression()
            val result = Expression(expression).calculate()
            if (result.isNaN()) {
                throw Exception()
            } else {
                val text = DecimalFormat("0.######").format(result).toString()
                inputConnection?.commitText(text, 1)
            }
        } catch (e: Exception) {
            inputConnection?.commitText("error", 1)
        }
    }

    private fun getInputExpression(): String {
        val input = inputConnection?.getSelectedText(0)?.apply {
            replace(Regex("×"), "*")
            replace(Regex("÷"), "/")
        }
        return input?.toString() ?: ""
    }

    fun setInputConnection(ic: InputConnection) {
        this.inputConnection = ic
    }
}