package com.lingyu.customViewDemo.customView;

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.lingyu.customViewDemo.R

/**
 * Created by lingyu on  2021/4/9
 *
 * description:
 */
class ImageTextView @JvmOverloads constructor(val mContext: Context, val attrs: AttributeSet?=null, val defStyleAttr:Int = 0): View(mContext,attrs, defStyleAttr) {
    private  val TAG = "ImageTextView"

    var contentText = "讲述了天才少年萧炎在创造了家族空前绝后的修炼纪录后突然成了废人，种种打击接踵而至。就在他即将绝望的时候，一缕灵魂从他手上的戒指里浮现，一扇全新的大门在面前开启，经过艰苦修炼最终成就辉煌的故事。\n" +
            "斗破苍穹全套书籍" +
            "斗破苍穹全套书籍(2张)" +
            "这里是属于斗气的世界，没有花俏艳丽的魔法，有的，仅仅是繁衍到巅峰的斗气" +
            "萧炎，主人公，萧家历史上空前绝后的斗气修炼天才。4岁就开始修炼斗之气，10岁拥有了九段斗之气，11岁突破十段斗之气，一跃成为家族百年来最年轻的斗者。然而在12岁那年，他却“丧失”了修炼能力，只拥有三段斗之气。整整三年时间，家族冷落，旁人轻视，被未婚妻退婚……种种打击接踵而至。\n" +
            "就在他即将绝望的时候，一缕灵魂从他手上的戒指里浮现，一扇全新的大门在面前开启！萧炎重新成为家族年轻一辈中的佼佼者，受到众人的仰慕，他却不满足于此。为了一雪退婚带来的耻辱，萧炎来到了魔兽山脉，在药老的帮助下，为了进一步提升自己的修为，在魔兽山脉，他结识了小医仙，云芝（云岚宗宗主云韵）等人，他发现自己面向的世界更加宽广了。\n" +
            "三十年河东，三十年河西，莫欺少年穷！ 年仅15岁的萧家废物，于此地，立下了誓言，从今以后便一步步走向斗气大陆巅峰！" +
            "经历了一系列的磨练，收异火，寻宝物，炼丹药，斗魂族。" +
            "最终成为斗帝，为解开斗帝失踪之谜而前往大千世界...."

    private var paint: Paint? = null

    private var textPaint:TextPaint? = null

    private var breakTextWidth:FloatArray = FloatArray(1)

    private var fontMetrics:Paint.FontMetrics? = null

    private val Float.dp2px
        get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this,Resources.getSystem().displayMetrics)

    private var staticLayout: StaticLayout? = null

    private var point:Point = Point(0,0)

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.textSize = 50f
        fontMetrics = paint?.fontMetrics

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //画图片
        var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)

        canvas.drawBitmap(bitmap,point.x.toFloat(),point.y.toFloat(),null)

        var startY = -fontMetrics!!.top

        var count = if(startY <= bitmap.height) getSingleLineTextCount(bitmap.width) else getSingleLineTextCount(0)

        while (count>0){

            Log.e(TAG, "count: =============$count" )

            canvas.drawText(contentText,0,count,if(startY <= point.y+bitmap.height) point.x+bitmap.width.toFloat() else 0f,startY,paint!!)

            startY+= paint!!.fontSpacing

            contentText = contentText.substring(count)

            count = if(startY <= point.y+bitmap.height) getSingleLineTextCount(bitmap.width) else getSingleLineTextCount(0)

        }

    }

    private fun getSingleLineTextCount(bitmapWidth:Int):Int{

        return paint!!.breakText(contentText,true,width-bitmapWidth.toFloat(),breakTextWidth)
    }
}