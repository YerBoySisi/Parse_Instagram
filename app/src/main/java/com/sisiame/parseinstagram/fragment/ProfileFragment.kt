package com.sisiame.parseinstagram.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parse.ParseQuery
import com.parse.ParseUser
import com.sisiame.parseinstagram.Post
import com.sisiame.parseinstagram.R

class ProfileFragment : FeedFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun queryPosts() {

        // specify class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // find all post objects
        query.include(Post.KEY_USR)

        // find all post objects
        query.whereEqualTo(Post.KEY_USR, ParseUser.getCurrentUser())

        // return posts in descending order
        query.addDescendingOrder("createdAt")


        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(FeedFragment.TAG, "Error fetching posts")
            } else {

                if(posts != null) {

                    for(post in posts) {
                        Log.i(
                            FeedFragment.TAG,
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


}