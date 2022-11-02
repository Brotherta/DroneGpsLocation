package com.example.testkotlinwatch

import android.content.Context
import android.location.Location
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testkotlinwatch.databinding.ItemGpsLocationBinding
import java.util.*


class GpsLocationAdaptater(val locations: ArrayList<Location>, val context: Context):
    RecyclerView.Adapter<GpsLocationAdaptater.GpslocationViewHolder>() {

    companion object {
        const val DMYHMS_DATE_FORMAT = "dd/MM/yyyy-HH':'mm':'ss"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GpslocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGpsLocationBinding.inflate(layoutInflater, parent, false);
        return GpslocationViewHolder(binding);
    }

    override fun onBindViewHolder(holder: GpslocationViewHolder, position: Int) {
        val curItem = locations.get(position)
        val date = Date(curItem.time)
        holder.binding.timeStampTv.text = context.getString(
            R.string.timestamp_format,
            DateFormat.format(
                DMYHMS_DATE_FORMAT,
                date
            ).toString()
        )
        holder.binding.latitudeTv.text =
            context.getString(R.string.latitude_format, curItem.latitude)
        holder.binding.longitudeTv.text =
            context.getString(R.string.longitude_format, curItem.longitude)
    }

    override fun getItemCount(): Int {

        return locations.size
    }

    class GpslocationViewHolder(val binding: ItemGpsLocationBinding): RecyclerView.ViewHolder(binding.root) {
    }
}
