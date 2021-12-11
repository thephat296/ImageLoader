package com.vtp.imageloader.demo

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vtp.imageloader.demo.databinding.ActivityHomeBinding
import com.vtplib.imageloader.util.imageLoader
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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
            layoutManager = StaggeredGridLayoutManager(ImageListAdapter.NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(ImageItemDecoration(this@HomeActivity))
        }
        binding.btnLoadImages.setOnClickListener {
            viewModel.loadImages().observe(this) {
                trackLastRenderTime()
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

    private fun trackLastRenderTime() {
        val start = System.currentTimeMillis()
        findViewById<ViewGroup>(android.R.id.content).viewTreeObserver.addOnDrawListener {
            val end = System.currentTimeMillis() - start
            binding.tvLastDraw.text = TimeUnit.MILLISECONDS.toSeconds(end).toString()
        }
    }
}
