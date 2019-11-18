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
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.R
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.viewmodel.VoteViewModel

class VoteFragment : Fragment() {

    companion object {
        const val GOOGLE_SIGN_IN = 10
    }

    private lateinit var firestore: FirebaseFirestore
    private val voteViewModel by viewModels<VoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.fragment_vote, container, false)

        FirebaseFirestore.setLoggingEnabled(true)
        initFirestore()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.take_picture_button)?.setOnClickListener {
            findNavController().navigate(R.id.action_voteFragment_to_permissionFragment)
            Log.d(TAG, "setNavOnClick")
        }
    }

    private fun initFirestore() {
        firestore = Firebase.firestore
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

    private fun startAuthentication() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
            .setIsSmartLockEnabled(false)
            .build()
        startActivityForResult(intent, GOOGLE_SIGN_IN)
        voteViewModel.isAuthenticating = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            voteViewModel.isAuthenticating = false
            if (resultCode != Activity.RESULT_OK && shouldStartAuthentication()) {
                startAuthentication()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
