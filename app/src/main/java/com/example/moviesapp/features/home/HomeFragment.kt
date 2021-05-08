package com.example.moviesapp.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val fragments = mapOf(
        Pair("Recent", NowPlayingMoviesFragment()),
        Pair("Popular", PopularMoviesFragment()),
        Pair("Top", TopRatedMoviesFragment()),
        Pair("Upcoming", UpcomingMoviesFragment())
    )

    private val adapter by lazy {
        activity?.let { HomePagerAdapter(fragments.values.toList(), it) }
    }

    private val tabLayoutMediator by lazy {
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = fragments.keys.toList()[position]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeViewPager.adapter = adapter
        tabLayoutMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }

}

private class HomePagerAdapter(private val fragments: List<Fragment>, fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}