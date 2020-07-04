package com.example.papipel.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.papipel.Fragments.CreateOrderFragment
import com.example.papipel.Fragments.OrderProductsFragment

class MainPageAdapter(fragment: FragmentManager): FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    lateinit var createOrderFragment: CreateOrderFragment
    lateinit var orderProductsFragment: OrderProductsFragment

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            if (!this::createOrderFragment.isInitialized) {
                createOrderFragment = CreateOrderFragment()
            }
            return createOrderFragment
        } else {
            if (!this::orderProductsFragment.isInitialized) {
                orderProductsFragment = OrderProductsFragment()
            }
            return orderProductsFragment
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return "Adicionar itens"
        } else {
            return "Ver pedido"
        }
    }
}