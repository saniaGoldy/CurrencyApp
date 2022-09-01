package com.example.currencyapp.ui.newsFragment.searchSettingsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.example.currencyapp.R
import com.example.currencyapp.databinding.FragmentNewsListBinding
import com.example.currencyapp.databinding.FragmentSearchSettingsBinding
import com.example.currencyapp.ui.newsFragment.NewsViewModel
import com.example.currencyapp.ui.newsFragment.newsListFragment.NewsListAdapter
import com.example.currencyapp.ui.newsFragment.newsListFragment.NewsListFragment

class SearchSettingsFragment : Fragment() {

    private val viewModel: NewsViewModel by activityViewModels()

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropDownList()

    }

    private fun setupDropDownList() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.news_api_date_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner.adapter = adapter
        }
    }

    companion object{
        fun newInstance() = SearchSettingsFragment()
    }
}