package com.bryan.personalproject.ui.screens.profile.viewModel

import android.net.Uri

interface ProfileViewModel {

    fun getCurrentUser()
    fun updateProfilePic(uri: Uri)
    fun getProfilePicUri()
    fun logout()

}