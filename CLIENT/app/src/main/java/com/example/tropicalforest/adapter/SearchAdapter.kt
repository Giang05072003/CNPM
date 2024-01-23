package com.example.tropicalforest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tropicalforest.ProductDetailActivity
import com.example.tropicalforest.R
import com.example.tropicalforest.SearchFragment
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.shareData.MyData

class SearchAdapter(
    private val context: SearchFragment,
    private var dataset: ArrayList<ProductData>
) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val prod_Card: LinearLayout = view.findViewById(R.id.prodCard)
        val product_imageImageView: ImageView = view.findViewById(R.id.product_imageImageView)
        val product_nameTextView: TextView = view.findViewById(R.id.product_nameTextView)
        val product_priceTextView: TextView = view.findViewById(R.id.product_priceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(context).load(item.prod_image).into(holder.product_imageImageView)
        holder.product_nameTextView.setText(item.prod_name.toString())
        holder.product_priceTextView.setText(item.price.toString() + "đ")
        holder.prod_Card.setOnClickListener {
            val intent = Intent(context.requireContext(), ProductDetailActivity::class.java)
            MyData.cat_id = item.cat_id
            MyData.prod_id = item.prod_id
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataset.size

}