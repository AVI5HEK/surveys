package com.avi5hek.surveys.core.widget

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avi5hek.surveys.core.extension.cast
import timber.log.Timber
import kotlin.math.max

/**
 * Created by "Avishek" on 8/4/2020.
 */
class CircleListIndicator : RecyclerView.ItemDecoration() {

  private val interpolator: Interpolator by lazy { AccelerateDecelerateInterpolator() }

  private val paintInactive = Paint()
  private val paintActive = Paint()

  init {
    paintInactive.style = Paint.Style.STROKE
    paintInactive.strokeCap = Paint.Cap.ROUND
    paintInactive.strokeWidth = INDICATOR_STROKE_WIDTH
    paintInactive.color = INDICATOR_COLOR
    paintInactive.isAntiAlias = true

    paintActive.style = Paint.Style.FILL
    paintActive.color = INDICATOR_COLOR
    paintActive.isAntiAlias = true
  }

  override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
    super.onDrawOver(canvas, recyclerView, state)

    val itemCount = recyclerView.adapter?.itemCount ?: return

    val stripLength = INDICATOR_LENGTH * itemCount
    val paddingBetweenItems = max(0, itemCount - 1) * INDICATOR_PADDING
    val totalStripLength = stripLength + paddingBetweenItems

    val layoutManager = recyclerView.layoutManager.cast<LinearLayoutManager>()

    val indicatorX: Float
    val indicatorY: Float

    if (layoutManager?.orientation == RecyclerView.HORIZONTAL) {
      indicatorX = (recyclerView.width - totalStripLength) / 2f
      indicatorY =
        recyclerView.height - INDICATOR_LENGTH / 2f - (DP * 8)
      drawInactiveCircleIndicators(canvas, indicatorX, indicatorY, itemCount, true)
    } else {
      indicatorX =
        recyclerView.width - INDICATOR_LENGTH / 2f - (DP * 8)
      indicatorY = (recyclerView.height - totalStripLength) / 2f
      drawInactiveCircleIndicators(canvas, indicatorX, indicatorY, itemCount, false)
    }

    val activePosition = layoutManager?.findFirstVisibleItemPosition() ?: return
    if (activePosition == RecyclerView.NO_POSITION) {
      return
    }

    val activeChild = layoutManager.findViewByPosition(activePosition)

    val childBefore: Int
    val childLength: Int

    if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
      childBefore = activeChild?.left ?: return
      childLength = activeChild.width
    } else {
      childBefore = activeChild?.top ?: return
      childLength = activeChild.height
    }

    val progress = interpolator.getInterpolation(childBefore * -1 / childLength.toFloat())

    drawActiveCircleIndicator(
      canvas,
      indicatorX,
      indicatorY,
      activePosition,
      progress,
      layoutManager.orientation == RecyclerView.HORIZONTAL
    )
  }

  private fun drawInactiveCircleIndicators(
    canvas: Canvas,
    x: Float,
    y: Float,
    itemCount: Int,
    isHorizontal: Boolean
  ) {
    val itemLength = INDICATOR_LENGTH + INDICATOR_PADDING
    if (isHorizontal) {
      var currentX = x
      for (i in 0 until itemCount) {
        canvas.drawCircle(currentX, y, INDICATOR_LENGTH / 2f, paintInactive)
        currentX += itemLength
      }
    } else {
      var currentY = y
      for (i in 0 until itemCount) {
        canvas.drawCircle(x, currentY, INDICATOR_LENGTH / 2f, paintInactive)
        currentY += itemLength
      }
    }
  }

  private fun drawActiveCircleIndicator(
    canvas: Canvas,
    x: Float,
    y: Float,
    activePosition: Int,
    progress: Float,
    isHorizontal: Boolean
  ) {
    Timber.i(
      """
        highlight position: $activePosition
        progress: $progress
        """.trimIndent()
    )
    val itemLength = INDICATOR_LENGTH + INDICATOR_PADDING
    val radius = INDICATOR_LENGTH / 2f

    val activeX: Float
    val activeY: Float

    if (isHorizontal) {
      activeX = x + itemLength * activePosition
      activeY = y
    } else {
      activeX = x
      activeY = y + itemLength * activePosition
    }

    if (progress == 0f) {
      canvas.drawCircle(activeX, activeY, radius, paintActive)
    } else {
      val partialLength = itemLength * progress
      if (isHorizontal) {
        canvas.drawCircle(activeX + partialLength, y, radius, paintActive)
      } else {
        canvas.drawCircle(x, activeY + partialLength, radius, paintActive)
      }
    }
  }

  companion object {
    private const val INDICATOR_COLOR = -0x1
    private val DP = Resources.getSystem().displayMetrics.density
    private val INDICATOR_STROKE_WIDTH = DP * 2
    private val INDICATOR_LENGTH = DP * 12
    private val INDICATOR_PADDING = DP * 8
  }
}
