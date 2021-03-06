package com.sisiame.parseinstagram.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.parse.ParseFile
import com.parse.ParseUser
import com.sisiame.parseinstagram.MainActivity
import com.sisiame.parseinstagram.Post
import com.sisiame.parseinstagram.R
import java.io.File

class ComposeFragment : Fragment() {

    private var photoFileName = "photo.jpg"
    private var photoFile: File? = null

    private lateinit var ivPreview: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showPhotoScreen(view)

        ivPreview = view.findViewById(R.id.img_preview)

        view.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            val desc = view.findViewById<EditText>(R.id.field_desc).text.toString()
            val user = ParseUser.getCurrentUser()

            if(photoFile != null) {
                submitPost(desc, user, photoFile!!)
            } else {
                Log.e(MainActivity.TAG, "Photo file does not exist")
                Toast.makeText(requireContext(), "There was a problem with your photo", Toast.LENGTH_SHORT).show()
            }

        }

        view.findViewById<Button>(R.id.btn_takephoto).setOnClickListener {
            onLaunchCamera()
        }

        view.findViewById<Button>(R.id.btn_createcaption).setOnClickListener {
            showCaptionScreen(view)
        }

        view.findViewById<Button>(R.id.btn_retakephoto).setOnClickListener {
            showPhotoScreen(view)
        }
        
    }

    /**
     * Shows screen for taking photo and hides screen for creating caption
     * @param view the view parameter from onViewCreated
     */
    private fun showPhotoScreen(view: View) {
        view.findViewById<View>(R.id.layout_photo).visibility = View.VISIBLE
        view.findViewById<View>(R.id.layout_caption).visibility = View.GONE
    }


    /**
     * Shows screen for creating caption and hides screen for taking photo if
     * a photo has been taken, otherwise displays a message to take a photo
     * @param view the view parameter from onViewCreated
     */
    private fun showCaptionScreen(view: View) {

        if(photoFile != null) {
            view.findViewById<View>(R.id.layout_photo).visibility = View.GONE
            view.findViewById<View>(R.id.layout_caption).visibility = View.VISIBLE
        } else {
            Toast.makeText(requireContext(), "Take a photo first!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun submitPost(desc: String, user: ParseUser, file: File) {

        val post = Post()
        post.setDescription(desc.trim())
        post.fetchIfNeeded<Post>().setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { e ->

            if(e != null) {
                Log.e(TAG, "Error while saving post: $e")
                Toast.makeText(requireContext(), "Error while saving post", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } else {
                Log.i(TAG, "Successfully saved post")
                Toast.makeText(requireContext(), "Saved post", Toast.LENGTH_SHORT).show()
                goToFeed()
            }

        }

    }

    private fun goToFeed() {

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, FeedFragment())
            .commit()

    }

    // Returns the File for a photo stored on disk given the fileName
    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider.parseinstagram", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            photoFileName = photoFile!!.name

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TAG = "ComposeFragment"
    }

}