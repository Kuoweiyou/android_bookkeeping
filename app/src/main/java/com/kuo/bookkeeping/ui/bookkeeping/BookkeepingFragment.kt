package com.kuo.bookkeeping.ui.bookkeeping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kuo.bookkeeping.R
import com.kuo.bookkeeping.databinding.FragmentBookkeepingBinding

class BookkeepingFragment : Fragment() {

    private var _binding: FragmentBookkeepingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookkeepingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAddRecord.setOnClickListener {
            findNavController().navigate(R.id.action_bookkeepingFragment_to_addRecordFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}