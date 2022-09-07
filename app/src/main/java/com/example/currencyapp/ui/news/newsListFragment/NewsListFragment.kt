package com.example.currencyapp.ui.news.newsListFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.databinding.FragmentNewsListBinding
import com.example.currencyapp.domain.repository.MainRepository.DataState.*
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.ui.news.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListFragment : Fragment() {

    companion object {
        fun newInstance() = NewsListFragment()
    }

    private val viewModel: NewsViewModel by activityViewModels()

    private val adapter = NewsListAdapter()

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNewsList.adapter = adapter

        setupObservers()

        binding.settingsImageButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(NewsListFragmentDirections.actionNavigationNewsToSearchSettingsFragment())
        }
    }

    private fun setupObservers() {
        viewModel.newsDataState.observe(viewLifecycleOwner) { dataState ->
            binding.newsProgressBar.isVisible = dataState is Loading
            when (dataState) {
                is Success -> {
                    adapter.newsList = dataState.result
                }
                is Failure -> {
                    Log.e(TAG, "NewsStateObserver Failure: ${dataState.errorInfo}")

                    Toast.makeText(
                        this.requireContext(),
                        if (viewModel.networkStatus.value == ConnectivityObserver.Status.Available) getString(
                            R.string.standart_error_message
                        ) else getString(
                            R.string.no_internet_connection_error_message
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Log.d(TAG, "NewsStateObserver : $dataState")
            }
        }
    }
}