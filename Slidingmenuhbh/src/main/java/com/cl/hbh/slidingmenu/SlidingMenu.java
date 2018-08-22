package com.cl.hbh.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Houbenhui on 2018/3/23.
 * 自定义抽屉View
 */
public class SlidingMenu extends FrameLayout {

    private View menuView, mainView;
    private int menuWidth, mainWidth;
    private Status status = Status.Close;
    private ViewDragHelper mDragHelper;
    /**
     * slidingStyle
     * 1 左侧
     * 2 左内侧
     * 3 右侧
     * 4 右内侧
     */
    private String slidingStyle = "1";//侧滑样式，默认为左侧

    /**
     * 开闭状态
     */
    public enum Status {
        Open, Close
    }

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        slidingStyle = typedArray.getString(R.styleable.SlidingMenu_slidingStyle);
        mDragHelper = ViewDragHelper.create(this, callback);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View view, int arg1) {
            return mainView == view;
        }

        //边缘被点击
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Log.d("debug", "onEdgeTouched: "+edgeFlags);
        }
        @Override
        public int getViewHorizontalDragRange(View child) {
            return menuWidth;
        }

        //处理水平滑动
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            switch (slidingStyle){
                case "1":
                case "2":
                    if (child == mainView) {
                        if (left < 0)
                            return 0;
                        else if (left > menuWidth)
                            return menuWidth;
                        else
                            return left;
                    } else if (child == menuView) {
                        if (left > 0)
                            return 0;
                        else if (left > menuWidth)
                            return menuWidth;
                        else
                            return left;
                    }

                case "3":
                case "4":
                    if (child == mainView) {
                        if (left > 0)
                            return 0;
                        else if (left < -menuWidth)
                            return -menuWidth;

                    } else if (child == menuView) {
                        if (left > mainWidth)
                            return mainWidth;
                        else if (left < mainWidth - menuWidth)
                            return mainWidth - menuWidth;
                    }
                    return left;
                default:
                    return 0;
            }
        }
        // 当位置改变的时候调用,常用与滑动时更改scale等
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (slidingStyle.equals("1") || slidingStyle.equals("3")) {
                if (changedView == mainView)
                    menuView.offsetLeftAndRight(dx);
                else
                    mainView.offsetLeftAndRight(dx);
                invalidate();
            }
        }

        //拖拽结束后回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //判断菜单打开关闭逻辑
            switch (slidingStyle){
                case "1":
                    //左滑
                    if (xvel == 0
                            && Math.abs(mainView.getLeft()) > menuWidth * 0.5f) {
                        open();
                    } else if (xvel > 0) {
                        open();
                    } else {
                        close();
                    }
                    break;
                case "2":
                    //左内滑
                    if (xvel == 0
                            && Math.abs(mainView.getLeft()) > menuWidth * 0.5f) {
                        open();
                    } else if (xvel > 0) {
                        open();
                    } else {
                        close();
                    }
                    break;
                case "3":
                    //右滑
                    if (xvel == 0 && Math.abs(mainView.getLeft()) < -menuWidth * 0.5f) {
                        open();
                    } else if (xvel < 0) {
                        open();
                    } else {
                        close();
                    }
                    break;
                case "4":
                    //右内滑
                    if (xvel == 0 && Math.abs(mainView.getLeft()) < menuWidth * 0.5f) {
                        open();
                    } else if (xvel < 0) {
                        open();
                    } else {
                        close();
                    }
                    break;
            }
        }
    };

    /**
     * 打开菜单
     */
    public void open() {
        switch (slidingStyle){
            case "1":
            case "2":
                if (mDragHelper.smoothSlideViewTo(mainView, menuWidth, 0)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
            case "3":
            case "4":
                if (mDragHelper.smoothSlideViewTo(mainView, -menuWidth, 0)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
        }
        status = Status.Open;
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mDragHelper.smoothSlideViewTo(mainView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        status = Status.Close;
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (status == Status.Close) {
            open();
        } else {
            close();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()!=2){
            throw new IllegalArgumentException("The subview must have two");
        }
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        //菜单界面View的宽度
        menuWidth = menuView.getLayoutParams().width;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //主界面View的宽度
        mainWidth = mainView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        switch (slidingStyle){
            case "1":
                //左侧菜单
                menuView.layout(-menuWidth, 0, 0, menuView.getMeasuredHeight());
                break;
            case "2":
                //左内侧菜单
                menuView.layout(0, 0, menuWidth, menuView.getMeasuredHeight());
                break;
            case "3":
                //右侧菜单
                menuView.layout(mainWidth, 0, mainWidth + menuWidth, menuView.getMeasuredHeight());
                break;
            case "4":
                //右内侧菜单
                menuView.layout(mainWidth - menuWidth, 0, mainWidth, menuView.getMeasuredHeight());
                break;
        }
        mainView.layout(0, 0, right, bottom);
    }

    /**
     * 重写拦截事件，将事件传递给ViewDragHelper处理
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

}
