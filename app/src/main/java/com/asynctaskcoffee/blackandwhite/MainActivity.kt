package com.asynctaskcoffee.blackandwhite

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.asynctaskcoffee.blackandwhite.workers.Eraser
import com.asynctaskcoffee.blackandwhite.workers.EraserListener
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

class MainActivity : AppCompatActivity(), EraserListener {

    lateinit var imageViewForeground: ImageView
    lateinit var imageViewBackground: ImageView
    lateinit var progressBar: ProgressBar

    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        imageViewForeground = findViewById(R.id.imageViewForeground)
        imageViewBackground = findViewById(R.id.imageViewBackground)
        imageViewForeground.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        fileUri = data?.data
                        Eraser.eraseAndReturnResult(
                            BitmapFactory.decodeFile(fileUri?.path),
                            this@MainActivity
                        )
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(
                            this@MainActivity,
                            ImagePicker.getError(data),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Task Cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    override fun onStartProcess() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onEndProcess() {
        progressBar.visibility = View.GONE
    }

    override fun onResultReady(bitmap: Bitmap?) {
        imageViewBackground.setImageURI(fileUri)
        imageViewBackground.colorFilter =
            ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        imageViewForeground.setImageBitmap(bitmap)
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(this@MainActivity, exception?.message.toString(), Toast.LENGTH_SHORT)
            .show()
    }
}