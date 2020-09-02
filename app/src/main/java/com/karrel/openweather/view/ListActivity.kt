package com.karrel.openweather.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.karrel.openweather.R
import com.karrel.openweather.base.BaseActivity
import com.karrel.openweather.databinding.ActivityWeatherListBinding
import com.karrel.openweather.view.adapter.CurrentListAdapter
import com.karrel.openweather.viewmodel.CurrentViewModel
import kotlinx.android.synthetic.main.activity_weather_list.*
import kotlinx.android.synthetic.main.content_weather_list.*

class ListActivity : BaseActivity() {

    private lateinit var binding: ActivityWeatherListBinding

    private val viewModel by lazy { ViewModelProviders.of(this).get(CurrentViewModel::class.java) }

    private val listAdapter: CurrentListAdapter by lazy {
        CurrentListAdapter { cityId, cityName ->
            DetailActivity.startActivity(this, cityId, cityName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_list)
        binding.viewmodel = viewModel

        setSupportActionBar(toolbar)

        setupRecyclerView()
        observeData()

        startIntro()
        viewModel.loadWeatherListData()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.weatherList.swiperRefresh.apply {
            setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent
            )

            setOnRefreshListener {
                viewModel.loadWeatherListData()
            }
        }
    }

    private fun startIntro() {
        startActivity(Intent(this, IntroActivity::class.java))
        overridePendingTransition(0, 0)
    }

    private fun observeData() {
        viewModel.apply {
            currentList.observe(this@ListActivity, Observer {
                listAdapter.setData(it)
            })
        }
    }

    private fun setupRecyclerView() {
        recyclerWeather.apply {
            adapter = listAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun showProgressDialog() {
        binding.weatherList.imageProgress.visibility = View.VISIBLE
    }

    override fun hideProgressDialog() {
        binding.weatherList.swiperRefresh.isRefreshing = false
    }
}
