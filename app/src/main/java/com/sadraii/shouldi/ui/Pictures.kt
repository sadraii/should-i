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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sadraii.shouldi.R
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import com.sadraii.shouldi.viewmodel.PicturesViewModel
import kotlinx.android.synthetic.main.fragment_pictures.view.*
import kotlinx.android.synthetic.main.item_picture.view.*

class Pictures : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: FirestorePagingAdapter<PictureEntity, PictureViewHolder>
    private val myPicturesViewModel by viewModels<PicturesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val query = Firebase.firestore
            .collection(UserRepository.USERS_PATH)
            .document(user!!.uid)
            .collection(PictureRepository.PICTURES_PATH)
            .orderBy("created", Query.Direction.DESCENDING)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .setPageSize(10)
            .build()
        // TODO Parser?
        val options = FirestorePagingOptions.Builder<PictureEntity>()
            .setLifecycleOwner(this)
            .setQuery(query, config, PictureEntity::class.java)
            .build()

        viewAdapter = object : FirestorePagingAdapter<PictureEntity, PictureViewHolder>(options) {

            val storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
                val item = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_picture, parent, false)
                return PictureViewHolder(item)
            }

            override fun onBindViewHolder(holder: PictureViewHolder, position: Int, model: PictureEntity) {
                holder.yesVotes.text = model.yesVotes.toString()
                holder.noVotes.text = model.noVotes.toString()
                holder.caption.text = model.caption
                Log.d(TAG, "recycling yes: ${model.yesVotes}")

                // TODO Move to Repo
                val pictureRef = storageRef.child(model.pictureUrl)

                Log.d(TAG, "Glide loading ${model.pictureUrl}")
                Glide.with(holder.itemView.context)
                    .load(pictureRef)
                    .placeholder(R.drawable.ic_photo_placeholder_24dp)
                    .into(holder.picture)

                holder.itemView.setOnClickListener {
                    findNavController().navigate(
                        PicturesDirections.actionMyPicturesFragmentToPictureDetailFragment(model)
                    )
                }
            }

            override fun onError(e: Exception) {
                Log.e(TAG, "FirestorePagingAdapter error: ${e.message}")
            }
        }

        viewManager = LinearLayoutManager(requireContext())
        recyclerView = view.my_pictures_recyclerView.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }

    class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val picture: ImageView = view.picture_imageView
        val yesVotes: TextView = view.yes_vote_textView
        val noVotes: TextView = view.no_vote_textView
        val caption: TextView = view.caption_textView
    }
}





