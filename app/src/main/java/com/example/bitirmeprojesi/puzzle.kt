package com.example.bitirmeprojesi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView

import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_puzzle.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class puzzle : AppCompatActivity() {

    var mCurrentPhotoPath:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)
    }

    fun onImageCameraClicked(view: View?) {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager)!=null){
            var photofile: File? =null
            try {
                photofile=createImageFile()

            }
            catch (e:IOException){
                //e.printStackTrace()
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
            if (photofile != null){
                val photoUri = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName + ".fileprovider",
                    photofile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }

        }
    }
    @Throws(IOException ::class)
    private fun createImageFile(): File? {
        if (ContextCompat.checkSelfPermission(
                this,Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!=PackageManager.PERMISSION_GRANTED){
            //PERMISSION NOT GRANTED İNİTİATE REQUEST
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        }
        else{
            //create an image file name
            val timestamp=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

            val imageFileName= "JPEG_"+ timestamp + "_"
            val storageDir=Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            val image =File.createTempFile(
                imageFileName,".jpg",storageDir
            )
            mCurrentPhotoPath = image.absolutePath //save this to  use in the intent
            return image
        }
        return null

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array< String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE ->{
                if (grantResults.size > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onImageCameraClicked(View(this))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val intent = Intent(this, PuzzleActivity::class.java)
            intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath)
            startActivity(intent)

        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            val uri = data!!.data
            val intent = Intent(this, PuzzleActivity::class.java)
            intent.putExtra("mCurrentPhotoUri", uri.toString())
            startActivity(intent)
        }
    }

        fun onImageGalleryClicked(view: View?) {
            if(ContextCompat.checkSelfPermission(
                    this,Manifest.permission.READ_EXTERNAL_STORAGE
                )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this,arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
                )
            }
            else{
                val intent=Intent(Intent.ACTION_GET_CONTENT)
                intent.type="image/*"
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
            }

        }

        companion object{
            private const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE =2
            private const val REQUEST_IMAGE_CAPTURE =1

            const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE =3
            const val REQUEST_IMAGE_GALLERY=4
        }
    }








