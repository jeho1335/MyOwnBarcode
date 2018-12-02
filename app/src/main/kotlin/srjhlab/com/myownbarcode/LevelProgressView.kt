package srjhlab.com.myownbarcode

import android.content.Context
import android.widget.SeekBar

class LevelProgressView(context : Context) : SeekBar(context) {
    val TAG = this.javaClass.simpleName

    override fun getAccessibilityClassName(): CharSequence {
        return super.getAccessibilityClassName()
    }

    override fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener?) {
        super.setOnSeekBarChangeListener(l)
    }
}