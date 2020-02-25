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

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doAfterTextChanged
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
        upload_button.isEnabled = false

        upload_button.setOnClickListener { v ->
            hideKeyboardAndUploadPicture(v, picture)
        }

        caption_editText.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    hideKeyboardAndUploadPicture(v, picture)
                    true
                }
                else -> false
            }
        }
        caption_editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        hideKeyboardAndUploadPicture(v, picture)
                        return@setOnKeyListener true
                    }
                }
            }
            false
        }

        caption_editText.doAfterTextChanged { text ->
            upload_button.isEnabled = !text.isNullOrBlank()
        }
    }

    private fun hideKeyboardAndUploadPicture(view: View, picture: Bitmap) {
        hideKeyboardFrom(view)
        caption_progress.visibility = View.VISIBLE
        caption_editText.isEnabled = false
        upload_button.isEnabled = false
        captionViewModel.addPicture(picture, caption_editText.text.toString())
        captionViewModel.pictureAdded.observe(viewLifecycleOwner, Observer { picAdded ->
            if (picAdded) {
                findNavController().navigate(R.id.action_captionFragment_to_myPicturesFragment)
            }
        })
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}






