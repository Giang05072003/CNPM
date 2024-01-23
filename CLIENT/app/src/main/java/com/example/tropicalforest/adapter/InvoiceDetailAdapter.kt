package com.example.tropicalforest.adapter

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

class InvoiceDetailAdapter(
    private val context: Context,
    private val dataset: ArrayList<ProductData>
) : RecyclerView.Adapter<InvoiceDetailAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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
    }

    override fun getItemCount() = dataset.size
}