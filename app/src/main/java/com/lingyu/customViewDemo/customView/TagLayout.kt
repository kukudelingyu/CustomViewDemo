package com.lingyu.customViewDemo.customView

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.lingyu.customViewDemo.R

/**
 * Created by lingyu on  2021/4/9
 *
 * description:
 */
class TagLayout  @JvmOverloads constructor(context: Context,  attrs: AttributeSet? = null ,def:Int = 0): ViewGroup(context,attrs,def) {

    private val childBoundList by lazy {
        mutableListOf<Rect>()
    }

    private var horizontalPadding = 0

    private var verticalPadding = 0

    init {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagLayout)

        horizontalPadding = typedArray.getDimensionPixelSize(R.styleable.TagLayout_horizental_padding,15)

        verticalPadding = typedArray.getDimensionPixelSize(R.styleable.TagLayout_vertical_padding,15)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthUsed = horizontalPadding
        var heightUsed = verticalPadding
        var maxLineHeight = 0
        for (i in 0 until childCount){
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, heightMeasureSpec, 0)
            maxLineHeight = maxLineHeight.coerceAtLeast(getChildAt(i).measuredHeight)
            if(widthUsed+getChildAt(i).measuredWidth+horizontalPadding>measuredWidth){
                widthUsed = horizontalPadding
                heightUsed+=(maxLineHeight+verticalPadding)
            }
            val rect = Rect()
            rect.set(widthUsed, heightUsed, widthUsed + getChildAt(i).measuredWidth+horizontalPadding, heightUsed+getChildAt(i).measuredHeight+verticalPadding)
            childBoundList.add(rect)
            widthUsed+= getChildAt(i).measuredWidth+horizontalPadding
        }
        setMeasuredDimension(widthMeasureSpec,heightUsed+maxLineHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount){
            var rect = childBoundList[i]
            if(getChildAt(i) != selectView){
                getChildAt(i).layout(rect.left,rect.top,rect.right,rect.bottom)
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context,attrs)
    }
    var selectView:View? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var currentX = event?.x
        var currentY = event?.y


        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN->{
                for (i in 0 until childCount){
                    if (isInBound(currentX,currentY,getChildAt(i))){
                        selectView = getChildAt(i)
                    }
                }
            }

            MotionEvent.ACTION_MOVE->{
               if(selectView != null){
                   selectView?.layout((currentX-selectView!!.width/2).toInt(),(currentY-selectView!!.height/2).toInt(),(currentX+selectView!!.width/2).toInt(),(currentY+selectView!!.height/2).toInt())
                   requestLayout()
               }
            }
            MotionEvent.ACTION_UP->{
                selectView = null
            }

        }
        return true
    }

    private fun isInBound(posiX:Float,posiY:Float,child:View):Boolean{
        return posiX > child.left && posiX < child.right && posiY > child.top && posiY < child.bottom
    }
}