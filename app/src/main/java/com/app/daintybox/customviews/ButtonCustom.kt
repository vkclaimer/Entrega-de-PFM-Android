package com.app.daintybox.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.app.daintybox.R

class ButtonCustom: AppCompatButton {

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context,attrs,defStyle){
        init_font(attrs,context)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context,attrs){
        init_font(attrs,context)
    }

    constructor(context: Context): super(context){
        init_font(null,context)
    }

    fun init_font(attrs: AttributeSet?, context: Context?) {
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonCustom)
            val fontName = a.getString(R.styleable.ButtonCustom_fontName)
            if (fontName != null) {
                when (fontName) {
                    "semibold" -> {
                        val semiboldTypeface = Typeface.createFromAsset(
                            getContext().assets,
                            "fonts/Roboto-Medium.ttf"
                        )
                        setTypeface(semiboldTypeface)
                    }
                    "bold" -> {
                        val boldTypeface = Typeface.createFromAsset(
                            getContext().assets,
                            "fonts/Roboto-Bold.ttf"
                        )
                        setTypeface(boldTypeface)
                    }
                    "regular" -> {
                        val regularTypeface = Typeface.createFromAsset(
                            getContext().assets,
                            "fonts/Roboto-Regular.ttf"
                        )
                        setTypeface(regularTypeface)
                    }
                    else -> {
                        val regularTypeface = Typeface.createFromAsset(
                            getContext().assets,
                            "fonts/Roboto-Regular.ttf"
                        )
                        setTypeface(regularTypeface)
                    }
                }
            } else {
                val regularTypeface =
                    Typeface.createFromAsset(getContext().assets, "fonts/Roboto-Regular.ttf")
                setTypeface(regularTypeface)
            }
            a.recycle()
        } else {
            val regularTypeface =
                Typeface.createFromAsset(getContext().assets, "fonts/Roboto-Regular.ttf")
            setTypeface(regularTypeface)
        }
    }

}