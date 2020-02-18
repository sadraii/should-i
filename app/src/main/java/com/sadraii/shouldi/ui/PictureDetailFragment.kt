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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.storage.FirebaseStorage
import com.sadraii.shouldi.R
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.util.GlideApp
import kotlinx.android.synthetic.main.fragment_picture_detail.*

class PictureDetailFragment : Fragment() {

    private val safeArgs: PictureDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pictureEntity = safeArgs.pictureEntity

        yes_vote_textView.text = pictureEntity.yesVotes.toString()
        no_vote_textView.text = pictureEntity.noVotes.toString()

        val storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference
        val pictureRef = storageRef.child(pictureEntity.pictureUrl)
        GlideApp.with(this)
            .load(pictureRef)
            .transform(CenterCrop())
            .placeholder(R.drawable.ic_photo_placeholder_24dp)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(picture_imageView)
    }
}
