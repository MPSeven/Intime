package kr.go.mapo.intime.setting

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.go.mapo.intime.setting.fragment.*

class FavoriteCollectionAdapter(settingFavoriteActivity: SettingFavoriteActivity) :
    FragmentStateAdapter(settingFavoriteActivity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteAllFragment()
            1 -> FavoriteAedFragment()
            2 -> FavoriteShelterFragment()
            3 -> FavoriteEmergencyFragment()
            else -> FavoritePharmacyFragment()
        }
    }
}