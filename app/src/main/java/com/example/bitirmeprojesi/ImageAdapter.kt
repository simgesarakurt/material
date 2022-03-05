package com.example.bitirmeprojesi

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.bitirmeprojesi.model.ImageList
import java.io.IOException

class ImageAdapter(private val mContext: Context):BaseAdapter()
{

    private val am: AssetManager
    private var files:Array<String>?=null

    override fun getCount(): Int {
        return files!!.size}

    override fun getItem(position: Int): Any?{
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertview: View?, parent:ViewGroup): View? {
        val v= LayoutInflater.from(mContext).inflate(R.layout.grid_element,null)
        val imageView=v.findViewById<ImageView>(R.id.gridImageView)
        imageView.setImageBitmap(null)



        imageView.post{

            object : AsyncTask<Void?, Void?, Void?>() {
                private var bitmap: Bitmap?=null
                protected override fun doInBackground(vararg p0: Void?): Void?{

                    bitmap=getPicFromAsset(imageView,files!![position])
                    return null
                }
                override fun onPostExecute(aVoid: Void?) {
                    super.onPostExecute(aVoid)
                    imageView.setImageBitmap(bitmap)
                }
            }.execute()
        }

        return v

    }

    private fun getPicFromAsset(imageView: ImageView, assetName: String): Bitmap? {
        val targetW=imageView.width
        val targetH=imageView.height

        return if (targetW==0 || targetH==0){
            null
        }else try {
            val `is`=am.open("img/$assetName")
            val bmOptions=BitmapFactory.Options()
            bmOptions.inJustDecodeBounds=true
            BitmapFactory.decodeStream(`is`,
                Rect(-1,-1,-1,-1),
                bmOptions)
            val photoW=bmOptions.outWidth
            val photoH=bmOptions.outHeight

            //peternine how much to scale down te imageview
            val scaleFactor =Math.min(photoW/ targetW ,photoH/ targetH)

            `is`.reset()
            //decode the image file in to a bitmap sized to fill the view

            bmOptions.inJustDecodeBounds=false
            bmOptions.inSampleSize=scaleFactor
            bmOptions.inPurgeable=true
            BitmapFactory.decodeStream(
                `is`,
                Rect(-1,-1,-1,-1),
                bmOptions)

        }catch (e: IOException){
            e.printStackTrace()
            null
        }

    }
    init{
        am=mContext.assets
        try {
            files=am.list("img")
        }catch (e:IOException){
            e.printStackTrace()
    }
}
}


