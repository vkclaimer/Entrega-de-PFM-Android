package com.app.daintybox.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.app.daintybox.R

class TextViewCustom : AppCompatTextView {

    constructor(context: Context,attrs: AttributeSet?,defStyle: Int): super(context,attrs,defStyle){
        init_font(attrs)
    }

    constructor(context: Context,attrs: AttributeSet?): super(context,attrs){
        init_font(attrs)
    }

    constructor(context: Context): super(context){
        init_font(null)
    }



    fun setFontName(fontName: String?) {
        typeface = if (fontName != null) {
            when (fontName) {
                "italic" -> {
                    val italicTypeface =
                        Typeface.createFromAsset(context.assets, "fonts/Roboto-Italic.ttf")
                    italicTypeface
                }
                "semibold" -> {
                    val semiboldTypeface =
                        Typeface.createFromAsset(context.assets, "fonts/Roboto-Medium.ttf")
                    semiboldTypeface
                }
                "bold" -> {
                    val boldTypeface =
                        Typeface.createFromAsset(context.assets, "fonts/Roboto-Bold.ttf")
                    boldTypeface
                }
                "regular" -> {
                    val regularTypeface =
                        Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
                    regularTypeface
                }
                else -> {
                    val regularTypeface =
                        Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
                    regularTypeface
                }
            }
        } else {
            val regularTypeface =
                Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
            regularTypeface
        }
    }

    fun init_font(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.TextViewCustom)
            val fontName = a.getString(R.styleable.TextViewCustom_fontName)
            if (fontName != null) {
                when (fontName) {
                    "italic" -> {
                        val italicTypeface =
                            Typeface.createFromAsset(context.assets, "fonts/Roboto-Italic.ttf")
                        setTypeface(italicTypeface)
                    }
                    "semibold" -> {
                        val semiboldTypeface = Typeface.createFromAsset(
                            context.assets,
                            "fonts/Roboto-Medium.ttf"
                        )
                        setTypeface(semiboldTypeface)
                    }
                    "bold" -> {
                        val boldTypeface =
                            Typeface.createFromAsset(context.assets, "fonts/Roboto-Bold.ttf")
                        setTypeface(boldTypeface)
                    }
                    "regular" -> {
                        val regularTypeface =
                            Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
                        setTypeface(regularTypeface)
                    }
                    else -> {
                        val regularTypeface =
                            Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
                        setTypeface(regularTypeface)
                    }
                }
            } else {
                val regularTypeface =
                    Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
                setTypeface(regularTypeface)
            }
            a.recycle()
        } else {
            val regularTypeface =
                Typeface.createFromAsset(context.assets, "fonts/Montserrat-Regular.ttf")
            setTypeface(regularTypeface)
        }
    }

}