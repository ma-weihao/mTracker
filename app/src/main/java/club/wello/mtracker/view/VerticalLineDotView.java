package club.wello.mtracker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import club.wello.mtracker.R;

/**
 * vertical line-dot in PackageDetailActivity
 * Created by maweihao on 2017/10/9.
 */

public class VerticalLineDotView extends View {

    private int lineColor;
    private int dotColor;
    private int specialDotColor;
    private boolean displayUpLine;
    private boolean displayDownLine;
    private boolean isSpecialDot;

    private Paint linePaint;
    private Paint dotPaint;
    private Paint specialDotPaint;

    public VerticalLineDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalLineDotView);
        lineColor = typedArray.getColor(R.styleable.VerticalLineDotView_lineColor, Color.parseColor("#BDBDBD"));  //light gray
        dotColor = typedArray.getColor(R.styleable.VerticalLineDotView_dotColor, Color.parseColor("#424242"));  //dark gray
        displayUpLine = typedArray.getBoolean(R.styleable.VerticalLineDotView_displayUpLine, true);
        displayDownLine = typedArray.getBoolean(R.styleable.VerticalLineDotView_displayDownLine, true);
        isSpecialDot = typedArray.getBoolean(R.styleable.VerticalLineDotView_special, false);
        specialDotColor = typedArray.getColor(R.styleable.VerticalLineDotView_specialDotColor, Color.parseColor("#4CAF50")); //green
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(15);

        dotPaint = new Paint();
        dotPaint.setColor(dotColor);
        dotPaint.setStyle(Paint.Style.FILL);

        specialDotPaint = new Paint();
        specialDotPaint.setColor(specialDotColor);
        specialDotPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (displayUpLine) {
            canvas.drawLine(width / 2, 0, width / 2, height / 2, linePaint);
        }
        if (displayDownLine) {
            canvas.drawLine(width / 2, height / 2, width / 2, height, linePaint);
        }
        if (isSpecialDot) {
            canvas.drawCircle(width / 2, height / 2, 30, specialDotPaint);
        } else {
            canvas.drawCircle(width / 2, height / 2, 20, dotPaint);
        }
        super.onDraw(canvas);
    }

    public void setSpecial(boolean isSpecial) {
        if (isSpecialDot != isSpecial) {
            isSpecialDot = isSpecial;
            invalidate();
        }
    }

    public void setUpDown(boolean up, boolean down) {
        if (displayUpLine != up || displayDownLine != down) {
            displayUpLine = up;
            displayDownLine = down;
            invalidate();
        }
    }
}
