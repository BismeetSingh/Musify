package com.app.bissudroid.musify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.app.bissudroid.musify.fragments.*
import com.app.bissudroid.musify.utils.Constants.Companion.MY_PERMISSIONS_REQUEST_READ_MEDIA
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.setupWithViewPager(container)





    }



    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when(position){
                0 -> return SongsFragment()
                1 -> return Albums()
                2 -> return ArtistsFragment()
                3 -> return Folders()
                4 -> return  PlaylistFragment()
                5 -> return GenresFragment()
                else ->
                    return SongsFragment()

            }



        }

        override fun getCount(): Int {

            return 6
        }

        override fun getPageTitle(position: Int): CharSequence? {
           when(position)
           {
               0 -> return "Songs"
               1 -> return "Albums"
               2 -> return "Artists"
               3 -> return "Folders"
               4 -> return  "Playlists"
               5 -> return "Genres"
           }
            return null
        }

    }




}
