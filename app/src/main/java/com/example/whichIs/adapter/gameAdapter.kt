package com.example.whichIs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.whichIs.R
import com.example.whichIs.model.Game
import com.example.whichIs.model.Quiz

class GameAdapter(private val array : ArrayList<Quiz>): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var clickDisabled = true

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageCard0: ImageView by lazy {
            itemView.findViewById(R.id.top_image)
        }
        val imageCard1: ImageView by lazy {
            itemView.findViewById(R.id.bottom_image)
        }
        val timer = itemView.findViewById<TextView>(R.id.timer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // Replace "your_layout.xml" with your actual layout file name
        val view = inflater.inflate(R.layout.game_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
        val imagesUrl0 : String = array[position].imageUrlAnswer[0].imageUrl
        val imagesUrl1 : String = array[position].imageUrlAnswer[1].imageUrl
        val requestOptions= RequestOptions().transform(CenterInside())

        Glide.with(holder.imageCard0)
            .load(imagesUrl0)
            .apply(requestOptions)
            .into(holder.imageCard0)

        Glide.with(holder.imageCard1)
            .load(imagesUrl1)
            .apply(requestOptions)
            .into(holder.imageCard1)

        if (this.clickDisabled){
            holder.imageCard0.setOnClickListener {
                listener?.onItemClick(0)
            }
            holder.imageCard1.setOnClickListener {
                listener?.onItemClick(1)
            }
        }
    }

    fun setDisabled_Enabled(position: Int,disabledOrEnabled:Boolean){
        this.clickDisabled = disabledOrEnabled
        notifyItemChanged(position)
        notifyItemChanged(position+1)
    }

    interface OnItemClickListener {
        fun onItemClick(userAnswer: Int)
    }
}