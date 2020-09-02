    package com.example.gitstarscounter.user_starred

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
    import com.example.gitstarscounter.git_api.Star
    import com.example.gitstarscounter.git_api.User
    import de.hdodenhof.circleimageview.CircleImageView

    class UserStarredAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var usersList: ArrayList<User> = ArrayList()
        private var sourceList: ArrayList<User> = ArrayList()

        fun setupUsers(starList: MutableList<Star>) {
            val userList: ArrayList<User> = ArrayList()
            starList.forEach {
                userList.add(it.user)
            }
            sourceList.clear()
            sourceList.addAll(userList)
            filter("")
        }

        fun filter(query: String) {
            usersList.clear()
            sourceList.forEach {
                if (it.login.contains(query, true)) {
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
                Log.d("BIND ", usersList[position].login)
                holder.bind(usersList[position])
            }
        }

        override fun getItemCount(): Int {
            return usersList.count()
        }

        class UsersViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
            private var userNameTextView: TextView = itemView.findViewById(R.id.text_view_user_name)
            private var userPhotoImageView: CircleImageView =
                itemView.findViewById(R.id.circle_image_view_user_photo)

            @SuppressLint("SetTextI18n")
            fun bind(user: User) {
                Glide
                    .with(context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(userPhotoImageView)
                userNameTextView.text = user.login
            }

        }

    }