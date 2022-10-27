package com.example.currencyapp.ui.ratesList.ratesListFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.databinding.CurrenciesListItemBinding
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.ui.model.MyOnQueryTextListener

class CurrenciesListAdapter(val onListItemClickedActionCallback: ItemClickedAction) :
    RecyclerView.Adapter<CurrenciesListAdapter.CurrencyViewHolder>() {

    private var roundingFormat = "%.3f"

    fun setRoundingFormat(precision: Int) {
        roundingFormat = roundingFormat.replaceFirst(Regex("\\d"), precision.toString())
        notifyItemRangeChanged(0, currenciesList.size)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CurrencyData>() {

        override fun areItemsTheSame(
            oldItem: CurrencyData,
            newItem: CurrencyData
        ): Boolean {
            return oldItem.currency == newItem.currency
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

    fun getOnQueryTextListener(cashedListState: DataState<List<CurrencyData>>?): SearchView.OnQueryTextListener {
        return MyOnQueryTextListener(object : MyOnQueryTextListener.QueryFilter {
            override fun filter(keyword: String?) {
                currenciesList = RatesFilter(cashedListState).filter(keyword)
            }
        })
    }

    inner class CurrencyViewHolder(binding: CurrenciesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvFullName = binding.tvcurrencyFullName
        private val tvCode = binding.tvCurrencyCode
        private val tvRate = binding.tvRate
        private val tvDifference = binding.tvDifference
        private val indicatorImage = binding.imageViewArrowIndicator
        private val container = binding.itemContainer

        init {
            container.setOnClickListener {
                onListItemClickedActionCallback.run(currenciesList[bindingAdapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: CurrencyData) {
            tvFullName.text = item.currency.fullName
            tvCode.text = item.currency.name
            tvRate.text = String.format(roundingFormat, item.rate)

            val rateDiff = item.getRateDifference()
            val rateDiffText = String.format(roundingFormat, rateDiff)

            if (rateDiff >= 0) {
                tvDifference.text = "+$rateDiffText"
                indicatorImage.setImageResource(R.drawable.increase)
            } else {
                tvDifference.text = rateDiffText
                R.drawable.decrease
            }
        }
    }

    interface ItemClickedAction {
        fun run(currencyData: CurrencyData)
    }
}