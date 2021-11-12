package com.reactnativepullups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.os.Bundle
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.LabelFormatter

private const val STATE_CHANGE_EVENT_NAME = "BottomSheetStateChange"

class ContentContainerManager : ViewGroupManager<ContentContainerView>() {

  override fun getName() = "RNPullUpContentView"

  override fun createViewInstance(reactContext: ThemedReactContext): ContentContainerView {
    var view = ContentContainerView(reactContext)
    return view
  }

}