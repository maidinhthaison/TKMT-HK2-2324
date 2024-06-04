package com.mdts.eieapp.presentation.bottomsheet.adapter

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdts.eieapp.R
import com.mdts.eieapp.databinding.ViewHolderPhoneticItemBinding
import com.mdts.eieapp.domain.model.PhoneticsModel


internal class PhoneticsAdapter (
    private var context: Context,
) : ListAdapter<PhoneticsModel, PhoneticsAdapter.PhoneticsItemViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhoneticsAdapter.PhoneticsItemViewHolder {
        val binding = ViewHolderPhoneticItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return PhoneticsItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PhoneticsAdapter.PhoneticsItemViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        item?.let {
            holder.bind(item, position)
        }
    }

    internal inner class PhoneticsItemViewHolder(private val binding: ViewHolderPhoneticItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var mediaPlayer: MediaPlayer = MediaPlayer()
        fun bind(item: PhoneticsModel, position: Int) {
            binding.tvText.text = item.text
            binding.tvSourceUrl.text = HtmlCompat.fromHtml(String.format(context.getString(R.string.bottom_sheet_source_url_label), item.sourceUrl?: ""),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.imvSpeak.isVisible = !item.audio.isNullOrEmpty()
            binding.tvLicense.text = HtmlCompat.fromHtml(String.format(context.getString(R.string.bottom_sheet_source_license_label), item.license?.name ?: ""
            ), HtmlCompat.FROM_HTML_MODE_LEGACY)

            //speak
            binding.imvSpeak.setOnClickListener {
                if(!item.audio.isNullOrEmpty()){
                    playFromUrl(item.audio){
                        prepareAsync()
                    }

                }
            }
        }
//https://stackoverflow.com/questions/5974392/how-to-play-audio-file-from-url-in-android
        private fun playFromUrl(
            url: String,
            onStart: MediaPlayer.() -> Unit
        ) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            MediaPlayer().apply {
                setAudioAttributes(audioAttributes)
                setDataSource(url)

                setOnPreparedListener {
                    //isEnabled = false
                    start()
                    //setImageDrawable(context.getDrawableResource(R.drawable.ic_baseline_stop_24))
                }

                setOnCompletionListener {
                    //setImageDrawable(context.getDrawableResource(R.drawable.ic_baseline_volume_up_24))
                    release()
                    //isEnabled = true
                }
            }.onStart()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhoneticsModel>() {
            override fun areItemsTheSame(oldItem: PhoneticsModel, newItem: PhoneticsModel): Boolean {
                return oldItem.text == newItem.text
            }

            override fun areContentsTheSame(oldItem: PhoneticsModel, newItem: PhoneticsModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
