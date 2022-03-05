package com.example.bitirmeprojesi

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_puzzle.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random




class PuzzleActivity : AppCompatActivity() {
    var pieces:ArrayList<PuzzlePiece?>?=null
    var mCurrentPhotoPath:String?=null
    var mCurrentPhotoUri:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle2)


        val layout=findViewById<RelativeLayout>(R.id.layout)
        val imageView=findViewById<ImageView>(R.id.imageView)

        val intent=intent
        val assetName =intent.getStringExtra("assetName")
        mCurrentPhotoPath=intent.getStringExtra("mCurrentPhotoPath")
        mCurrentPhotoUri=intent.getStringExtra("mCurrentPhotoUri")


        //run image related code after the view was laid out
        //to have all dimensions calculated

        imageView.post{
            if (assetName != null){

                setPicFromAsset(assetName,imageView)
            }
            else if (mCurrentPhotoPath !=null){
                setPicFromPhotoPath(mCurrentPhotoPath!!,imageView)
            }
            else if (mCurrentPhotoUri !=null){
imageView.setImageURI(Uri.parse(mCurrentPhotoUri))
            }
            pieces=splitImage()
            val touchListener=TouchListener(this@PuzzleActivity)

            //shuffle pieces order
            Collections.shuffle(pieces)
            for (piece in pieces!!){
                piece!!.setOnTouchListener(touchListener)
                layout.addView(piece)

                //randomize position , on the bottom of the screen
                val lParams=piece.layoutParams as RelativeLayout.LayoutParams
                lParams.leftMargin=Random.nextInt(
                    layout.width - piece.pieceWidth
                )

                lParams.topMargin=layout.height - piece.pieceHeight
                piece.layoutParams=lParams

            }

        } }



    private fun setPicFromAsset(assetName: String, imageView: ImageView) {

        val targetW = imageView.width
        val  targetH=imageView.height
        val am=assets

        try {

            val `is` = am.open("img/$assetName")

            //get the dimensions of the bitmap

            val bmOptions =BitmapFactory.Options()
            BitmapFactory.decodeStream(
                `is`,Rect(-1,-1,-1,-1),bmOptions
            )

            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            //determine how much to scale down the image
            val scaleFactor =Math.min(
                photoW/targetW,photoH/targetH
            )


            //decode the image file into a bitmap sized to fill the view
            bmOptions.inJustDecodeBounds=false
            bmOptions.inSampleSize=scaleFactor
            bmOptions.inPurgeable=true
            val bitmap =BitmapFactory.decodeStream(
                `is`, Rect(-1,-1,-1,-1),bmOptions
            )
            imageView.setImageBitmap(bitmap)




        }catch (e:IOException){
            e.printStackTrace()
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }


    }

    private fun splitImage():ArrayList<PuzzlePiece?> {

        val piecesNumber = 4
        val rows = 2
        val cols = 2
        val imageView = findViewById<ImageView>(R.id.imageView)
        val pieces = ArrayList<PuzzlePiece?>(piecesNumber)


        //Get the scaled bitmap of the source image
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val dimensions = getBitmapPositionInsideImageView(imageView)

        val scaledBitmapLeft = dimensions[0]
        val scaledBitmapTop = dimensions[1]
        val scaledBitmapWidth = dimensions[2]
        val scaledBitmapHeight = dimensions[3]


        val croppedImageWidth = scaledBitmapWidth - 2 * Math.abs(scaledBitmapLeft)
        val croppedImageHeight = scaledBitmapHeight - 2 * Math.abs(scaledBitmapTop)


        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap, scaledBitmapWidth, scaledBitmapHeight, true
        )
        val croppedBitmap = Bitmap.createBitmap(
            scaledBitmap,
            Math.abs(scaledBitmapLeft),
            Math.abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight
        )
        //calculate the width and height of the piece
        val pieceWidth = croppedImageWidth / cols
        val pieceHeight = croppedImageHeight / rows

