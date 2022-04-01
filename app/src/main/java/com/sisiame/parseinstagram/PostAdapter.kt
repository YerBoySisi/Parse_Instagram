package com.sisiame.parseinstagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(private val context: Context, private val posts: ArrayList<Post>):
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    // Clean all elements of the recycler
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(postsToAdd: List<Post>) {
        posts.addAll(postsToAdd)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvUsernameHead: TextView = itemView.findViewById(R.id.user_head)
        private val tvUsername: TextView = itemView.findViewById(R.id.user)
        private val ivImage: ImageView = itemView.findViewById(R.id.picture)
        private val tvDescription: TextView = itemView.findViewById(R.id.description)
        private val tvCreatedAt: TextView = itemView.findViewById(R.id.created_at)

        fun bind(post: Post) {
            tvUsernameHead.text = post.getUser()?.fetchIfNeeded()?.username
            tvDescription.text = post.getDescription()
            tvUsername.text = tvUsernameHead.text
            tvCreatedAt.text = post.createdAt.toString()

            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
        }

    }

}