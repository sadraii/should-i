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

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sadraii.shouldi.R
import com.sadraii.shouldi.viewmodel.CaptionViewModel
import kotlinx.android.synthetic.main.fragment_caption.*

class CaptionFragment : Fragment() {

    private val safeArgs: CaptionFragmentArgs by navArgs()
    private val captionViewModel: CaptionViewModel by viewModels()

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
            overlayText(picture)
            captionViewModel.addPicture(picture)
            captionViewModel.pictureAdded.observe(viewLifecycleOwner, Observer { picAdded ->
                if (picAdded) {
                    findNavController().navigate(R.id.action_captionFragment_to_myPicturesFragment)
                }
            })

        }
    }

    private fun overlayText(picture: Bitmap) {
        val canvas = Canvas(picture)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 100F
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        canvas.drawBitmap(picture, 0F, 0F, paint)
        canvas.drawText(caption_editText.text.toString(), 10F, 1300F, paint)

        // picture_imageView.background = picture.toDrawable(resources)
    }
}




