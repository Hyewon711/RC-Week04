package com.example.myrc_04.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrc_04.App
import com.example.myrc_04.R
import com.example.myrc_04.databinding.ItemRecordBinding
import com.example.myrc_04.model.Rank

class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder> () {
    val TAG: String = "로그"
    var dataSet = ArrayList<Rank>()

    inner class RecordViewHolder(private val binding: ItemRecordBinding): RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.rankImg
        val nameTextView = binding.rankName
        val scoreTextView = binding.rankScore


        //뷰와 데이터를 연결
        fun bind(rank: Rank) {
            nameTextView.text = rank.name
            scoreTextView.text = rank.score.toString()

            Glide
                .with(App.instance)
                .load(rank.img)
                .placeholder(R.drawable.ic_test_img)
                .fallback(R.drawable.ic_test_img)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // 데이터와 뷰를 묶음
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    // 데이터 삭제
    fun removeData(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }
//    // 데이터 추가
//    fun addData(img: Int, name: String, score: Int) {
//        dataSet.add(Rank(img, name, score))
//    }

    // 외부에서 데이터 넘기기
    fun submitList(rank: ArrayList<Rank>){
        dataSet = rank
    }

    fun setList(rank: ArrayList<Rank>) {
        dataSet = rank
        notifyItemRangeChanged(0, dataSet.size)
    }

}