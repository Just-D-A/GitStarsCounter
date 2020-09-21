package com.example.gitstarscounter.ui.screens.repository

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class RepositoryAdapter(val deleteCallback: DeleteCallback) : OmegaRecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var repositoryList = ArrayList<Repository>()

    fun setRepositoriesList(repositoriesListInput: List<Repository?>?) {
        val repositoriesArrayList: ArrayList<Repository> = ArrayList()
        repositoriesListInput!!.forEach {
            repositoriesArrayList.add(it!!)
        }
        repositoryList.clear()
        repositoryList.addAll(repositoriesArrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_repository_for_screen, parent, false)
        return RepositoryViewHolder(parent.context, itemView, deleteCallback)
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
        val context: Context,
        itemView: View,
        private val deleteCallback: DeleteCallback
    ) : RecyclerView.ViewHolder(itemView) {
        private var bookCircleImageView: CircleImageView =
            itemView.findViewById(R.id.circle_image_view_rep_image)

        private var repositoryNameTextView: TextView =
            itemView.findViewById(R.id.text_view_rep_name)

        private var userNameTextView: TextView =
            itemView.findViewById(R.id.text_view_owner_name)

        private val deleteButton: Button =
            itemView.findViewById(R.id.button_delete_repository)

        fun bind(repository: Repository) {
            Glide
                .with(context)
                .load(R.drawable.repository_img)
                .centerCrop()
                .into(bookCircleImageView);

            repositoryNameTextView.text = repository.name
            userNameTextView.text = repository.user.name

            deleteButton.setOnClickListener {
                deleteCallback.onPressedDeleteButton(repository)
            }
        }
    }

    interface DeleteCallback {
        fun onPressedDeleteButton(repository: Repository)
    }
}
