package com.example.currencyapp.ui.newsFragment.searchSettingsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.NewsApiRequestOptions
import com.example.currencyapp.databinding.FragmentSearchSettingsBinding
import com.example.currencyapp.ui.newsFragment.NewsViewModel
import com.example.currencyapp.ui.newsFragment.mоdel.SearchSettings

//TODO: Validation
class SearchSettingsFragment : Fragment() {

    private val viewModel: NewsViewModel by activityViewModels()

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!
    private val settings = SearchSettings(null, null, null)

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

        with(binding) {
            buttonSearch.setOnClickListener {
                settings.keywords = editTextKeywords.text.toString()
                settings.tags = editTextTags.text.toString()

                if (settings.timeGap == null)
                    settings.timeGap = "${editTextDateFrom.text},${editTextDateTo.text}"


                viewModel.searchSettings.value = settings
                Log.d(TAG, "onViewCreated: $settings")
            }
        }
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
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d(TAG, "onItemSelected: $position")
                    when (position) {
                        7 -> {
                            binding.editTextDateFrom.isVisible = true
                            settings.timeGap = null
                        }
                        8 -> {
                            with(binding) {
                                editTextDateFrom.isVisible = true
                                editTextDateTo.isVisible = true
                            }
                            settings.timeGap = null
                        }
                        else -> {
                            with(binding) {
                                editTextDateFrom.isVisible = false
                                editTextDateTo.isVisible = false
                            }
                            settings.timeGap = NewsApiRequestOptions.date[position]
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "onNothingSelected")
                }

            }
        }
    }

    companion object {
        fun newInstance() = SearchSettingsFragment()
    }
}