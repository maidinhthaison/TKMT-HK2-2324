package com.mdts.eieapp.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream
import java.util.regex.Pattern.compile

@GlideModule
class GlideUtils : AppGlideModule() {
    /**
     * Setting memory cache size 10M
     */
    private val cacheSize:Long = 10485760 //10*1024*1024
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(cacheSize))
    }
    /**
     * Register a BaseGlideUrlLoader of String type
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            String::class.java, InputStream::class.java,
            CustomBaseGlideUrlLoader.factory
        )
    }

    /**
     * Close parsing Android Manifest
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     *
     */
    internal class CustomBaseGlideUrlLoader(
        concreteLoader: ModelLoader<GlideUrl, InputStream>,
        modelCache: ModelCache<String, GlideUrl>
    ): BaseGlideUrlLoader<String>(concreteLoader, modelCache){
        /**
         * Url The matching rules of 6550
         */
        private val pattern= compile(/* regex = */ "__w-((?:-?\\d+)+)__")
        /**
         * Control the size of the image to be loaded
         */
        override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
            val m=pattern.matcher(model)
            var bestBucket=0
            if (m.find()){
                val found= m.group(1)?.split("-")
                if (found != null) {
                    for (item in found){
                        bestBucket=item.toInt()
                        if (bestBucket>=width) break
                    }
                }
            }
            return model
        }

        override fun handles(model: String): Boolean {
            return true
        }

        companion object{
            val urlCache= ModelCache<String, GlideUrl>(150)
            /**
             * The default is private and exposed to Java calls through the @JvmField annotation
             */
            @JvmField
            val factory=object: ModelLoaderFactory<String, InputStream> {
                override fun build(multiFactory: MultiModelLoaderFactory)
                        : ModelLoader<String, InputStream> {

                    return CustomBaseGlideUrlLoader(
                        multiFactory.build(GlideUrl::class.java, InputStream::class.java), urlCache
                    )
                }
                override fun teardown() {
                }
            }
        }
    }

    /*fun loadImageFitCenter(context: Context, url: String?, imageView: AppCompatImageView) {
        val options: RequestOptions = RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
        GlideApp.with(context)
            .load(url)
            .transform(FitCenter())
            .apply(options)
            .transition(DrawableTransitionOptions.withCrossFade()).into(imageView)

    }

    fun loadImageCenterCrop(context: Context, url: String?, imageView: AppCompatImageView) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
        GlideApp.with(context)
            .load(url)
            .transform(CenterCrop())
            .apply(options)
            .transition(DrawableTransitionOptions.withCrossFade()).into(imageView)

    }*/
}