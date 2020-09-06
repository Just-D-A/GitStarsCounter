package com.example.gitstarscounter.login


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.RepositoryModel
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.PaginationViewCreator
import de.hdodenhof.circleimageview.CircleImageView

class RepositoryAdapter(
    private val onRepositoryClickListener: OnRepositoryClickListener,
    val context: Context)
    : OmegaRecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var repositoriesList: ArrayList<RepositoryModel> = ArrayList() //как праильно по соглашению??
    lateinit var adapterCallback: Callback

    fun setupRepositoriesList(repositoriesListInput: List<RepositoryModel?>?) {
        val repositoriesArrayList: ArrayList<RepositoryModel> = ArrayList()
        repositoriesListInput!!.forEach {
            repositoriesArrayList.add(it!!)
        }
        repositoriesList.clear()
        repositoriesList.addAll(repositoriesArrayList)
        notifyDataSetChanged()
    }

    fun addMoreRepositories(repositoriesListInput: List<RepositoryModel>) {
        val repositoriesArrayList: ArrayList<RepositoryModel> = ArrayList()
        repositoriesListInput!!.forEach {
            repositoriesArrayList.add(it!!)
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

    fun setCallback(callback: Callback) {
        adapterCallback = callback
    }

    override fun getItemCount(): Int {
        return repositoriesList.count()
    }

    class RepositoryViewHolder(
        itemView: View,
        repositoriesList: List<RepositoryModel?>?,
        onRepositoryClickListener: OnRepositoryClickListener,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        private var bookCircleImageView: CircleImageView =
            itemView.findViewById(R.id.circle_image_view_book)
        private var nameRepositoryTextView: TextView = itemView.findViewById(R.id.text_rep_name)

        init {
            //обработка клика
            itemView.setOnClickListener {
                val repository: RepositoryModel? = repositoriesList?.get(layoutPosition)
                onRepositoryClickListener.onRepositoryClick(repository)
            }
        }

        fun bind(repository: RepositoryModel) {
            val imageURL =
                "https://img2.freepng.ru/20180516/ohq/kisspng-used-book-computer-icons-5afc9d4ed92065.8296718615265047828894.jpg"
            Glide
                .with(context)
                .load(R.drawable.repository_img)
                .centerCrop()
                .into(bookCircleImageView);
            nameRepositoryTextView.text = repository.name
        }
    }

    interface OnRepositoryClickListener {
        fun onRepositoryClick(repository: RepositoryModel?)
    }

    interface Callback {
        fun onGetMoreRepositories(repositoriesModel: List<RepositoryModel>?)
    }
}
