package com.example.marketflow.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marketflow.adapters.HomeFragmentViewPagerAdapter
import com.example.marketflow.databinding.FragmentHomeBinding
import com.example.marketflow.fragments.catrogries.AccessoryFragment
import com.example.marketflow.fragments.catrogries.ChairFragment
import com.example.marketflow.fragments.catrogries.CupboardFragment
import com.example.marketflow.fragments.catrogries.FurnitureFragment
import com.example.marketflow.fragments.catrogries.MainCategoryFragment
import com.example.marketflow.fragments.catrogries.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragments = arrayListOf(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            FurnitureFragment(),
            AccessoryFragment()
        )
        binding.viewPagerHomeFragment.isUserInputEnabled = false
        val viewPager2 =
            HomeFragmentViewPagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewPagerHomeFragment.adapter = viewPager2
        TabLayoutMediator(
            binding.tabLayOutHomeFragment, binding.viewPagerHomeFragment
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Furniture"
                5 -> tab.text = "Accessory"
            }
        }.attach()
    }
}