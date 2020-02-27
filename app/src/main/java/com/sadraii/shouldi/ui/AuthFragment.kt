/*
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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.sadraii.shouldi.R
import com.sadraii.shouldi.viewmodel.AuthViewModel

class AuthFragment : Fragment() {

    companion object {
        private const val SIGN_IN = 1001
    }

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (shouldStartAuthentication()) {
            startAuthentication()
            return
        } else {
            findNavController().navigate(R.id.action_authFragment_to_voteFragment)
        }
    }

    private fun shouldStartAuthentication() =
        !authViewModel.isAuthenticating && FirebaseAuth.getInstance().currentUser == null

    private fun startAuthentication() {
        authViewModel.isAuthenticating = true
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN) {
            authViewModel.isAuthenticating = false
            if (resultCode == Activity.RESULT_OK) {
                // TODO if current dest = auth
                findNavController().navigate(R.id.action_authFragment_to_voteFragment)
            } else if (resultCode != Activity.RESULT_OK && shouldStartAuthentication()) {
                startAuthentication()
            }
        }
    }
}
