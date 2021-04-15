package com.lingyu.customViewDemo.customView;

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.lingyu.customViewDemo.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by lingyu on  2021/4/9
 *
 * description: 一个仪表盘的自定义view
 */
class DashBoardView @JvmOverloads constructor(val mContext: Context, val attrs:AttributeSet?=null,val defStyleAttr:Int = 0): View(mContext,attrs, defStyleAttr) {

    var paint:Paint? = null
    val RADIUS:Int = 180

    val startDegree = 315.0
    val endDegree = 315.0

    private var progress = 0
        set(value) {
            field = value
            invalidate()
        }


    private val Float.dp2px
            get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this,Resources.getSystem().displayMetrics)

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.color = resources.getColor(R.color.teal_200)
        paint?.style = Paint.Style.STROKE
        paint?.strokeWidth = 2f.dp2px
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //表盘的rect
        var oval = RectF(width/2-2*RADIUS.toFloat(),height/2-2*RADIUS.toFloat(),width/2+2*RADIUS.toFloat(),height/2+2*RADIUS.toFloat())
        canvas?.drawArc(oval,135f,270f,false,paint!!)
        //一个刻度的path
        val smallRectPath = Path()
        val smallRect = RectF(0f,0f,2f.dp2px,8f.dp2px)
        smallRectPath.addRect(smallRect,Path.Direction.CW)
        //测量弧的长度
        val pathMeasure = PathMeasure()
        //表盘弧度的path
        val arcPath = Path()
        arcPath.addArc(oval,135f,270f)
        pathMeasure.setPath(arcPath,false)
        //表盘弧线的长度
        val dashLength = pathMeasure.length
        //PathDashPathEffect 与 DashPathEffect 类似，不过使用设定的path来填充path，而不是直接以虚线来填充path
        //总共20个刻度
        //这里Style如果使用MORPH最后一个刻度会不显示 ，猜测是因为要做平滑处理的缘故
        paint?.pathEffect = PathDashPathEffect(smallRectPath,(dashLength-2f.dp2px)/20,0f,PathDashPathEffect.Style.ROTATE)
        canvas?.drawArc(oval,135f,270f,false,paint!!)
        paint?.pathEffect = null
        //画指针
        //指针的起始点就是表盘的圆心，而终点是不固定的。但是由于知道起始点和指针的长度，以起点为坐标轴原点，指针和x轴为夹角α，用三角函数可以算出终点的（x = sinα*R,y = cosα*R）

        var sweepAngle = startDegree- 270.0/100*progress
        val endX = (sin(Math.toRadians(sweepAngle))*(RADIUS*1.5f)).toFloat()
        val endY = (cos(Math.toRadians(sweepAngle))*(RADIUS*1.5f)).toFloat()
        canvas?.drawLine(width/2f,height/2f,width/2+endX,height/2+endY,paint!!)
    }

}