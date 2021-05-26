package com.vtp.imageloader.demo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageLoader: ImageLoader = ImageLoaderImpl(
            applicationContext,
            DefaultMemoryCache(MemoryUtils.calculateAvailableMemorySize(applicationContext, 0.25))
        )
        findViewById<Button>(R.id.btn_load_image).setOnClickListener {
            loadImage(imageLoader)
        }
    }

    private fun loadImage(imageLoader: ImageLoader) {
        val imgUrl = "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"
        imageLoader.enqueue(ImageRequest(imgUrl, findViewById(R.id.img_test)))
    }
}
