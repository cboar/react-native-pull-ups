package com.reactnativepullups

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout 
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior


class PullUpsViewManager : ViewGroupManager<LinearLayout>() {

  override fun getName() = "RNPullUpView"
  private lateinit var dialog: BottomSheetDialog
  private lateinit var contents: CustomCoordinatorLayout
  private lateinit var ctx: ThemedReactContext
  
  private var active: Boolean = false

  override fun createViewInstance(reactContext: ThemedReactContext): LinearLayout {
    ctx = reactContext
    contents = CustomCoordinatorLayout(reactContext)

    dialog = object : BottomSheetDialog(reactContext) {
      override fun onCreate(bundle: Bundle?){
        super.onCreate(bundle)
      }
    }
    return LinearLayout(reactContext)
  }

  override fun addView(parent: LinearLayout?, child: View, index: Int) {
    contents.addView(child)
    contents.requestLayout()
    //contents = child as ReactViewGroup
  }

  @ReactProp(name = "active")
  fun setActive(parent: LinearLayout, nowActive: Boolean){
    if(active == nowActive)
      return
    if(active){
      dialog.dismiss();
    } else {

      dialog.setContentView(contents)
      dialog.show()
    }
    active = nowActive
  }
}
