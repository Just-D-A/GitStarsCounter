package com.example.gitstarscounter.ui.screens.repository

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class RepositoryAdapter : OmegaRecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var repositoryList: MutableList<Repository> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_user, parent, false)
        return RepositoryViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepositoryViewHolder) {
            Log.d("BIND ", repositoryList[position].name)
            holder.bind(repositoryList[position])
        }
    }

    override fun getItemCount(): Int {
        return repositoryList.count()
    }

    class RepositoryViewHolder(
        itemView: View,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        private var bookCircleImageView: CircleImageView= itemView.findViewById(R.id.circle_image_view_rep_image)

        private var repositoryNameTextView: TextView =
            itemView.findViewById(R.id.text_view_rep_name)

        private var userNameTextView: TextView =
            itemView.findViewById(R.id.text_view_owner_name)

        fun bind(repository: Repository) {
            Glide
                .with(context)
                .load(R.drawable.repository_img)
                .centerCrop()
                .into(bookCircleImageView);

            repositoryNameTextView.text = repository.name
            userNameTextView.text = repository.user.name
        }

    }
}
