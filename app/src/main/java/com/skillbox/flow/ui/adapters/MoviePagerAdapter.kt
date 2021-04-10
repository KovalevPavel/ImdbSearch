package com.skillbox.flow.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
/*
Класс-адаптер для ViewPager

fragment: Fragment - фрагмент, внутри которого расположен ViewPager

fragmentList: List<Pair<Fragment, String>> - список фрагментов для отображения
Здесь Fragment - экземпляр класса фрагмента, который необходимо отобразить
String - заголовок вкладки для TabLayout
 */
class MoviePagerAdapter(
    fragment: Fragment,
    private val fragmentList: List<Pair<Fragment, String>>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].first
    }
}
