package kr.go.mapo.intime.setting

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.go.mapo.intime.setting.fragment.FavoriteAedFragment
import kr.go.mapo.intime.setting.fragment.FavoriteAllFragment
import kr.go.mapo.intime.setting.fragment.FavoriteShelterFragment

class FavoriteCollectionAdapter(settingFavoriteActivity: SettingFavoriteActivity) :
    FragmentStateAdapter(settingFavoriteActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteAllFragment()
            1 -> FavoriteAedFragment()
            else -> FavoriteShelterFragment()
        }
    }
}