package com.example.currencyapp.ui.newsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.currencyapp.TAG
import com.example.currencyapp.databinding.FragmentNewsBinding
import com.example.currencyapp.ui.newsFragment.newsListFragment.NewsListFragment
import com.example.currencyapp.ui.newsFragment.searchSettingsFragment.SearchSettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    companion object {
        fun newInstance() = NewsFragment()
    }

    private val viewModel: NewsViewModel by activityViewModels()
    private val args: NewsFragmentArgs by navArgs()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this.requireActivity())
    }


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            Log.d(TAG, "createFragment: $position")
            return when(position){
                0 -> SearchSettingsFragment.newInstance()
                1 -> NewsListFragment.newInstance()
                else -> SearchSettingsFragment.newInstance()
            }
        }
    }
}