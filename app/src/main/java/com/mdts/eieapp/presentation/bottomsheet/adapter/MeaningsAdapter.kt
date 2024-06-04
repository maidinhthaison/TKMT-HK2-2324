package com.mdts.eieapp.presentation.bottomsheet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdts.eieapp.R
import com.mdts.eieapp.databinding.ViewHolderMeaningItemBinding
import com.mdts.eieapp.domain.model.MeaningModel
import timber.log.Timber

internal class MeaningsAdapter  (
    private var context: Context,
) : ListAdapter<MeaningModel, MeaningsAdapter.MeaningsItemViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MeaningsAdapter.MeaningsItemViewHolder {
        val binding = ViewHolderMeaningItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return MeaningsItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MeaningsAdapter.MeaningsItemViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        item?.let {
            holder.bind(item, position)
        }
    }

    internal inner class MeaningsItemViewHolder(private val binding: ViewHolderMeaningItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MeaningModel, position: Int) {
            binding.tvPartOfSpeech.text = "Type: ${item.partOfSpeech}"
            var definition  = ""
            item.definitions?.forEach {
                definition += "\u2022 ${it.definition}\nExample: ${it.example}\n\n"
            }
            binding.tvDefinition.text =  definition

            var synonyms  = ""
            item.synonyms?.forEach {
                synonyms += "\u2022 ${it}\n"
            }
            binding.tvSynonyms.text = HtmlCompat.fromHtml(String.format(context.getString(R.string.bottom_sheet_synonyms_label), synonyms),
                HtmlCompat.FROM_HTML_MODE_LEGACY)

            var antonyms  = ""
            item.antonyms?.forEach {
                antonyms += "\u2022 ${it}\n"
            }
            binding.tvAntonyms.text = HtmlCompat.fromHtml(String.format(context.getString(R.string.bottom_sheet_antonyms_label), antonyms),
                HtmlCompat.FROM_HTML_MODE_LEGACY)


        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MeaningModel>() {
            override fun areItemsTheSame(oldItem: MeaningModel, newItem: MeaningModel): Boolean {
                return oldItem.partOfSpeech == newItem.partOfSpeech
            }

            override fun areContentsTheSame(oldItem: MeaningModel, newItem: MeaningModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
