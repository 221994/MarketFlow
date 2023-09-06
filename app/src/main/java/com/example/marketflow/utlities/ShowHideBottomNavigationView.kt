package com.example.marketflow.utlities

import android.view.View
import androidx.fragment.app.Fragment
import com.example.marketflow.R
import com.example.marketflow.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation() {
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNavigation() {
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNavigationView.visibility = View.VISIBLE
}
