package com.example.myrc_04.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrc_04.App
import com.example.myrc_04.R
import com.example.myrc_04.databinding.ItemRecordBinding
import com.example.myrc_04.model.Rank

class RecordAdapter(private val rank: ArrayList<Rank>) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder> () {
    inner class RecordViewHolder(private val binding: ItemRecordBinding): RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.rankImg
        private val nameTextView = binding.rankName
        private val scoreTextView = binding.rankScore

        //뷰와 데이터를 연결
        fun bind(rank: Rank) {
            nameTextView.text = rank.name
            scoreTextView.text = rank.score

            Glide
                .with(App.instance)
                .load(rank.img)
                .placeholder(R.drawable.ic_test_img)
                .fallback(R.drawable.ic_test_img)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val itemView = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val rankItem = this.rank[position]
        // 데이터와 뷰 묶음
        holder.bind(rankItem)
    }

    override fun getItemCount(): Int {
        return rank.size
    }

}