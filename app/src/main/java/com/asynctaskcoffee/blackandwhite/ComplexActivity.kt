package com.asynctaskcoffee.blackandwhite

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asynctaskcoffee.blackandwhite.workers.Eraser
import com.asynctaskcoffee.blackandwhite.workers.EraserListener
import com.github.dhaval2404.imagepicker.ImagePicker

class ComplexActivity : AppCompatActivity(), EraserListener {

    lateinit var foregroundImageView: ImageView
    lateinit var backgroundImageView: ImageView
    private var result: Bitmap? = null
    private var fileUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complex)
        initViews()
    }

    fun initViews() {
        foregroundImageView = findViewById(R.id.foregroundImageView)
        backgroundImageView = findViewById(R.id.backgroundImageView)
    }

    fun doBlackAndWhite(view: View) {
        if (fileUri != null) {
            backgroundImageView.setImageURI(fileUri)
            backgroundImageView.colorFilter =
                ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }
    }

    fun doRemoveBackground(view: View) {
        backgroundImageView.clearColorFilter()
        backgroundImageView.setImageResource(0)
    }

    fun doCologneShop(view: View) {
        backgroundImageView.clearColorFilter()
        backgroundImageView.setImageResource(R.drawable.coln_shop)
    }

    fun doParisShop(view: View) {
        backgroundImageView.clearColorFilter()
        backgroundImageView.setImageResource(R.drawable.eiffel_shop)
    }

    fun selectImage(view: View) {
        pickImage()
    }

    override fun onStartProcess() {

    }

    override fun onEndProcess() {

    }

    override fun onResultReady(bitmap: Bitmap?) {
        result = bitmap
        foregroundImageView.setImageBitmap(bitmap)
    }

    override fun onError(exception: Exception?) {

    }

    private fun pickImage() {
        ImagePicker.with(this)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        fileUri = data?.data
                        Eraser.eraseAndReturnResult(
                            BitmapFactory.decodeFile(fileUri?.path),
                            this@ComplexActivity
                        )
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(
                            this@ComplexActivity,
                            ImagePicker.getError(data),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Toast.makeText(this@ComplexActivity, "Task Cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    fun doEgShop(view: View) {
        backgroundImageView.clearColorFilter()
        backgroundImageView.setImageResource(R.drawable.egypt_shop)
    }
}