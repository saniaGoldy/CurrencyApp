package com.example.currencyapp.ui.homeFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.databinding.CurrenciesListItemBinding
import com.example.currencyapp.domain.model.CurrencyData

private const val ROUNDING_FORMAT: String = "%.3f"

class CurrenciesListAdapter(val onListItemClickedActionCallback: ItemClickedAction) :
    RecyclerView.Adapter<CurrenciesListAdapter.CurrencyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<CurrencyData>() {

        override fun areItemsTheSame(
            oldItem: CurrencyData,
            newItem: CurrencyData
        ): Boolean {
            return oldItem.iso4217Alpha == newItem.iso4217Alpha
        }

        override fun areContentsTheSame(
            oldItem: CurrencyData,
            newItem: CurrencyData
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var currenciesList: List<CurrencyData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(
            CurrenciesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) =
        holder.bind(currenciesList[position])

    override fun getItemCount(): Int = currenciesList.size

    inner class CurrencyViewHolder(binding: CurrenciesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvFullName = binding.tvcurrencyFullName
        private val tvCode = binding.tvCurrencyCode
        private val tvRate = binding.tvRate
        private val tvDifference = binding.tvDifference
        private val indicatorImage = binding.imageViewArrowIndicator
        private val container = binding.itemContainer

        @SuppressLint("SetTextI18n")
        fun bind(item: CurrencyData) {
            tvFullName.text = item.fullName
            tvCode.text = item.iso4217Alpha
            tvRate.text = String.format(ROUNDING_FORMAT, item.rate)

            val rateDiff = item.getRateDifference()
            val rateDiffText = String.format(ROUNDING_FORMAT, rateDiff)

            if (rateDiff >= 0){
                tvDifference.text = "+$rateDiffText"
                indicatorImage.setImageResource(R.drawable.increase)
            }else{
                R.drawable.decrease
            }

            container.setOnClickListener {
                onListItemClickedActionCallback.run(item)
            }
        }
    }

    interface ItemClickedAction {
        fun run(currencyData: CurrencyData)
    }
}