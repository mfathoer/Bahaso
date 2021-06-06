package com.bahaso.bahaso.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bahaso.bahaso.core.domain.Topic

class LearningTopicListAdapter :
    ListAdapter<Topic, LearningTopicViewHolder>(
        TOPIC_COMPARATOR) {

    var onItemClicked: ((Topic) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearningTopicViewHolder {
        return LearningTopicViewHolder.create(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: LearningTopicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val TOPIC_COMPARATOR = object : DiffUtil.ItemCallback<Topic>() {
            override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return newItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return newItem == oldItem
            }
        }
    }
}