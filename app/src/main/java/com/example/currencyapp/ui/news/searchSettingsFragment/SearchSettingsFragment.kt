package com.example.currencyapp.ui.news.searchSettingsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.NewsApiRequestOptions
import com.example.currencyapp.databinding.FragmentSearchSettingsBinding
import com.example.currencyapp.ui.news.NewsViewModel
import com.google.android.material.textfield.TextInputLayout

private const val TAGS_REGEX = " *-?[\\w ]+(?:, *-?(?:\\w+ *)+)*"
private const val DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}"

class SearchSettingsFragment : Fragment() {

    private val viewModel: NewsViewModel by activityViewModels()

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var spinnerItems: MutableList<NewsApiRequestOptions>

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

        NewsApiRequestOptions.values().forEach { option ->
            spinnerItems.add(option)
        }

        setupDropDownList()

        with(binding) {
            textInputTags.setupErrorHandling(
                Regex(TAGS_REGEX),
                getString(R.string.edit_text_error_message)
            )
            textInputDateFrom.setupErrorHandling(
                Regex(DATE_REGEX),
                getString(R.string.edit_text_error_message)
            )
            textInputDateTo.setupErrorHandling(
                Regex(DATE_REGEX),
                getString(R.string.edit_text_error_message)
            )

            buttonApply.setOnClickListener {

                viewModel.setSearchSettings(
                    editTextKeywords.text.toString(),
                    textInputTags.editText?.text.toString(),
                    spinnerItems[spinner.selectedItemPosition],
                    textInputDateFrom.editText?.text.toString(),
                    textInputDateTo.editText?.text.toString()
                )

                root.findNavController()
                    .popBackStack()
            }

            buttonCancel.setOnClickListener {
                root.findNavController()
                    .popBackStack()
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
                    with(binding) {
                        when (position) {
                            DATE_INPUT_FROM -> {
                                textInputDateFrom.isVisible = true
                            }
                            DATE_INPUT_FROM_TO -> {
                                textInputDateFrom.isVisible = true
                                textInputDateTo.isVisible = true
                            }
                            else -> {
                                textInputDateFrom.isVisible = false
                                textInputDateTo.isVisible = false
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "onNothingSelected")
                }

            }
        }
    }

    private fun TextInputLayout.setupErrorHandling(regex: Regex, errorMessage: String) {
        editText?.addTextChangedListener {
            if (it.toString().matches(regex) || it.toString() == "") {
                isErrorEnabled = false
            } else {
                error = errorMessage
                isErrorEnabled = true
            }
        }
    }


    companion object {
        private const val DATE_INPUT_FROM = 7
        private const val DATE_INPUT_FROM_TO = 8
        fun newInstance() = SearchSettingsFragment()
    }
}