//create each bitmap piece and it to the resulting array
        var yCoord = 0
        for (row in 0 until rows) {

            var xCoord = 0
            for (col in 0 until cols) {
                //calculate offset for each
                var offsetX = 0
                var offsetY = 0
                if (col > 0) {
                    offsetX = pieceWidth / 3
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3

                }
                val pieceBitmap = Bitmap.createBitmap(
                    croppedBitmap, xCoord - offsetX, yCoord - offsetY,
                    pieceWidth + offsetX, pieceHeight + offsetY
                )
                val piece = PuzzlePiece(applicationContext)
                piece.setImageBitmap(pieceBitmap)
                piece.xCoord = xCoord - offsetX + imageView.left
                piece.yCoord = yCoord - offsetY + imageView.top

                piece.pieceWidth = pieceWidth + offsetX
                piece.pieceHeight = pieceHeight + offsetY

                //this bitmap will hold our final puzzle piece Image
                val puzzlePiece = Bitmap.createBitmap(
                    pieceWidth + offsetX, pieceHeight + offsetY,
                    Bitmap.Config.ARGB_8888
                )
                //draw path
                val bumpSize = pieceHeight / 4
                val canvas = Canvas(puzzlePiece)
                val path = Path()
                path.moveTo(offsetX.toFloat(), offsetY.toFloat())

                if (row == 0) {
                    //top side piece
                    path.lineTo(
                        pieceBitmap.width.toFloat(),
                        offsetY.toFloat()
                    )
                } else {
                    //to bump

                    path.lineTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 3).toFloat(),
                        offsetY.toFloat()
                    )
                    path.cubicTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 6).toFloat(),
                        (offsetY - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 6 * 5).toFloat(),
                        (offsetY - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 3 * 2).toFloat(),
                        offsetY.toFloat()
                    )
                    path.lineTo(pieceBitmap.width.toFloat(), offsetY.toFloat())
                }

                if (col == cols - 1) {//ride side piece
                    path.lineTo(
                        pieceBitmap.width.toFloat(),
                        pieceBitmap.height.toFloat()
                    )
                } else {
                    //right bump
                    path.lineTo(
                        pieceBitmap.width.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3).toFloat()
                    )
                    path.cubicTo(
                        (pieceBitmap.width - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6).toFloat(),
                        (pieceBitmap.width - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6 * 5).toFloat(),
                        pieceBitmap.width.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3 * 2).toFloat()
                    )

                    path.lineTo(
                        pieceBitmap.width.toFloat(),
                        pieceBitmap.height.toFloat()
                    )


                }


                if (row == rows - 1) {
                    //bottom side piece
                    path.lineTo(
                        offsetX.toFloat(), pieceBitmap.height.toFloat()
                    )
                } else {
                    //bottom bump
                    path.lineTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 3 * 2).toFloat(),
                        pieceBitmap.height.toFloat()
                    )
                    path.cubicTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 6 * 5).toFloat(),
                        (pieceBitmap.height - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 6).toFloat(),
                        (pieceBitmap.height - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 3).toFloat(),
                        pieceBitmap.height.toFloat()
                    )
                    path.lineTo(
                        offsetX.toFloat(),
                        pieceBitmap.height.toFloat()
                    )

                }

                if (col == 0) {
                    // left side piece
                    path.close()
                } else {
                    //left bump
                    path.lineTo(
                        offsetX.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3 * 2).toFloat()
                    )
                    path.cubicTo(
                        (offsetX - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6 * 5).toFloat(),
                        (offsetX - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6).toFloat(),
                        offsetX.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3).toFloat()
                    )
                    path.close()

                }

                //mask the piece
                val paint = Paint()
                paint.color = -0x1000000
                paint.style = Paint.Style.FILL
                canvas.drawPath(path, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(pieceBitmap, 0f, 0f, paint)

                //draw a white border
                var border = Paint()
                border.color = -0x7f000001
                border.style = Paint.Style.STROKE
                border.strokeWidth = 8.0f
                canvas.drawPath(path, border)

                //draw a black border
                border = Paint()
                border.color = -0x80000000
                border.style = Paint.Style.STROKE
                border.strokeWidth = 3.0f
                canvas.drawPath(path, border)

                //set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece)
                pieces.add(piece)
                xCoord += pieceWidth

            }
            yCoord += pieceHeight
        }
        return pieces
    }


    private fun getBitmapPositionInsideImageView(imageView: ImageView?): IntArray {

        val ret = IntArray(4)
        if (imageView == null || imageView.drawable == null)
            return ret


        //get image dimensions
        //get image matrix values and place them in array
        val f=FloatArray(9)

        imageView.imageMatrix.getValues(f)

        //extract the scalevalues using the constans(if aspect ratio maintained scaleX

        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        //get the drawable (could also get the bitmap the drawable and getWidth/getHeight

        val d =imageView.drawable
        val origW =d.intrinsicWidth
        val origH =d.intrinsicHeight

        //calculate the actual dimensions

        val actW = Math.round(origW * scaleX)
        val actH = Math.round(origH * scaleY)

        ret[2]=actW
        ret[3]=actH


        //get image position
        //we assume that the image is centered into imageview

        val imgViewW = imageView.width
        val imgViewH = imageView.height

        val top =(imgViewH - actH)/2
        val left =(imgViewW - actW)/2

        ret[0]=left
        ret[1]=top

        return ret



    }

   fun checkGameOver() {
       if (isGameOver) {
           AlertDialog.Builder(this@PuzzleActivity)
               .setTitle("KAZANDIN!!!!")

               .setMessage("YENİ OYUN OYNAMAK İSTER MİSİN?")
               .setPositiveButton("EVET") {
                       dialog, _ ->
                   finish()
                   dialog.dismiss()
               }
               .setNegativeButton("HAYIR") { dialog, _ ->
                   finish()
                   dialog.dismiss()
               }

               .create()
               .show()
       }
   }





    private val isGameOver :Boolean
    private get (){
        for(piece in pieces!!){
            if (piece!!.canMove) {
                return false
            }
        }
        return true
    }

    private fun setPicFromPhotoPath(mCurrentPhotoPath: String, imageView: ImageView) {

        //get the dimensions of the view
        val targetW = imageView.width
        val targetH = imageView.height

        //get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()

        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions)

        val photoW=bmOptions.outWidth
        val photoH=bmOptions.outHeight

        //determine how much to scale down the image

        val scaleFactor=Math.min(
            photoW/targetW,photoH/targetH
        )

        //decode the image file into a bitmap sized to fill the view

        bmOptions.inJustDecodeBounds=false
        bmOptions.inSampleSize=scaleFactor
        bmOptions.inPurgeable=true
        val bitmap=BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions)

        var rotatedBitmap=bitmap


        //rotate bitmap if needed

        try {
            val ei=ExifInterface(mCurrentPhotoPath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 ->
                    rotatedBitmap = rotateImage(bitmap,90f)

                ExifInterface.ORIENTATION_ROTATE_180 ->
                    rotatedBitmap = rotateImage(bitmap,180f)

                ExifInterface.ORIENTATION_ROTATE_270 ->
                    rotatedBitmap = rotateImage(bitmap,270f)

            }

        }catch (e:IOException){
            //e.printStackTrace()
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

        imageView.setImageBitmap(rotatedBitmap)

    }
    companion object{
        fun rotateImage(source:Bitmap,angle:Float):Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)

            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height, matrix, true
            )
        }

    }
}
