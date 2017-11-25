package club.wello.mtracker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * view for show time on the left  of traces
 * Created by maweihao on 25/11/2017.
 */

public class TraceTimeView extends View {

    private static final String TAG = TraceTimeView.class.getSimpleName();

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LATEST_DELIVERED = 1;
    public static final int TYPE_LATEST_DELIVERING = 2;
    public static final int TYPE_OLDEST = 3;

    public TraceTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TraceTimeView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int length = getWidth() / 2;

        super.onDraw(canvas);
    }
}
