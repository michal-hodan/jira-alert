package com.github.michalhodan.jiraalert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private var url: String? = null

    private var username: String? = null

    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Set up the ViewPager with the sections adapter.
        container.adapter = SectionsPagerAdapter(supportFragmentManager)

        val preferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)

        val url = preferences.getString("url", null)
        val authToken = preferences.getString("authToken", null)

        if (url != null && authToken != null) {
            startActivity(Intent(this, UserActivity::class.java))
        }
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int) = when(position) {
            0 ->  WelcomeIntroductionFragment.newInstance()
            1 -> WelcomeExplanationFragment.newInstance { url = it
            Toast.makeText(baseContext, "url: $url", Toast.LENGTH_SHORT).show()}
            else -> WelcomeLoginFragment.newInstance ( { url!! }) { u, p ->
                username = u
                password = p
            }
        }

        override fun getCount() = 3
    }
}
