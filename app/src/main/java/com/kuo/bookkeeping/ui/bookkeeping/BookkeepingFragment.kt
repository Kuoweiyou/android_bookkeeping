package com.kuo.bookkeeping.ui.bookkeeping

import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.databinding.FragmentBookkeepingBinding
import com.kuo.bookkeeping.ui.base.BaseFragment

class BookkeepingFragment : BaseFragment<FragmentBookkeepingBinding>(
    FragmentBookkeepingBinding::inflate
) {
    override fun setupView() {

    }

    override fun setupListener() {
        binding.fabAddRecord.setOnClickListener {
            findNavController().navigate(R.id.action_bookkeepingFragment_to_addRecordFragment)
        }
    }

    override fun setupObserver() {

    }
}