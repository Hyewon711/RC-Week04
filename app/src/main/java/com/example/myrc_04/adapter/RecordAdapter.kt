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
        private val imageView = binding.rankImg
        private val nameTextView = binding.rankName
        private val scoreTextView = binding.rankScore
        private var imgNumber = 0

        //뷰와 데이터를 연결
        fun bind(rank: Rank) {
            nameTextView.text = rank.name
            scoreTextView.text = rank.score.toString()

            when (rank.img) {
                1 -> imgNumber = R.drawable.ic_record_img1_select
                2 -> imgNumber = R.drawable.ic_record_img2_select
                3 -> imgNumber = R.drawable.ic_record_img3_select
                4 -> imgNumber = R.drawable.ic_record_img4_select
                5 -> imgNumber = R.drawable.ic_record_img5_select
                6 -> imgNumber = R.drawable.ic_record_img6_select
                7 -> imgNumber = R.drawable.ic_record_img7_select
                8 -> imgNumber = R.drawable.ic_record_img8_select
            }

            Glide
                .with(App.instance)
                .load(imgNumber)
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
        dataSet.sortBy{ it.score }
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