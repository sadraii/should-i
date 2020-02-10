/**
 * Copyright 2019 Mostafa Sadraii
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sadraii.shouldi.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sadraii.shouldi.R
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.util.GlideApp
import com.sadraii.shouldi.viewmodel.VoteViewModel
import kotlinx.android.synthetic.main.fragment_vote.*

class VoteFragment : Fragment() {

    companion object {
        private const val SIGN_IN = 1001
    }

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private val voteViewModel: VoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.fragment_vote, container, false)

        FirebaseFirestore.setLoggingEnabled(true)
        initFirestore()
        subscribeToModel()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        take_picture_button.setOnClickListener {
            findNavController().navigate(R.id.action_voteFragment_to_permissionFragment)
            Log.d(TAG, "setNavOnClick")
        }

        picture_imageView.visibility = View.GONE
        no_vote_imageButton.visibility = View.GONE
        yes_vote_imageButton.visibility = View.GONE
        username_textView.visibility = View.GONE
        if (hasAuthenticated()) displayPictureForVoting()
    }

    private fun initFirestore() {
        firestore = Firebase.firestore
        storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference
    }

    override fun onStart() {
        super.onStart()

        if (shouldStartAuthentication()) {
            startAuthentication()
            return
        }
    }

    private fun shouldStartAuthentication() =
        !voteViewModel.isAuthenticating && FirebaseAuth.getInstance().currentUser == null

    private fun hasAuthenticated() =
        !voteViewModel.isAuthenticating && FirebaseAuth.getInstance().currentUser != null

    private fun startAuthentication() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
            .setIsSmartLockEnabled(false)
            .build()
        startActivityForResult(intent, SIGN_IN)
        voteViewModel.isAuthenticating = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN) {
            voteViewModel.isAuthenticating = false
            Log.d(TAG, "hiii")
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.metadata?.run {
                    //     if (creationTimestamp == lastSignInTimestamp) {
                    //         // User is new
                    // voteViewModel.addUser(user)
                    //     }
                }
                voteViewModel.addUser(user!!)
                displayPictureForVoting()
            } else if (resultCode != Activity.RESULT_OK && shouldStartAuthentication()) {
                startAuthentication()
            }
        }
    }

    private fun subscribeToModel() {
        voteViewModel.currentPicture.observe(viewLifecycleOwner, Observer<PictureEntity?> { newPicture ->
            if (newPicture == null) {
                GlideApp.with(this)
                    .load(R.drawable.ic_add_a_photo_black_36dp)
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(picture_imageView)
            } else {
                val pictureRef = storageRef.child(newPicture.pictureUrl)
                Log.d(TAG, "Glide loading ${newPicture.pictureUrl}")
                GlideApp.with(this)
                    .load(pictureRef)
                    .transform(CenterCrop(), RoundedCorners(50))
                    .placeholder(R.drawable.ic_photo_placeholder_24dp)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(picture_imageView)
            }
        })
    }

    private fun displayPictureForVoting() {
        voteViewModel.user = FirebaseAuth.getInstance().currentUser!!
        voteViewModel.updateCurrentPicture()

        no_vote_imageButton.visibility = View.VISIBLE
        no_vote_imageButton.setOnClickListener {
            Toast.makeText(context, "Voted NO", Toast.LENGTH_SHORT).show()
            voteViewModel.voteNo()
        }
        yes_vote_imageButton.visibility = View.VISIBLE
        yes_vote_imageButton.setOnClickListener {
            Toast.makeText(context, "Voted YES", Toast.LENGTH_SHORT).show()
            voteViewModel.voteYes()
        }
        picture_imageView.visibility = View.VISIBLE
        username_textView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}