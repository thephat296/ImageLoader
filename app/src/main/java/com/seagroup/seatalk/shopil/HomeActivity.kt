package com.seagroup.seatalk.shopil

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.seagroup.seatalk.shopil.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listAdapter: ImageListAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listAdapter = ImageListAdapter()
        with(binding.rvImages) {
            adapter = listAdapter
            layoutManager = StaggeredGridLayoutManager(ImageListAdapter.NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
        }
        binding.btnLoadImages.setOnClickListener {
            viewModel.loadImages().observe(this, listAdapter::submitList)
        }
    }
}