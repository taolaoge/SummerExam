package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.beans.TiktokResponseItem
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.extensions.gone
import com.example.summerexam.extensions.visible
import com.example.summerexam.view.PrepareView

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
class TiktokVpAdapter(private val clickVideo:(Int) ->Unit)
    : ListAdapter<TiktokResponseItem, TiktokVpAdapter.InnerHolder>(DiffCallBack) {

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mPlayerContainer = itemView.findViewById<FrameLayout?>(R.id.fl_tiktok_container)
        val mPrepareView = itemView.findViewById<PrepareView>(R.id.prepare_view_tiktok)
        val mThumb = mPrepareView.findViewById<ImageView>(R.id.thumb)
        var mPosition = 0

        init {
            //通过tag将ViewHolder和itemView绑定
            itemView.tag = this

            mPlayerContainer.setOnClickListener {
                clickVideo(mPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewpager_item_tiktok, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val video = getItem(position)
        holder.run {
            mPosition = position
            if (video.joke.videoUrl.decrypt() != "") {
                holder.mPlayerContainer.visible()
                Glide.with(holder.mThumb.context)
                    .load(video.joke.thumbUrl.decrypt())
                    .into(holder.mThumb)
            } else holder.mPlayerContainer.gone()
        }
    }

    /**
     * 差分刷新固定写法
     */
    object DiffCallBack : DiffUtil.ItemCallback<TiktokResponseItem>() {

        override fun areItemsTheSame(
            oldItem: TiktokResponseItem,
            newItem: TiktokResponseItem
        ): Boolean {
            return oldItem.joke.jokesId == newItem.joke.jokesId
        }

        override fun areContentsTheSame(
            oldItem: TiktokResponseItem,
            newItem: TiktokResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}