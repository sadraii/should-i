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
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.R
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import com.sadraii.shouldi.viewmodel.MyPicturesViewModel
import kotlinx.android.synthetic.main.fragment_my_pictures.*
import kotlinx.android.synthetic.main.item_my_picture.view.*

class MyPicturesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirebaseRecyclerPagingAdapter<PictureEntity, PictureViewHolder>
    private val myPicturesViewModel by viewModels<MyPicturesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = my_pictures_recyclerView

        val firestore = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser
        val picturesCollectionId = firestore
            .collection(UserRepository.USERS_PATH)
            .document(user!!.uid)
            .collection(PictureRepository.PICTURES_PATH)
            .id
        val picturesQuery = firestore.collectionGroup(picturesCollectionId)
        // val adapterParser =
        val adapterOptions = FirebaseRecyclerOptions.Builder<PictureEntity>()
            .setLifecycleOwner(this)
            .setQuery(picturesQuery, PictureEntity::class.java)
            .build()
        adapter = FirebaseRecyclerPagingAdapter<PictureEntity, PictureViewHolder>(adapterOptions) {
            
        }
    }

    class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val picture: ImageView = itemView.picture_imageView
        val upVotes: TextView = itemView.up_vote_textView
        val downVotes: TextView = itemView.down_vote_textView
    }
}