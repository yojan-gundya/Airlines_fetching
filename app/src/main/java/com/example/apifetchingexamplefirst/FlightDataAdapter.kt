package com.example.apifetchingexamplefirst



import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apifetchingexamplefirst.databinding.RvItemsBinding
import `in`.yajnesh.util.android.GenericUtil


//class FlightDataAdapter(private var context: Context, var list: List<FlightData>?) :
//    RecyclerView.Adapter<FlightDataAdapter.ViewHolder>() {

class FlightDataAdapter(private var context: Context, private var list: List<Map<String,Any>>) :
    RecyclerView.Adapter<FlightDataAdapter.ViewHolder>() {
    var i=0


//    inner class ViewHolder(v: View) :
    inner class ViewHolder(private val binding:RvItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var image = binding.flightimage
        var name = binding.flightname
        var code = binding.flightcode
    var count=binding.countNumber

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RvItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
//        val l = list
//        if (l == null) {
//            return 0
//        } else return l.size

//        if (list == null) {
//            return 0
//        } else {
//            return list.size
//        }


        return GenericUtil.size(list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = GenericUtil.get(list, position)
        i++

            holder.name.text = item["name"]?.toString() ?: ""
            holder.code.text = item["code"]?.toString() ?: ""
            holder.count.text = i.toString()
            val imageUrl = item["imageUrl"]?.toString()
            Glide.with(context)
                .load(imageUrl)
                .into(holder.image)

    }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = GenericUtil.get(list, position)
//        if (item == null) {
//            holder.name.text = ""
//            holder.code.text = ""
//            Glide.with(context)
//                .load(item.imageUrl)
//              .into(holder.image)
//        } else {
//            holder.name.text = item.name
//            holder.code.text = item.code
//            Glide.with(context)
//                .load(item.imageUrl)
//                .into(holder.image)
//        }
//    }


}





