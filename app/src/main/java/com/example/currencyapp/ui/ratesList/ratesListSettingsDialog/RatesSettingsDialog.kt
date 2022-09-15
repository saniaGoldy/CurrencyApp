package com.example.currencyapp.ui.ratesList.ratesListSettingsDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.currencyapp.R
import com.example.currencyapp.databinding.AlertDialogRatesTabSettingsBinding
import com.example.currencyapp.ui.ratesList.RatesListViewModel
import com.example.currencyapp.ui.ratesList.model.RatesListSettings

class RatesSettingsDialog : DialogFragment() {

    private lateinit var _binding: AlertDialogRatesTabSettingsBinding
    private val binding get() = _binding

    private val viewModel: RatesListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    lateinit var baseCurrencyOptions: Array<String>
    lateinit var precisionOptions: List<Int>

    private lateinit var settings: RatesListSettings

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlertDialogRatesTabSettingsBinding.inflate(inflater, container, false)

        settings = arguments?.get(SETTINGS_ARGUMENT) as RatesListSettings
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseCurrencyOptions = resources.getStringArray(R.array.currency_three_letter_codes)
        precisionOptions = resources.getStringArray(R.array.precision_options).map { it.toInt() }

        setupSettingsSpinners()
        setupButtons()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .also { it.window?.requestFeature(Window.FEATURE_NO_TITLE) }
    }

    private fun setupSettingsSpinners() {
        with(binding) {
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.currency_three_letter_codes,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerBaseCurrency.adapter = arrayAdapter
            }

            spinnerBaseCurrency.setSelection(
                baseCurrencyOptions.indexOf(
                    settings.currencyCode
                )
            )


            spinnerRateDifferencePrecision.apply {
                ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.precision_options,
                    android.R.layout.simple_spinner_item
                ).also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    this.adapter = arrayAdapter
                }
            }

            spinnerRateDifferencePrecision.setSelection(
                precisionOptions.indexOf(
                    settings.precision
                )
            )
        }
    }

    private fun setupButtons() {
        with(binding) {
            btnCancelRatesSettings.setOnClickListener {
                dismiss()
            }
            btnApplyRatesSettings.setOnClickListener {
                viewModel.updateRatesListSettings(
                    RatesListSettings(
                        baseCurrencyOptions[spinnerBaseCurrency.selectedItemPosition],
                        precisionOptions[spinnerRateDifferencePrecision.selectedItemPosition]
                    )
                )
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "SettingsDialog"
        private const val SETTINGS_ARGUMENT = "Settings"
        fun newInstance(
            settings: RatesListSettings?
        ): RatesSettingsDialog {
            return RatesSettingsDialog().also {
                it.arguments = bundleOf(SETTINGS_ARGUMENT to settings)
            }
        }
    }
}