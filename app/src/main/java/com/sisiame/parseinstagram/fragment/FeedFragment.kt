package com.sisiame.parseinstagram.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.parse.ParseQuery
import com.sisiame.parseinstagram.Post
import com.sisiame.parseinstagram.PostAdapter
import com.sisiame.parseinstagram.R

open class FeedFragment : Fragment() {

    private lateinit var postRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: ArrayList<Post> = arrayListOf()

    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postRecyclerView = view.findViewById(R.id.rv_posts)

        // bind adapter to post recycler view
        adapter = PostAdapter(requireContext(), allPosts)
        postRecyclerView.adapter = adapter

        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            adapter.clear()
            queryPosts()
        }

        queryPosts()

    }

    open fun queryPosts() {

        // specify class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // find all post objects
        query.include(Post.KEY_USR)

        // return posts in descending order
        query.addDescendingOrder("createdAt")


        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching posts")
            } else {

                if(posts != null) {

                    for(post in posts) {
                        Log.i(
                            TAG,
                            "Post: " + post.getDescription() + ", user: " +
                                    post.getUser()?.objectId
                        )

                        adapter.addAll(posts)
                        swipeContainer.isRefreshing = false
                    }

                }

            }
        }

    }

    companion object {
        const val TAG = "FeedFragment"
    }

}