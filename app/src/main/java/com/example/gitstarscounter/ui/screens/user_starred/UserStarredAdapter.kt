package com.example.gitstarscounter.ui.screens.user_starred

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.entity.User
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UserStarredAdapter(val context: Context) :
    OmegaRecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var usersList: ArrayList<User> = ArrayList()
    private var sourceList: ArrayList<User> = ArrayList()

    fun setupUsers(remoteStarList: MutableList<Star>) {
        val addedUserList: ArrayList<User> = ArrayList()
        remoteStarList.forEach {
            addedUserList.add(it.user)
        }
        sourceList.clear()
        sourceList.addAll(addedUserList)
        usersList.addAll(sourceList)
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
        return UsersViewHolder(itemView)
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

    class UsersViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var userNameTextView: TextView =
            itemView.findViewById(R.id.text_view_user_name)

        private var userPhotoImageView: CircleImageView =
            itemView.findViewById(R.id.circle_image_view_user_photo)

        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            /*     Glide
                     .with(context)
                     .load(user.avatarUrl)
                     .centerCrop()
                     .into(userPhotoImageView)*/
            userNameTextView.text = user.name
        }
    }
}
