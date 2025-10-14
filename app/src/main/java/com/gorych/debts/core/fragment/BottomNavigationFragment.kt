package com.gorych.debts.core.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gorych.debts.R
import com.gorych.debts.good.ui.list.GoodListActivity
import com.gorych.debts.home.MainActivity
import com.gorych.debts.purchaser.ui.list.ClientListActivity

class BottomNavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false)

        val bottomNavView = view.findViewById<BottomNavigationView>(R.id.nav_bottom)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home ->
                    startActivityIfNotStarted(MainActivity::class.java)

                R.id.navigation_clients ->
                    startActivityIfNotStarted(ClientListActivity::class.java)

                R.id.navigation_goods ->
                    startActivityIfNotStarted(GoodListActivity::class.java)

                else -> false
            }
        }

        return view
    }

    private fun startActivityIfNotStarted(
        activeActivityClass: Class<out AppCompatActivity>,
    ): Boolean {
        val context = requireContext()

        if (context.javaClass != activeActivityClass) {
            context.startActivity(Intent(context, activeActivityClass))
        }
        return true
    }
}