package com.bahaso.bahaso.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.domain.Topic
import com.bahaso.bahaso.databinding.ItemLearningTopicBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class LearningTopicViewHolder(
    private val binding: ItemLearningTopicBinding,
    private val onItemTopicClicked: ((Topic) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: Topic) {
        with(binding) {
            val requestOptions = RequestOptions().placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_baseline_broken_image_24)

            Glide.with(itemView).setDefaultRequestOptions(requestOptions)
                .load(item.imageUrl).circleCrop().into(imgTopic)

            tvTopicTitle.text = item.topic
            tvTopicDesc.text = item.description

            root.setOnClickListener {
                onItemTopicClicked?.invoke(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemTopicClicked: ((Topic) -> Unit)?,
        ): LearningTopicViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_learning_topic, parent, false)
            val binding = ItemLearningTopicBinding.bind(view)
            return LearningTopicViewHolder(binding, onItemTopicClicked)
        }
    }
}