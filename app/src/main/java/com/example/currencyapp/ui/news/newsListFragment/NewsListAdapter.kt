package com.example.currencyapp.ui.news.newsListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.databinding.NewsListItemBinding
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.ui.model.MyOnQueryTextListener

/** @param [onClickListener] string parameter of listener is url*/
class NewsListAdapter(private val onClickListener: ItemOnClickListener) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<NewsData>() {

        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var newsList: List<NewsData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(
            NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) =
        holder.bind(newsList[position])

    override fun getItemCount(): Int = newsList.size

    fun getOnQueryTextListener(cashedListState: DataState<List<NewsData>>?): SearchView.OnQueryTextListener {
        return MyOnQueryTextListener(object : MyOnQueryTextListener.QueryFilter {
            override fun filter(keyword: String?) {
                newsList = NewsFilter(cashedListState).filter(keyword)
            }
        })
    }

    inner class NewsViewHolder(private val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemContainer.setOnClickListener {
                onClickListener.invoke(newsList[bindingAdapterPosition].url)
            }
        }

        fun bind(item: NewsData) {
            with(binding) {
                tvTitle.text = item.title
                tvSource.text = item.source

                tvPublicationDate.text = item.publishedAt.publishDate
                tvPublicationTime.text = item.publishedAt.publishTime

                tvTags.text = item.getTagsAsString()

                itemContainer.setOnClickListener {
                    root.findNavController().navigate(
                        NewsListFragmentDirections.actionNavigationNewsToWebViewActivity(item.url)
                    )
                }
            }
        }
    }

    interface ItemOnClickListener {
        fun invoke(url: String)
    }
}