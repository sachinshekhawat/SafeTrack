package com.example.safetrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val listMembers: List<MemberModel>) :RecyclerView.Adapter<MemberAdapter.ViewHolder>() {


    class ViewHolder(private val item : View):RecyclerView.ViewHolder(item) {
        val imageUser = item.findViewById<ImageView>(R.id.img_user)
        val User_name = item.findViewById<TextView>(R.id.name)
        val User_address = item.findViewById<TextView>(R.id.address)
        val User_battery = item.findViewById<TextView>(R.id.battery_percent)
        val User_distance = item.findViewById<TextView>(R.id.distance_value)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_member,parent,false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listMembers[position]
        holder.User_name.text = item.name
        holder.User_address.text = item.address
        holder.User_battery.text = item.battery
        holder.User_distance.text = item.distance
    }

    override fun getItemCount(): Int {
        return listMembers.size
    }

}