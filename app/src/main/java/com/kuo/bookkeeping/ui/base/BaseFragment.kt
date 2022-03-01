package com.kuo.bookkeeping.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater:
        (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupListener()
        setupObserver()
    }

    abstract fun setupView(view: View)

    abstract fun setupListener()

    abstract fun setupObserver()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}