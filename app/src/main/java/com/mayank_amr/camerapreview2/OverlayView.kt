package com.mayank_amr.camerapreview2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout


/**
 * @Project Camera Preview 2
 * @Created_by Mayank Kumar on 19-06-2021 11:35 PM
 */
open class OverlayView : FrameLayout {
    private var mBackgroundPaint: Paint? = null
    private val mCanvasBackgroundColor = Color.parseColor("#97FFFFFF")


    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!, attrs, defStyleAttr, defStyleRes
    ) {
        init()
    }

    private fun init() {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        mBackgroundPaint = Paint()
        mBackgroundPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }


    override fun onDraw(canvas: Canvas) {
/*
        //Getting the display height and width..
//        var displayHeightMid: Int
//        var displayWidthMid: Int
//        resources.displayMetrics.let { displayMetrics ->
//            displayHeightMid = displayMetrics.heightPixels / 2
//            displayWidthMid = displayMetrics.widthPixels / 2
//        }
        canvas.drawColor(mCanvasBackgroundColor)


//        canvas.drawRect(
//            (width / 4.0f),
//            (height / 4.0f),
//            width - (width / 4.0f),
//            height - (height / 4.0f),
//            mBackgroundPaint!!
//        )


        if (width < height) {
            val top = (height / 2) - (((width - 100) * 230) / 340.0f) / 2
            val bot = (height / 2) + (((width - 100) * 230) / 340.0f) / 2

            canvas.drawRect(left + 50.0f, top, right - 50.0f, bot, mBackgroundPaint!!)

            val myPaint = Paint()
            myPaint.color = Color.WHITE
            myPaint.strokeWidth = 2.0f
            myPaint.style = Paint.Style.STROKE
            canvas.drawRect(left + 50.0f, top, right - 50.0f, bot, myPaint)
        } else {

            val leftPo = (width / 2) - (((height - 50) * 340) / 230.0f) / 2
            val rightPo = (width / 2) + (((height - 50) * 340) / 230.0f) / 2

            canvas.drawRect(leftPo, top + 25.0f, rightPo, bottom - 25.0f, mBackgroundPaint!!)

            val myPaint = Paint()
            myPaint.color = Color.WHITE
            myPaint.strokeWidth = 2.0f
            myPaint.style = Paint.Style.STROKE
            canvas.drawRect(leftPo, top + 25.0f, rightPo, bottom - 25.0f, myPaint)
        }

 */
    }

}

