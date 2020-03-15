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

package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.sadraii.shouldi.TAG
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileDialogViewModel(application: Application) : AndroidViewModel(application) {

    internal fun delete() {
        viewModelScope.launch {
            try {
                val x = FirebaseAuth.getInstance().currentUser?.delete()?.await()
                Log.d(TAG, "dbug delete() void=$x")
            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                FirebaseAuth.getInstance().signOut()
                Log.d(TAG, "dbug delete() signout()")
                // Toast.makeText(conte, "Please sign in again to delete your account.", Toast.LENGTH_LONG).show()
            } catch (e: FirebaseAuthInvalidUserException) {
                Log.d(TAG, "dbug delete() authinvalid")
                // Toast.makeText(context, "authinvalidexception", Toast.LENGTH_LONG).show()
            }
            Log.d(TAG, "dbug delete() user")
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileDialogViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileDialogViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ProfileDialogViewModel")
        }
    }
}