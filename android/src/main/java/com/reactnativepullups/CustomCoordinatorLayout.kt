package com.reactnativepullups

import android.util.Log
import android.util.AttributeSet
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.RelativeLayout
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CustomCoordinatorLayout : RelativeLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
  
  /* Sets the height of the layout to that of its first child's */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var child = getChildAt(0)
    Log.d("PULLUPS", "CCL ${child.height} ${child.measuredHeight} ${MeasureSpec.getSize(heightMeasureSpec)}")
    super.onMeasure(
      widthMeasureSpec,
      if(child == null)
        heightMeasureSpec
      else 
        MeasureSpec.makeMeasureSpec(child.measuredHeight, MeasureSpec.EXACTLY)
    )
  }

  override fun onLayout(a: Boolean, b: Int, c: Int, d: Int, e: Int){
    Log.d("PULLUPS", "CCL onLayout $a $b $c $d $e")
    super.onLayout(a,b,c,d,e)
  }

  override fun requestLayout(){
    var child = getChildAt(0)
    Log.d("PULLUPS", "CCL requestLayout " + child?.height + "," + child?.measuredHeight)
    super.requestLayout()
  }

}