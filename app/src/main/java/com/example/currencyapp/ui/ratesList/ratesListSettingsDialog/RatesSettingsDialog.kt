package com.example.currencyapp.ui.ratesList.ratesListSettingsDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.currencyapp.R
import com.example.currencyapp.databinding.AlertDialogRatesTabSettingsBinding
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.ui.ratesList.model.RatesListSettings

class RatesSettingsDialog : DialogFragment() {

    private lateinit var _binding: AlertDialogRatesTabSettingsBinding
    private val binding get() = _binding

    lateinit var baseCurrencyOptions: Array<String>
    lateinit var precisionOptions: List<Int>

    private lateinit var processor: SettingsDataProcessor
    private lateinit var settings: RatesListSettings

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlertDialogRatesTabSettingsBinding.inflate(inflater, container, false)
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
                processor.process(
                    RatesListSettings(
                        baseCurrencyOptions[spinnerBaseCurrency.selectedItemPosition],
                        precisionOptions[spinnerRateDifferencePrecision.selectedItemPosition]
                    )
                )
                dismiss()
            }
        }
    }

    interface SettingsDataProcessor {
        fun process(settings: RatesListSettings)
    }


    companion object {
        const val TAG = "SettingsDialog"
        fun newInstance(
            processor: SettingsDataProcessor,
            settings: RatesListSettings
        ): RatesSettingsDialog {
            return RatesSettingsDialog().also {
                it.processor = processor
                it.settings = settings
            }
        }
    }
}