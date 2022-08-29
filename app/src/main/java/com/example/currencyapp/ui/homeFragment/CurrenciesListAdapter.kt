package com.example.currencyapp.ui.homeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.databinding.CurrenciesListItemBinding
import com.example.currencyapp.model.CurrencyFluctuation

private const val ROUNDING_FORMAT: String = "%.3f"

class CurrenciesListAdapter : RecyclerView.Adapter<CurrenciesListAdapter.CurrencyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<CurrencyFluctuation>() {

        override fun areItemsTheSame(
            oldItem: CurrencyFluctuation,
            newItem: CurrencyFluctuation
        ): Boolean {
            return oldItem.iso4217Alpha == newItem.iso4217Alpha
        }

        override fun areContentsTheSame(
            oldItem: CurrencyFluctuation,
            newItem: CurrencyFluctuation
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var currenciesList: List<CurrencyFluctuation>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(
            CurrenciesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) =
        holder.bind(currenciesList[position], position)

    override fun getItemCount(): Int = currenciesList.size

    inner class CurrencyViewHolder(binding: CurrenciesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvFullName = binding.tvcurrencyFullName
        private val tvCode = binding.tvCurrencyCode
        private val tvRate = binding.tvRate
        private val tvDifference = binding.tvDifference
        private val indicatorImage = binding.imageViewArrowIndicator
        private val container = binding.itemContainer

        fun bind(item: CurrencyFluctuation, position: Int) {
            tvFullName.text = item.fullName
            tvCode.text = item.iso4217Alpha
            tvRate.text = String.format(ROUNDING_FORMAT, item.rate)
            tvDifference.text = String.format(ROUNDING_FORMAT, item.rateDifference)

            indicatorImage.setImageResource(
                if (item.rateDifference >= 0) {
                    R.drawable.increase
                } else {
                    R.drawable.decrease
                }
            )
            container.setBackgroundColor(
                container.resources.getColor(
                    if (position % 2 != 1) {
                        R.color.grey
                    } else {
                        R.color.white
                    }
                )
            )
        }
    }
}