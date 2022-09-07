package com.example.currencyapp.ui.currencyInfoFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.currencyapp.R
import com.example.currencyapp.databinding.FragmentCurrencyInfoBinding
import com.example.currencyapp.databinding.FragmentHomeBinding
import com.example.currencyapp.ui.homeFragment.HomeViewModel


class CurrencyInfoFragment : Fragment() {

    private val viewModel: CurrencyInfoViewModel by viewModels()

    private var _binding: FragmentCurrencyInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}