package com.example.tropicalforest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.InvoiceDetailActivity
import com.example.tropicalforest.R
import com.example.tropicalforest.model.InvoiceData
import com.example.tropicalforest.shareData.MyData

class InvoiceAdapter(
    private val context: Context,
    private val dataset: ArrayList<InvoiceData>
) : RecyclerView.Adapter<InvoiceAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val invoiceCard: CardView = view.findViewById(R.id.invoiceCart)
        val invoiceTextView: TextView = view.findViewById(R.id.invoiceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.invoice_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.invoiceTextView.text = "Hóa đơn: " + item.inv_id.toString()
        holder.invoiceCard.setOnClickListener {
            MyData.inv_id = item.inv_id
            val intent = Intent(context, InvoiceDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataset.size
}