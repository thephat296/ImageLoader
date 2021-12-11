package com.vtp.imageloader.demo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vtp.imageloader.demo.databinding.ActivityHomeBinding
import com.vtplib.imageloader.util.imageLoader
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listAdapter: ImageListAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listAdapter = ImageListAdapter(this)

        with(binding.rvImages) {
            adapter = listAdapter
            layoutManager = StaggeredGridLayoutManager(
                ImageListAdapter.NUM_COLUMNS,
                StaggeredGridLayoutManager.VERTICAL
            )
            addItemDecoration(ImageItemDecoration(this@HomeActivity))
        }
        binding.btnLoadImages.setOnClickListener {
            viewModel.loadImages().observe(this) {
                listAdapter.submitList(it)
            }
        }
        binding.btnClearMemoryCache.setOnClickListener {
            imageLoader.clearMemoryCache()
        }
        binding.btnClearDiskCache.setOnClickListener {
            lifecycleScope.launch {
                imageLoader.clearDiskCache()
            }
        }
    }
}
