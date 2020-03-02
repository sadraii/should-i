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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.storage.FirebaseStorage
import com.sadraii.shouldi.R
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.util.GlideApp
import com.sadraii.shouldi.viewmodel.PictureDetailViewModel
import kotlinx.android.synthetic.main.fragment_picture_detail.*

class PictureDetail : Fragment() {

    private val safeArgs: PictureDetailArgs by navArgs()
    private val pictureDetailViewModel: PictureDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToModel()
        val pictureEntity = safeArgs.pictureEntity

        yes_vote_textView.text = pictureEntity.yesVotes.toString()
        no_vote_textView.text = pictureEntity.noVotes.toString()

        val storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference
        val pictureRef = storageRef.child(pictureEntity.pictureUrl)
        GlideApp.with(this)
            .load(pictureRef)
            .centerCrop()
            .into(picture_imageView)

        delete_fab.setOnClickListener { pictureDetailViewModel.delete(pictureEntity) }
    }

    private fun subscribeToModel() {
        pictureDetailViewModel.pictureDeleted.observe(viewLifecycleOwner, Observer { picDeleted ->
            if (picDeleted) {
                findNavController().popBackStack()
            }
        })
    }
}


