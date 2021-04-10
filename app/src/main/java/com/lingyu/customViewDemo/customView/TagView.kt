package com.lingyu.customViewDemo.customView

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.lingyu.customViewDemo.CommonUtil
import com.lingyu.customViewDemo.R

/**
 * Created by lingyu on  2021/4/10
 *
 * description:
 */
class TagView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, def:Int = 0): View(context,attrs,def) {

    private val TAG = "TagView"

    val HORIZENTAL_PADDING = CommonUtil.dp2px(10f)

    var VERTICAL_PADDING = CommonUtil.dp2px(5f)

    private val textPaint:Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private val boardPaint:Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    var textColor = 0

    var textSize = 0

    var content:String? = null

    var boardWidth:Float = 0f

    var boardColor = 0

    init {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView)

        textColor = typedArray.getColor(R.styleable.TagView_tag_textColor,Color.parseColor("#333333"))

        textSize = typedArray.getDimensionPixelSize(R.styleable.TagView_tag_textSize,40)

        content = typedArray.getString(R.styleable.TagView_tag_text)

        boardWidth = typedArray.getDimension(R.styleable.TagView_tag_board_width,3f)

        boardColor = typedArray.getColor(R.styleable.TagView_tag_board_color,Color.parseColor("#999999"))

        textPaint.color = textColor!!

        textPaint.textSize = textSize!!.toFloat()

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)

        var height =  MeasureSpec.getSize(heightMeasureSpec)

        var widthMode= MeasureSpec.getMode(widthMeasureSpec)

        var heightMod = MeasureSpec.getMode(heightMeasureSpec)

        if(widthMode == MeasureSpec.AT_MOST){
            width = (textPaint.measureText(content)+HORIZENTAL_PADDING*2).toInt()
        }

        if(heightMod == MeasureSpec.AT_MOST){
            height  = ((textPaint.fontMetrics.bottom - textPaint.fontMetrics.top)+VERTICAL_PADDING*2).toInt()
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width,widthMode),MeasureSpec.makeMeasureSpec(height,heightMod))
    }

    override fun onDraw(canvas: Canvas) {
        var rect = Rect()
        textPaint.getTextBounds(content?:"",0,(content?:"").length,rect)
        Log.e(TAG, "onDraw: measuredHeight/2 = ${measuredHeight/2}  rect.left = ${rect.left}  rect.top = ${rect.top}  rect.right = ${rect.right}  rect.bottom = ${rect.bottom}  " )
        canvas.drawText(content?:"",0,(content?:"").length,HORIZENTAL_PADDING,measuredHeight/2.toFloat()+(rect.bottom-rect.top)/2,textPaint)

        var rectF = RectF(boardWidth,boardWidth,measuredWidth.toFloat()-boardWidth,measuredHeight.toFloat()-boardWidth)

        boardPaint.style = Paint.Style.STROKE
        boardPaint.strokeWidth = boardWidth
        boardPaint.color = boardColor
        canvas.drawRoundRect(rectF,15f,15f,boardPaint)
    }
}