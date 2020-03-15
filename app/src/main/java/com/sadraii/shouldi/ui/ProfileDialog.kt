package com.sadraii.shouldi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.sadraii.shouldi.R
import com.sadraii.shouldi.util.GlideApp
import com.sadraii.shouldi.viewmodel.ProfileDialogViewModel
import kotlinx.android.synthetic.main.fragment_profile_dialog.*

class ProfileDialog : BottomSheetDialogFragment() {

    private val profileViewModel: ProfileDialogViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        GlideApp.with(view)
            .load(user?.photoUrl)
            .fallback(R.drawable.ic_account_circle_black_24dp)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_imageView)
        username_textView.text = user?.displayName
        email_textView.text = user?.email

        logout_button.setOnClickListener {
            signOut()
        }
        delete_button.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.delete()?.addOnFailureListener {
                Toast.makeText(context, "Please sign in again to delete your account.", Toast.LENGTH_LONG).show()
            }
            signOut()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_profileDialog_to_authFragment)
    }
}

