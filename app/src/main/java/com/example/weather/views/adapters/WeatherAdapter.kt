package com.example.weather.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.TotalWeather
import com.example.weather.databinding.HeaderWeatherBinding
import com.example.weather.databinding.ItemWeatherBinding

class WeatherAdapter : ListAdapter<TotalWeather, RecyclerView.ViewHolder>(WeatherDiffCallback()) {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(HeaderWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ItemViewHolder(ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind()
        } else if (holder is ItemViewHolder) {
            val item = currentList[position-1]
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return TYPE_HEADER
        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    class HeaderViewHolder(
        private val binding: HeaderWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                executePendingBindings()
            }
        }
    }

    class ItemViewHolder(
        private val binding: ItemWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TotalWeather) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }
}

private class WeatherDiffCallback : DiffUtil.ItemCallback<TotalWeather>() {

    override fun areItemsTheSame(oldItem: TotalWeather, newItem:TotalWeather): Boolean {
        return oldItem.woeid == newItem.woeid
    }

    override fun areContentsTheSame(oldItem: TotalWeather, newItem: TotalWeather): Boolean {
        return oldItem == newItem
    }
}