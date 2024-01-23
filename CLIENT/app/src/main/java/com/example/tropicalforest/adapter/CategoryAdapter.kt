package com.example.tropicalforest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tropicalforest.CategoryFilterActivity
import com.example.tropicalforest.HomeFragment
import com.example.tropicalforest.R
import com.example.tropicalforest.model.CategoryData
import com.example.tropicalforest.shareData.MyData

class CategoryAdapter(
    private val context: HomeFragment,
    private val dataset: ArrayList<CategoryData>
) : RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cat_Card: CardView = view.findViewById(R.id.catCard)
        val cat_imageImageView: ImageView = view.findViewById(R.id.cat_imageImageView)
        val cat_describeTextView: TextView = view.findViewById(R.id.cat_describeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(context).load(item.cat_image).into(holder.cat_imageImageView)
        holder.cat_describeTextView.text = item.cat_name.toString()
        holder.cat_Card.setOnClickListener {
            val intent = Intent(context.requireContext(), CategoryFilterActivity::class.java)
            MyData.cat_id = item.cat_id
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataset.size
}