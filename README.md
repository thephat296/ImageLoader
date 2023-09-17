## What is this
An Image Loader library to fetch, decode, and display image in your app.

## Key Features
- Load images from the network, local storage and display inside ImageView.
- Using a memory and disk bitmap cache to improve the responsiveness and fluidity of the UI
- Support placeholder and error displays
- Able to handle multiple loading scenarios such as in RecyclerView
- Support custom transformations added by user

## Usage
- ensure Internet and Storage permissions are granted before using this library
- Supported sources: Url, Uri, File
```kotlin
    imageView.load(source) {
        placeholder(placeholderDrawable)
        error(errorDrawable)
        transformations(transformations)
        memoryCachePolicy(cachePolicy)
        diskCachePolicy(cachePolicy)
    }    
```
```kotlin
    enum class CachePolicy {
        DISABLED,
        ENABLED
    }
```

## Updating
- Use BitmapPool to avoid frequently GC
- Improve disk cache: support limit size, LRU eviction strategy
