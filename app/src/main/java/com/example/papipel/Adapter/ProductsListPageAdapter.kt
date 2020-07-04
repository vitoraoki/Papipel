package com.example.papipel.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.papipel.Fragments.AllProductsFragment
import com.example.papipel.Fragments.ProductsByCategoryFragment

class ProductsListPageAdapter(fragment: FragmentManager): FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    lateinit var allProductsFragment: AllProductsFragment
    lateinit var productsByCategoryFragment: ProductsByCategoryFragment

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            if (!this::allProductsFragment.isInitialized) {
                allProductsFragment = AllProductsFragment()
            }
            return allProductsFragment
        } else {
            if (!this::productsByCategoryFragment.isInitialized) {
                productsByCategoryFragment = ProductsByCategoryFragment()
            }
            return productsByCategoryFragment
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return "Lista de produtos"
        } else {
            return "Produtos por categoria"
        }
    }
}