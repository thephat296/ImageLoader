package com.seagroup.seatalk.shopil

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.seagroup.seatalk.shopil.databinding.ActivityHomeBinding
import com.seagroup.seatalk.shopil.util.imageLoader
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listAdapter: ImageListAdapter
    private lateinit var glideListAdapter: GlideImageListAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listAdapter = ImageListAdapter(this)
        glideListAdapter = GlideImageListAdapter(this)

        with(binding.rvImages) {
            adapter = listAdapter
            layoutManager = StaggeredGridLayoutManager(ImageListAdapter.NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(ImageItemDecoration(this@HomeActivity))
        }
        binding.tvSwitchEngine.tag = binding.rvImages.adapter
        binding.tvSwitchEngine.text = binding.rvImages.adapter.toString()

        binding.btnLoadImages.setOnClickListener {
            val adapter = binding.tvSwitchEngine.tag as ImageListAdapter
            viewModel.loadImages().observe(this) {
                trackLastRenderTime()
                adapter.submitList(it)
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
        binding.tvSwitchEngine.setOnClickListener {
            swapEngine()
        }
    }

    private fun trackLastRenderTime() {
        val start = System.currentTimeMillis()
        findViewById<ViewGroup>(android.R.id.content).viewTreeObserver.addOnDrawListener {
            val end = System.currentTimeMillis() - start
            binding.tvLastDraw.text = TimeUnit.MILLISECONDS.toSeconds(end).toString()
        }
    }

    private fun swapEngine() {
        val adapter = if (binding.tvSwitchEngine.tag is GlideImageListAdapter) listAdapter else glideListAdapter
        adapter.submitList(emptyList())
        binding.rvImages.swapAdapter(adapter, true)
        binding.tvSwitchEngine.text = adapter.toString()
        binding.tvSwitchEngine.tag = adapter
    }
}
