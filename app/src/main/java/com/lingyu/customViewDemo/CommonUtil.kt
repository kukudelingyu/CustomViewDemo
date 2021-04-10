package com.lingyu.customViewDemo

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by lingyu on  2021/4/10
 *
 * description:
 */
object CommonUtil {

    fun dp2px(value:Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,Resources.getSystem().displayMetrics)
    }
}

