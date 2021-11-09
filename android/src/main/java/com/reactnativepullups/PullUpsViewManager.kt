package com.reactnativepullups

import android.util.Log
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.FrameLayout
import android.os.Bundle
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior

private const val STATE_CHANGE_EVENT_NAME = "BottomSheetStateChange"

class PullUpsViewManager : ViewGroupManager<LinearLayout>() {

  override fun getName() = "RNPullUpView"
  enum class BottomSheetState {
    COLLAPSED,
    HIDDEN,
    EXPANDED;
    fun toLowerCase() = this.toString().toLowerCase()
  }

  private lateinit var dialog: BottomSheetDialog
  private lateinit var contents: CustomCoordinatorLayout
  private lateinit var behavior: BottomSheetBehavior<FrameLayout>
  private lateinit var context: ThemedReactContext

  private var active: Boolean = false
  private var state: BottomSheetState = BottomSheetState.HIDDEN

  override fun createViewInstance(reactContext: ThemedReactContext): LinearLayout {
    context = reactContext
    contents = CustomCoordinatorLayout(reactContext)

    dialog = object : BottomSheetDialog(reactContext) {
      override fun onCreate(bundle: Bundle?){
        super.onCreate(bundle)
      }
    }
    dialog.setOnCancelListener(object : DialogInterface.OnCancelListener {
      override fun onCancel(info: DialogInterface){
        updateState(BottomSheetState.HIDDEN)
      }
    })
    behavior = dialog.behavior
    behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
      override fun onStateChanged(bottomSheet: View, stateVal: Int) {
        updateState(stateVal)
      }
    })

    var dummy = LinearLayout(reactContext)
    dummy.layoutParams = LinearLayout.LayoutParams(0, 0)
    dummy.visibility = LinearLayout.GONE
    return dummy
  }

  override fun addView(parent: LinearLayout?, child: View, index: Int) {
    contents.addView(child)
    contents.requestLayout()
  }

  private fun matchState(sheetState: Int): BottomSheetState? {
    return when (sheetState) {
      BottomSheetBehavior.STATE_EXPANDED -> BottomSheetState.EXPANDED
      BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetState.COLLAPSED
      BottomSheetBehavior.STATE_HIDDEN -> BottomSheetState.HIDDEN
      else -> null
    }
  }

  private fun updateState(sheetState: Int){
    var newState = matchState(sheetState)
    updateState(newState)
  }

  private fun updateState(newState: BottomSheetState?){
    newState?.let {
      if(state == newState)
        return
      state = newState
      context
        .getJSModule(RCTDeviceEventEmitter::class.java)
        .emit(STATE_CHANGE_EVENT_NAME, state.toLowerCase())
    }
  }

  @ReactProp(name = "active")
  fun setActive(parent: LinearLayout, nowActive: Boolean){
    if(active == nowActive)
      return
    if(active){
      dialog.dismiss()
    } else {
      dialog.setContentView(contents)
      dialog.show()
      updateState(behavior.state)
    }
    active = nowActive
  }
}
