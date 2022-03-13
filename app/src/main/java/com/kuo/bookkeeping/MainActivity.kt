package com.kuo.bookkeeping

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.kuo.bookkeeping.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainViewModel by viewModels()

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_container)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupNavigation()
        setupListener()
        setupObserver()
    }

    private fun setupView() {
        binding.appBarMain.fabAddRecord.apply {
            setShowMotionSpecResource(R.animator.fab_show)
            setHideMotionSpecResource(R.animator.fab_hide)
        }
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.appBarMain.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment

        navController = navHostFragment.navController
        setupWithNavController(binding.appBarMain.bottomNav, navController)

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.bookkeepingFragment, R.id.analyzeFragment),
            drawerLayout = binding.drawer
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun setupListener() {
        binding.appBarMain.fabAddRecord.setOnClickListener {
            currentNavigationFragment?.apply {
                exitTransition = MaterialElevationScale(false).apply {
                    duration = resources.getInteger(R.integer.motion_duration).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = resources.getInteger(R.integer.motion_duration).toLong()
                }
            }
            navController.navigate(R.id.action_bookkeepingFragment_to_saveRecordFragment)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.bookkeepingFragment) {
                viewModel.setShowAddFab(true)
            } else {
                viewModel.setShowAddFab(false)
            }
            showNavBar()
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.isShowAddFab) {
                        binding.appBarMain.fabAddRecord.show()
                    } else {
                        binding.appBarMain.fabAddRecord.hide()
                    }
                }
            }
        }
    }

    fun hideNavBar() {
        binding.appBarMain.bottomNav.animate()
            .translationY(-(binding.appBarMain.bottomNav.height.toFloat() / 2))
            .setStartDelay(resources.getInteger(R.integer.motion_nav_bar_to_keyboard_start_delay).toLong())
            .setDuration(resources.getInteger(R.integer.motion_nav_bar_to_keyboard_duration).toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    binding.appBarMain.bottomNav.visibility = View.GONE
                }
            })
    }

    fun showNavBar() {
        binding.appBarMain.bottomNav.animate()
            .translationY(0f)
            .setStartDelay(resources.getInteger(R.integer.motion_nav_bar_from_keyboard_start_delay).toLong())
            .setDuration(resources.getInteger(R.integer.motion_nav_bar_from_keyboard_duration).toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    binding.appBarMain.bottomNav.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}