package com.example.gitstarscounter.ui.screens.login

import android.content.Context
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

class LoginAdapter(
    val context: Context,
    private val onRepositoryClickListener: OnRepositoryClickListener
) : OmegaRecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var repositoriesList = ArrayList<Repository>()

    fun setRepositoriesList(repositoriesListInput: List<Repository?>?) {
        val repositoriesArrayList: ArrayList<Repository> = ArrayList()
        repositoriesListInput!!.forEach {
            repositoriesArrayList.add(it!!)
        }
        repositoriesList.clear()
        repositoriesList.addAll(repositoriesArrayList)
        notifyDataSetChanged()
    }

    fun addMoreRepositories(repositoriesListInput: List<Repository>) {
        val repositoriesArrayList: ArrayList<Repository> = ArrayList()
        repositoriesListInput.forEach {
            repositoriesArrayList.add(it)
        }
        repositoriesList.addAll(repositoriesArrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_repository, parent, false)
        return RepositoryViewHolder(itemView, repositoriesList, onRepositoryClickListener, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepositoryViewHolder) {
            holder.bind(repositoriesList[position])
        }
    }

    override fun getItemCount(): Int {
        return repositoriesList.count()
    }

    class RepositoryViewHolder(
        itemView: View,
        repositoriesList: List<Repository?>?,
        onRepositoryClickListener: OnRepositoryClickListener,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        private var bookCircleImageView: CircleImageView =
            itemView.findViewById(R.id.circle_image_view_book)
        private var nameRepositoryTextView: TextView =
            itemView.findViewById(R.id.text_view_repository_name)

        init {
            //обработка клика
            itemView.setOnClickListener {
                val repository: Repository? = repositoriesList?.get(layoutPosition)
                onRepositoryClickListener.onRepositoryClick(repository)
            }
        }

        fun bind(repository: Repository) {
            Glide
                .with(context)
                .load(R.drawable.repository_img)
                .centerCrop()
                .into(bookCircleImageView);
            nameRepositoryTextView.text = repository.name
        }
    }

    interface OnRepositoryClickListener {
        fun onRepositoryClick(repository: Repository?)
    }
}
