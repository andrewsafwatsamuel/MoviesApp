package com.moviesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moviesapp.CreditsMember
import com.moviesapp.presentation.POSTER_SIZE
import com.moviesapp.presentation.R
import com.moviesapp.presentation.drawPhoto

class CreditsHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name by lazy { view.findViewById<TextView>(R.id.name_text_view) }
    private val role by lazy { view.findViewById<TextView>(R.id.role_text_view) }
    private val image by lazy { view.findViewById<ImageView>(R.id.photo_image_view) }

    fun bind(member: CreditsMember) {
        name.text = member.title
        role.text = member.role
        drawPhoto(POSTER_SIZE, member.url, image)
    }
}

class CreditsAdapter(private val layout: Int, private val members: List<CreditsMember>) :
    RecyclerView.Adapter<CreditsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditsHolder =
        LayoutInflater.from(parent.context).inflate(layout, parent, false)
            .let { CreditsHolder(it) }

    override fun getItemCount() = members.size

    override fun onBindViewHolder(holder: CreditsHolder, position: Int) {
        holder.bind(members[position])
    }
}