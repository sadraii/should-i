package com.sadraii.shouldi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.sadraii.shouldi.R
import com.sadraii.shouldi.util.GlideApp
import kotlinx.android.synthetic.main.fragment_profile_dialog.*

class ProfileDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        GlideApp.with(view)
            .load(user?.photoUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_imageView)
        username_textView.text = user?.displayName
        email_textView.text = user?.email
        logout_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileDialog_to_authFragment)
        }
        delete_button.setOnClickListener {
            // TODO are you sure dialog?
            try {
                FirebaseAuth.getInstance().currentUser?.delete()
            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "Please sign in again to delete your account.", Toast.LENGTH_LONG).show()
            }
            findNavController().navigate(R.id.action_profileDialog_to_authFragment)
        }
    }
}

