package com.budget.tracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.budget.tracker.fragments.*
import com.budget.tracker.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    HomeFragment.OnFragmentInteractionListener,
    ExpensesFragment.OnFragmentInteractionListener,
    IncomesFragment.OnFragmentInteractionListener,
    CategoryListFragment.OnFragmentInteractionListener,
    GoalListFragment.OnFragmentInteractionListener {
    private lateinit var homeFragment: HomeFragment
    private lateinit var goalListFragment: GoalListFragment
    private lateinit var expensesCategoriesFragment: CategoryListFragment
    private lateinit var incomesCategoriesFragment: CategoryListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        homeFragment = HomeFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, homeFragment)
            .commit()

        nav_view.setCheckedItem(R.id.sidebar_home)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                SharedPrefManager.getInstance(applicationContext).clear()

                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sidebar_home -> {
                homeFragment = HomeFragment.newInstance()
                switchFragment(homeFragment)
            }
            R.id.sidebar_expenses -> {
                expensesCategoriesFragment = CategoryListFragment.newInstance(0)
                switchFragment(expensesCategoriesFragment)
            }
            R.id.sidebar_incomes -> {
                incomesCategoriesFragment = CategoryListFragment.newInstance(1)
                switchFragment(incomesCategoriesFragment)
            }
            R.id.sidebar_goals -> {
                goalListFragment = GoalListFragment.newInstance()
                switchFragment(goalListFragment)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {}

    fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(fragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
