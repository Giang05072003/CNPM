package com.example.tropicalforest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tropicalforest.R
import com.example.tropicalforest.model.ProductData


class CartAdapter(
    private val context: Context,
    private val dataset: ArrayList<ProductData>
) : RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val prodImage: ImageView = view.findViewById(R.id.prodImageView)
        val prodName: TextView = view.findViewById(R.id.prodNameTextView)
        val prodPrice: TextView = view.findViewById(R.id.prodPriceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(context).load(item.prod_image).into(holder.prodImage)
        holder.prodName.text = item.prod_name.toString()
        holder.prodPrice.text = item.price.toString() + "đ"
    }

    override fun getItemCount() = dataset.size
}