package com.mayank_amr.camerapreview2

import android.content.Context
import android.graphics.*

/**
 * @Project Camera Preview 2
 * @Created_by Mayank Kumar on 21-06-2021 08:15 AM
 */
class OverlayWithRectangle(
    context: Context,
    private var widthRatioOfDocument: Float,
    private var heightRatioOfDocument: Float
) : OverlayView(context) {
    private var mBackgroundPaint: Paint? = null
    private val mCanvasBackgroundColor = Color.parseColor("#97FFFFFF")


    override fun onDraw(canvas: Canvas) {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        mBackgroundPaint = Paint()
        mBackgroundPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        canvas.drawColor(mCanvasBackgroundColor)

        if (width < height) { // Screen is in Landscape Mode..

            // Here height is more than width so it is decided according to width..

            val topPositionToDraw =
                (height / 2) - (((width - 100) * heightRatioOfDocument) / widthRatioOfDocument) / 2
            val bottomPositionToDraw =
                (height / 2) + (((width - 100) * heightRatioOfDocument) / widthRatioOfDocument) / 2

            // It draw the clear part..
            canvas.drawRect(
                left + 50.0f,
                topPositionToDraw,
                right - 50.0f,
                bottomPositionToDraw,
                mBackgroundPaint!!
            )


            // It draw the Outline on the Clear Part..
            val outlineRectanglePaint = Paint()
            outlineRectanglePaint.color = Color.WHITE
            outlineRectanglePaint.strokeWidth = 2.0f
            outlineRectanglePaint.style = Paint.Style.STROKE
            canvas.drawRect(
                left + 50.0f,
                topPositionToDraw,
                right - 50.0f,
                bottomPositionToDraw,
                outlineRectanglePaint
            )

        } else { // Screen in Portrait Mode
            // Here width is more than height so it is decided according to height..

            val leftPositionToDraw =
                (width / 2) - (((height - 50) * widthRatioOfDocument) / heightRatioOfDocument) / 2
            val rightPositionToDraw =
                (width / 2) + (((height - 50) * widthRatioOfDocument) / heightRatioOfDocument) / 2


            canvas.drawRect(
                leftPositionToDraw,
                top + 25.0f,
                rightPositionToDraw,
                bottom - 25.0f,
                mBackgroundPaint!!
            )

            // It draw the Outline on the Clear Part in Landscape..
            val outlineRectanglePaint = Paint()
            outlineRectanglePaint.color = Color.WHITE
            outlineRectanglePaint.strokeWidth = 2.0f
            outlineRectanglePaint.style = Paint.Style.STROKE

            canvas.drawRect(
                leftPositionToDraw,
                top + 25.0f,
                rightPositionToDraw,
                bottom - 25.0f,
                outlineRectanglePaint
            )
            outlineRectanglePaint.reset()

        }
    }
}