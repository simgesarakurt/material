package com.example.bitirmeprojesi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitirmeprojesi.model.ImageList
import com.example.bitirmeprojesi.view.ImageAdapter

class nesnetanima : AppCompatActivity() {
private lateinit var ImageAdapter:ImageAdapter
private lateinit var imgRecycler: RecyclerView
private lateinit var imgList: ArrayList<ImageList>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nesnetanima)

        imgList= ArrayList()
        imgRecycler=findViewById(R.id.imgRecycler)
        ImageAdapter= ImageAdapter(this,imgList)
        val layoutM=GridLayoutManager(this,2)
        imgRecycler.layoutManager=layoutM
        imgRecycler.adapter=ImageAdapter

        imageList()
    }

    private fun imageList() {
        imgList.add(ImageList(R.drawable.armchair,"KOLTUK"))
        imgList.add(ImageList(R.drawable.chair,"SANDALYE"))
        imgList.add(ImageList(R.drawable.dog,"KÖPEK"))
        imgList.add(ImageList(R.drawable.laptop,"BİLGİSAYAR"))
        imgList.add(ImageList(R.drawable.moon,"AY"))
        imgList.add(ImageList(R.drawable.pillow,"YASTIK"))
        imgList.add(ImageList(R.drawable.sheep,"KOYUN"))
        imgList.add(ImageList(R.drawable.stars,"YİLDİZ"))
        imgList.add(ImageList(R.drawable.sun,"GÜNEŞ"))
        imgList.add(ImageList(R.drawable.table,"MASA"))
        imgList.add(ImageList(R.drawable.tv,"TELEVİZYON"))
        imgList.add(ImageList(R.drawable.cow,"İNEK"))
        imgList.add(ImageList(R.drawable.fly,"UÇAK"))
        imgList.add(ImageList(R.drawable.goat,"KEÇİ"))
        imgList.add(ImageList(R.drawable.helicopter,"HELİKOPTER"))
        imgList.add(ImageList(R.drawable.horse,"AT"))
        imgList.add(ImageList(R.drawable.tractor,"TRAKTÖR"))
        imgList.add(ImageList(R.drawable.boat,"GEMİ"))
        imgList.add(ImageList(R.drawable.corn,"MISIR"))
        imgList.add(ImageList(R.drawable.duck,"ÖRDEK"))
        imgList.add(ImageList(R.drawable.elephant,"FİL"))
        imgList.add(ImageList(R.drawable.rainbow,"GÖKKUŞAĞI"))
        imgList.add(ImageList(R.drawable.squirrel,"SİNCAP"))
        imgList.add(ImageList(R.drawable.traffic,"TRAFİK IŞIĞI"))
        imgList.add(ImageList(R.drawable.train,"TREN"))
        imgList.add(ImageList(R.drawable.tree,"AĞAÇ"))
        imgList.add(ImageList(R.drawable.umbrella,"ŞEMSİYE"))




    }
}