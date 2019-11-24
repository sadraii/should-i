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
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sadraii.shouldi.R
import com.sadraii.shouldi.viewmodel.CaptionViewModel
import kotlinx.android.synthetic.main.fragment_caption.*

class CaptionFragment internal constructor() : Fragment() {

    private val safeArgs: CaptionFragmentArgs by navArgs()
    private val captionViewModel by viewModels<CaptionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_caption, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val picture = safeArgs.picture

        picture_imageView.background = picture.toDrawable(resources)

        send_imageButton.setOnClickListener {
            captionViewModel.addPicture(picture)
            findNavController().navigate(R.id.action_captionFragment_to_myPicturesFragment)
        }
    }
}




