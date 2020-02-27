/**
 * Copyright 2020 Mostafa Sadraii
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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sadraii.shouldi.R
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.util.GlideApp
import com.sadraii.shouldi.viewmodel.VoteViewModel
import kotlinx.android.synthetic.main.fragment_vote.*

class VoteFragment : Fragment() {

    private lateinit var storageRef: StorageReference
    private val voteViewModel: VoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.fragment_vote, container, false)

        FirebaseFirestore.setLoggingEnabled(false)
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
        val user = FirebaseAuth.getInstance().currentUser
        voteViewModel.addUser(user!!)
        displayPictureForVoting()
    }

    private fun initFirestore() {
        storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference
    }

    private fun subscribeToModel() {
        voteViewModel.currentVotePicture.observe(viewLifecycleOwner, Observer { newPicture ->
            if (newPicture == null) {
                displayVotingOptions(false)
            } else {
                displayVotingOptions(true)
                val pictureRef = storageRef.child(newPicture.pictureUrl)
                Log.d(TAG, "Glide loading ${newPicture.pictureUrl}")
                GlideApp.with(this)
                    .load(pictureRef)
                    .centerCrop()
                    .into(picture_imageView)
            }
        })

        voteViewModel.currentVoteUser.observe(viewLifecycleOwner, Observer { newUser ->
            if (newUser?.userName.isNullOrBlank()) {
                username_textView.text = getString(R.string.anonymous_username)
            } else {
                username_textView.text = newUser?.userName
            }
        })
    }

    private fun displayPictureForVoting() {
        voteViewModel.user = FirebaseAuth.getInstance().currentUser!!
        voteViewModel.updateCurrentPicture()
        displayVotingOptions(true)
        vote_no_button.setOnClickListener { voteViewModel.voteYes(false) }
        vote_yes_button.setOnClickListener { voteViewModel.voteYes(true) }
    }

    private fun displayVotingOptions(visible: Boolean) {
        if (visible) {
            picture_cardView.visibility = View.VISIBLE
            no_pictures_textView.visibility = View.GONE
        } else {
            picture_cardView.visibility = View.GONE
            no_pictures_textView.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}


