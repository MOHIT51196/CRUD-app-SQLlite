package intern.mohitmalhotra.internassignment.widgets;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * This is a customize paging scroller
 * that can change the paging timing for the view pager
 */

public class ViewPagerScroller extends Scroller {

private static final int SCROLL_DURATION = 600;

public ViewPagerScroller(Context context) {
    super(context);
}
public ViewPagerScroller(Context context, Interpolator interpolator) {
    super(context, interpolator);
}
@Override
public void startScroll(int startX, int startY, int dx, int dy, int duration) {
    super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
}
@Override
public void startScroll(int startX, int startY, int dx, int dy) {
    super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
}


}