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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.NewsApiRequestOptions
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.databinding.FragmentSearchSettingsBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

private const val TAGS_REGEX = " *-?[\\w ]+(?:, *-?(?:\\w+ *)+)*"
private const val DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}"

@AndroidEntryPoint
class SearchSettingsFragment : Fragment() {

    private val viewModel: SearchSettingsViewModel by viewModels()

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    private var spinnerItems: MutableList<NewsApiRequestOptions> = mutableListOf()

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

        addSpinnerItems()

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.searchSettings.observe(viewLifecycleOwner) {
            setupDropDownList(it)

            with(binding) {
                setupInputTextFields(it)

                setupButtons()
            }
        }
    }

    private fun addSpinnerItems() {
        NewsApiRequestOptions.values().forEach { option ->
            spinnerItems.add(option)
        }
    }

    private fun FragmentSearchSettingsBinding.setupButtons() {
        setupApplyButton()

        setupCancelButton()
    }

    private fun FragmentSearchSettingsBinding.setupCancelButton() {
        buttonCancel.setOnClickListener {
            root.findNavController()
                .popBackStack()
        }
    }

    private fun FragmentSearchSettingsBinding.setupApplyButton() {
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
    }

    private fun FragmentSearchSettingsBinding.setupInputTextFields(settings: SearchSettings) {

        editTextKeywords.setText(viewModel.searchSettings.value?.keywords)

        textInputTags.apply {
            setupErrorHandling(
                Regex(TAGS_REGEX),
                getString(R.string.edit_text_error_message)
            )

            editText?.setText(viewModel.searchSettings.value?.tags.also {
                Log.d(
                    TAG,
                    "setupInputTextFieldsSetup: $it"
                )
            })
        }

        textInputDateFrom.apply {
            setupErrorHandling(
                Regex(DATE_REGEX),
                getString(R.string.edit_text_error_message)
            )

            editText?.setText(settings.timeGap.substringBefore(","))
        }

        textInputDateTo.apply {
            setupErrorHandling(
                Regex(DATE_REGEX),
                getString(R.string.edit_text_error_message)
            )

            editText?.setText(settings.timeGap.substringAfter(","))
        }
    }


    private fun setupDropDownList(settings: SearchSettings) = with(binding) {

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.news_api_date_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = getOnItemSelectedListener()
        }

        spinner.setSelection(spinnerItems.indexOf(settings.timeGapMode))

    }

    private fun FragmentSearchSettingsBinding.getOnItemSelectedListener() =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "onItemSelected: $position")

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

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d(TAG, "onNothingSelected")
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