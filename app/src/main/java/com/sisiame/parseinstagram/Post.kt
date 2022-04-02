package com.sisiame.parseinstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*


// description : String
// image: File
// user: User
@ParseClassName("Post")
class Post : ParseObject() {

    fun getDescription(): String? {
        return getString(KEY_DESC)
    }

    fun setDescription(desc: String) {
        put(KEY_DESC, desc)
    }

    fun getImage(): ParseFile? {
        return getParseFile(KEY_IMG)
    }

    fun setImage(parseFile: ParseFile) {
        put(KEY_IMG, parseFile)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USR)
    }

    fun setUser(parseUser: ParseUser) {
        put(KEY_USR, parseUser)
    }

    fun getProfileImage(): ParseFile? {
        return getUser()?.getParseFile(KEY_PFP)
    }

    companion object {
        const val KEY_DESC = "description"
        const val KEY_IMG = "image"
        const val KEY_USR = "user"
        const val KEY_PFP = "profilePicture"
    }

}