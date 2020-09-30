package com.example.gitstarscounter.ui.screens.user_starred

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.entity.User
import com.omega_r.base.adapters.OmegaListAdapter
import com.omega_r.libs.omegatypes.image.UrlImage
import de.hdodenhof.circleimageview.CircleImageView

class UserStarredAdapter :
    OmegaListAdapter<User, UserStarredAdapter.UsersViewHolder>() {
    override var list: List<User> = emptyList()
    private var sourceList: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_user, parent, false)
        return UsersViewHolder(itemView)
    }

    fun setupUsers(remoteStarList: List<Star>) {
        val addedUserList: ArrayList<User> = ArrayList()
        remoteStarList.map {
            addedUserList.add(it.user)
        }
        sourceList = addedUserList
        list = sourceList
    }

    fun filter(query: String) {
        list = sourceList
        list = list.filter {
            (it.name.contains(query, true))
        }
        notifyDataSetChanged()//need of this!!
    }

    class UsersViewHolder(itemView: View) :
        OmegaListAdapter.ViewHolder<User>(itemView) {
        private var userNameTextView: TextView =
            itemView.findViewById(R.id.text_view_user_name)

        private var userPhotoImageView: CircleImageView =
            itemView.findViewById(R.id.circle_image_view_user_photo)

        @SuppressLint("SetTextI18n")
        override fun bind(item: User) {
            Glide
                .with(itemView.context)
                .load((item.avatar as UrlImage).url)
                .centerCrop()
                .into(userPhotoImageView)

            userNameTextView.text = item.name
        }
    }
}
