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

class ContentContainerView : RelativeLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    Log.d("PULLUPS", "onMeasure ${MeasureSpec.getSize(widthMeasureSpec)} ${MeasureSpec.getSize(heightMeasureSpec)} ${getWidth()} ${getHeight()}")
    setMeasuredDimension(
      MeasureSpec.getSize(widthMeasureSpec),
      MeasureSpec.getSize(heightMeasureSpec)
    )
    if(height != measuredHeight){
      invalidate()
      (parent as View).invalidate()
    }
  }

  override fun onLayout(a: Boolean, b: Int, c: Int, d: Int, e: Int){
    Log.d("PULLUPS", "onLayout $a $b $c $d $e")
  }

  override fun requestLayout(){
    Log.d("PULLUPS", "requestLayout")
    super.requestLayout()
  }

}