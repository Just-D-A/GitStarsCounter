package com.example.gitstarscounter.ui.screens.user_starred

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitstarscounter.R
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteStar
import com.example.gitstarscounter.data.to_rename_2.remote.entity.RemoteUser
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UserStarredAdapter(val context: Context) :
    OmegaRecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var usersList: ArrayList<RemoteUser> = ArrayList()
    private var sourceList: ArrayList<RemoteUser> = ArrayList()

    fun setupUsers(remoteStarList: MutableList<RemoteStar>) {
        val remoteUserList: ArrayList<RemoteUser> = ArrayList()
        remoteStarList.forEach {
            remoteUserList.add(it.user)
        }
        sourceList.clear()
        sourceList.addAll(remoteUserList)
        filter("")
    }

    fun filter(query: String) {
        usersList.clear()
        sourceList.forEach {
            if (it.name.contains(query, true)) {
                usersList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_user, parent, false)
        return UsersViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UsersViewHolder) {
            Log.d("BIND ", usersList[position].name)
            holder.bind(usersList[position])
        }
    }

    override fun getItemCount(): Int {
        return usersList.count()
    }

    class UsersViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        private var userNameTextView: TextView = itemView.findViewById(R.id.text_view_user_name)
        private var userPhotoImageView: CircleImageView =
            itemView.findViewById(R.id.circle_image_view_user_photo)

        @SuppressLint("SetTextI18n")
        fun bind(remoteUser: RemoteUser) {
            Glide
                .with(context)
                .load(remoteUser.avatarUrl)
                .centerCrop()
                .into(userPhotoImageView)
            userNameTextView.text = remoteUser.name
        }
    }
}
