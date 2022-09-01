package com.example.currencyapp.ui.newsFragment.newsListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.databinding.NewsListItemBinding


class NewsListAdapter :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var newsList: List<Data>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(
            NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) =
        holder.bind(newsList[position], position)

    override fun getItemCount(): Int = newsList.size

    inner class NewsViewHolder(private val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data, position: Int) {
            with(binding) {
                tvTitle.text = item.title
                tvSource.text = item.source
                tvTimeStamp.text = item.publishedAt
                tvTags.text = item.tags.toString()
            }
        }
    }
}