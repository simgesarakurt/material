package com.example.bitirmeprojesi.view

import android.app.AlertDialog
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.model.ImageList
import com.example.bitirmeprojesi.view.ImageAdapter.*
import java.util.*
import kotlin.collections.ArrayList

class ImageAdapter (val c:Context,val imageList:ArrayList<ImageList>):
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private lateinit var mTTs : TextToSpeech
    inner class ImageViewHolder(val v: View):RecyclerView.ViewHolder(v){
        val imgImg=v.findViewById<ImageView>(R.id.imgList)
        val imgName=v.findViewById<TextView>(R.id.imgName)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ) : ImageViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_list,parent,false)
        return ImageViewHolder(v)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        mTTs = TextToSpeech(c){status ->
            if (status==TextToSpeech.SUCCESS){



                val result = mTTs.setLanguage(Locale("tr","TR","TR"))
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result ==TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTs","Dil Desteklenmiyor")
                    }
                    else{
                        holder.v.isEnabled=true
                    }
            }
            else{
                Log.e("TTs","ınitializztion failed")
            }
        }

        val imageList=imageList[position]
        holder.imgImg.setImageResource(imageList.imgImg)
        holder.imgName.text=imageList.imgName
        holder.v.setOnClickListener{
            speak(imageList.imgName)
            showAlpha(imageList.imgImg,imageList.imgName)
        }

    }

    private fun showAlpha(img: Int, name: String) {

        val inflater =LayoutInflater.from(c)
        val setView = inflater.inflate(R.layout.show_item,null)
        val nameImage = setView.findViewById<TextView>(R.id.imgName)
        val imgImage = setView.findViewById<ImageView>(R.id.imgList)
        val btnCancel = setView.findViewById<ImageView>(R.id.btnCancel)
        nameImage.text=name
        imgImage.setImageResource(img)
        val showDialog = AlertDialog.Builder(c)
        showDialog.setCancelable(true)
        showDialog.setView(setView)
        val openDialog =showDialog.create()
        btnCancel.setOnClickListener{openDialog.dismiss()}
        openDialog.show()
    }

    private fun speak(name: String) {
        mTTs.setPitch(1f)
        mTTs.setSpeechRate(1.1f)
        mTTs.speak(name,TextToSpeech.QUEUE_FLUSH,null)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
