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

package com.sadraii.shouldi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.viewmodel.ShouldIViewModel

class ShouldIActivity : AppCompatActivity() {

    companion object {
        const val GOOGLE_SIGN_IN = 10
    }

    private lateinit var firestore: FirebaseFirestore
    private val shouldIViewModel by viewModels<ShouldIViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shouldi)

        FirebaseFirestore.setLoggingEnabled(true)
        initFirestore()
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
        !shouldIViewModel.isAuthenticating && FirebaseAuth.getInstance().currentUser == null

    private fun startAuthentication() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
            .setIsSmartLockEnabled(false)
            .build()
        startActivityForResult(intent, GOOGLE_SIGN_IN)
        shouldIViewModel.isAuthenticating = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            shouldIViewModel.isAuthenticating = false
            if (resultCode != Activity.RESULT_OK && shouldStartAuthentication()) {
                startAuthentication()
            }
        }
    }
}


