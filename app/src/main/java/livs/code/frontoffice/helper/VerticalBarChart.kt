package livs.code.frontoffice.helper

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class VerticalBarChart(chart: BarDataProvider, animator: ChartAnimator, viewPortHandler: ViewPortHandler) : HorizontalBarChartRenderer(chart, animator, viewPortHandler) {
    override fun drawValue(c: Canvas, valueText: String, x: Float, y: Float, color: Int) {
        mValuePaint.color = color
        val xPoint = Utils.convertDpToPixel(26f)
        val yPoint = Utils.convertDpToPixel(16f)
        c.drawText(valueText, xPoint , yPoint , mValuePaint)
    }
}
