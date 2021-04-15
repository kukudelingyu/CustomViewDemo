package com.lingyu.customViewDemo.customView

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by lingyu on  2021/4/14
 *
 * description:
 */
class LineChartView @JvmOverloads constructor(val mContext: Context, val attrs: AttributeSet?=null, val defStyleAttr:Int = 0): View(mContext,attrs, defStyleAttr){

    private val Float.dp2px
        get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, Resources.getSystem().displayMetrics)

    private val MARGIN = 20f.dp2px


    private val markPaint:Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private val horizontalLinePaint:Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private val linePaint:Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private var averageScorePercent = 70
    //滑动的偏移量
    private var scrollOffset:Float = 0f
    //滑动过后记住当前的偏移量
    private var lastMoveOffset:Float = 0f

    private val verticalMark:List<String> = listOf("100","80","60","40","20","0")
    private val horizontalMark:List<String> = listOf("第一讲","第二讲","第三讲","第四讲","第五讲","第六讲","第七讲","第八讲","第九讲","第十讲","第十一讲","第十二讲")
    private val percentList:List<Int> = listOf(25,49,30,90,88,66,70,100,75,10,45,66)

    init {
        markPaint.color = Color.parseColor("#333333")
        markPaint.strokeWidth = 1f.dp2px
        markPaint.textSize = 10f.dp2px

        horizontalLinePaint.color = Color.parseColor("#d6d6d6")
        horizontalLinePaint.strokeWidth = 1f.dp2px
        horizontalLinePaint.style = Paint.Style.STROKE
        horizontalLinePaint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)

        linePaint.color = Color.parseColor("#ff7400")
        linePaint.strokeWidth = 1.5f.dp2px
        linePaint.style = Paint.Style.STROKE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)

        when(heightMode){
            MeasureSpec.AT_MOST->{
                height = 100f.dp2px.toInt()
            }
            MeasureSpec.UNSPECIFIED->{
                height = 100f.dp2px.toInt()
            }
        }
        if(horizontalMark.size<= 9){
            setMeasuredDimension(Resources.getSystem().displayMetrics.widthPixels,height)
        }else{
            setMeasuredDimension(Resources.getSystem().displayMetrics.widthPixels/9*horizontalMark.size,height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawMark(canvas)
    }
    //第一条横线的Y坐标
    private var startPointY = 0f
    //最后一条横线的Y坐标  相减得出distance * 百分比 = 平均分的Y坐标
    private var endPointY = 0f
    //需要滑动的区域
    val contentRect:Rect by lazy {
        Rect()
    }
    //除纵轴内容外  屏幕所见区域图标的宽度
    private var maxScrollOffset = 0f
    private fun drawMark(canvas: Canvas){
        maxScrollOffset = measuredWidth.toFloat()-Resources.getSystem().displayMetrics.widthPixels
        //纵轴起点的Y坐标
        var startVerticalY = MARGIN
        //纵轴数字的最大宽度
        var maxVerticalMarkWidth = 0
        verticalMark.forEach {
            var rect = Rect()
            markPaint.getTextBounds(it,0,it.length,rect)
            maxVerticalMarkWidth = maxVerticalMarkWidth.coerceAtLeast(rect.width())
        }
        //横轴文字的最大宽度
        var maxHorizontalMarRectkWidth = 0
        horizontalMark.forEach {
            var rect = Rect()
            markPaint.getTextBounds(it,0,it.length,rect)
            maxHorizontalMarRectkWidth = maxHorizontalMarRectkWidth.coerceAtLeast(rect.width())
        }
        //横坐标文字相对于Y轴所形成的夹角的对边的长度
        var horizontalMarkWidth = sin(Math.toRadians(45.0)) * maxHorizontalMarRectkWidth
        //横坐标文字相对于Y轴所形成的夹角的临边的长度
        var horizontalMarkHeight = cos(Math.toRadians(45.0)) * maxHorizontalMarRectkWidth
        //横轴间隔
        var spaceX = (measuredWidth-3*MARGIN-maxVerticalMarkWidth)/horizontalMark.size
        var startHorizontalX = 2*MARGIN+maxVerticalMarkWidth+spaceX/2

        //纵轴间隔
        var spaceY = (measuredHeight-2*MARGIN-horizontalMarkHeight.toFloat())/verticalMark.size
        contentRect.left = (startHorizontalX-horizontalMarkWidth.toFloat()).toInt()
        contentRect.top = 0
        //画Y轴的标记和横线
        for (i in verticalMark.indices){
            var rect = Rect()
            markPaint.getTextBounds(verticalMark[i],0,verticalMark[i].length,rect)
            canvas.drawText(verticalMark[i],0,verticalMark[i].length,MARGIN,startVerticalY+rect.height(),markPaint)
            if(i != verticalMark.size-1){
                if(i == 0){
                    startPointY = startVerticalY+rect.height()/2
                }
                var path = Path()
                path.moveTo(startHorizontalX-horizontalMarkWidth.toFloat(),startVerticalY+rect.height()/2)

                path.lineTo(measuredWidth-MARGIN,startVerticalY+rect.height()/2)
                contentRect.right = (measuredWidth-MARGIN).toInt()
                canvas.drawPath(path,horizontalLinePaint)
                startVerticalY+=spaceY
            }else{
                horizontalLinePaint.pathEffect = null
                horizontalLinePaint.color = Color.parseColor("#333333")
                var path = Path()
                path.moveTo(startHorizontalX-horizontalMarkWidth.toFloat(),startVerticalY+rect.height()/2)
                path.lineTo(measuredWidth-MARGIN,startVerticalY+rect.height()/2)
                canvas.drawPath(path,horizontalLinePaint)
                endPointY = startVerticalY+rect.height()/2
            }
        }
        contentRect.bottom = (startVerticalY+MARGIN+horizontalMarkHeight).toInt()
        //画平均分
        horizontalLinePaint.color = Color.parseColor("#ff7400")
        horizontalLinePaint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        markPaint.color = Color.parseColor("#ff7400")
        var rect = Rect()
        markPaint.getTextBounds(averageScorePercent.toString(),0,averageScorePercent.toString().length,rect)
        canvas.drawText(averageScorePercent.toString(),0,averageScorePercent.toString().length,startHorizontalX-rect.width()/2,endPointY-((endPointY-startPointY)*averageScorePercent/100)+rect.height()/2,markPaint)

        var path = Path()
        path.moveTo(startHorizontalX+markPaint.measureText(averageScorePercent.toString()),endPointY-((endPointY-startPointY)*averageScorePercent/100))
        path.lineTo(measuredWidth-MARGIN,endPointY-((endPointY-startPointY)*averageScorePercent/100))
        canvas.drawPath(path,horizontalLinePaint)
        horizontalLinePaint.color = Color.parseColor("#d6d6d6")
        //可以从画横线的步骤开始处理canvas  但是就这样吧  我懒得搞，视觉上效果是一样的
        canvas.save()
        canvas.translate(-scrollOffset,0f)
        contentRect.left +=scrollOffset.toInt()
        contentRect.right +=scrollOffset.toInt()
        canvas.clipRect(contentRect)
        //画X轴的标记 和 点
        markPaint.color = Color.parseColor("#333333")
        var linePath = Path()
        for (i in horizontalMark.indices){
            var path = Path()
            path.moveTo(startHorizontalX-horizontalMarkWidth.toFloat()/2,startVerticalY+MARGIN+horizontalMarkHeight.toFloat())
            path.lineTo(startHorizontalX+horizontalMarkWidth.toFloat()/2,startVerticalY+MARGIN)
            canvas.drawTextOnPath(horizontalMark[i],path,0f,0f,markPaint)

            canvas.drawCircle(startHorizontalX,endPointY-((endPointY-startPointY)*percentList[i]/100),3f.dp2px,linePaint)

            if(i==0){
                linePath.moveTo(startHorizontalX,endPointY-((endPointY-startPointY)*percentList[i]/100))
            }else {
                linePath.lineTo(startHorizontalX,endPointY-((endPointY-startPointY)*percentList[i]/100))
            }

            startHorizontalX+= spaceX
        }
        canvas.drawPath(linePath,linePaint)

        canvas.restore()

    }

    var downX:Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN->{
                downX = event.x
            }
            MotionEvent.ACTION_MOVE->{
                scrollOffset = lastMoveOffset+downX-event.x
                if(scrollOffset<0f){
                    scrollOffset = 0f
                }
                if(scrollOffset > maxScrollOffset){
                    scrollOffset = maxScrollOffset
                }
                Log.e("tagggg", "onTouchEvent:   downX = ${downX}  event.x = ${event.x}  moveOffset = ${scrollOffset}   lastMoveOffset = ${lastMoveOffset}" )
                invalidate()
            }
            MotionEvent.ACTION_UP->{
                lastMoveOffset = scrollOffset
            }
        }

        return true
    }

}