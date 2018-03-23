package com.infinity.weather.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.infinity.weather.R;

/**
 * Created by m.mazurkevich on 12.08.15.
 */
public class ExtendTextView extends TextView{

    private Context context;

    public ExtendTextView(Context context) {
        super(context);
        init(null);
    }

    public ExtendTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public ExtendTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if (attrs != null) {
            String fontName = context.obtainStyledAttributes(attrs, R.styleable.ExtendTextView)
                    .getString(R.styleable.ExtendTextView_custom_font);
            Log.e("",fontName);
            if (!fontName.isEmpty()) {
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf"));
            }
        }
    }

}
