package com.example.custom;

/**
 * @author Lix
 * @date 2018/10/15 15:42
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Lix
 * @date 2018/10/15 20:32
 * 发布功能切换按钮
 */
public class CustomView extends LinearLayout {
    private static final String TAG = "CustomView";
    private int currentItemCopy;
    private boolean scrollToRight = false;
    /**
     * 动画属性设置
     */
    private int AnimationDurationTime = 300;
    private int AnimationRunningCount = 0;
    private boolean mAnimationRunning = false;
    private boolean mRequestLayout = false;
    /**
     * 当前选中item
     */
    private int mCurrentItem = 0;
    /**
     * 点击事件
     */
    private onClickText mOnClickText;
    /**
     * 缩放比例
     */
    private float mItemScale;
    /**
     * 文字大小
     */
    private float mTextSize;
    /**
     * 前景色
     */
    private int mForeGround;
    /**
     * 背景色
     */
    private int mBackGround;


    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        mBackGround = typedArray.getInteger(R.styleable.CustomView_back_ground, ContextCompat.getColor(context, android.R.color.holo_green_light));
        mForeGround = typedArray.getInteger(R.styleable.CustomView_fore_ground, ContextCompat.getColor(context, android.R.color.white));
        mItemScale = typedArray.getFloat(R.styleable.CustomView_scale, 0.1f);
        mTextSize = typedArray.getDimension(R.styleable.CustomView_text_size, 15);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout ");
        super.onLayout(changed, l, t, r, b);
        View v = getChildAt(mCurrentItem);
        int delta = getWidth() / 2 - v.getLeft() - v.getWidth() / 2;

        for (int i = 0; i < getChildCount(); i++) {
            View v1 = getChildAt(i);
            if (i == mCurrentItem) {
                v1.layout(v1.getLeft() + delta, v1.getTop(),
                        v1.getRight() + delta, v1.getBottom());
                continue;
            }
            float mScale = Math.abs(i - mCurrentItem) * mItemScale;
            int move = (int) (v1.getWidth() * mScale / 2);
            if (i < mCurrentItem) {
                for (int j = i + 1; j < mCurrentItem; j++) {
                    View v2 = getChildAt(j);
                    move += (int) (v2.getWidth() * Math.abs(j - mCurrentItem) * mItemScale);
                }
            } else {
                for (int j = i - 1; j > mCurrentItem; j--) {
                    View v2 = getChildAt(j);
                    move += (int) (v2.getWidth() * Math.abs(j - mCurrentItem) * mItemScale);
                }
                move = -move;
            }
            v1.layout(v1.getLeft() + delta + move, v1.getTop(),
                    v1.getRight() + delta + move, v1.getBottom());
        }
        mRequestLayout = false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            updateChildItem(canvas, i);
        }
    }

    /**
     * 设置缩放
     *
     * @param canvas 画笔
     * @param item   text
     */
    public void updateChildItem(Canvas canvas, int item) {
        View v = getChildAt(item);
        float desi = 1 - Math.abs(item - mCurrentItem) * mItemScale;
        v.setScaleX(desi);
        v.setScaleY(desi);
        drawChild(canvas, v, getDrawingTime());
        updateTextColor();
    }

    /**
     * 更新颜色
     */
    private void updateTextColor() {
        for (int i = 0; i < getChildCount(); i++) {
            if (i == mCurrentItem) {
                ((TextView) getChildAt(i)).setTextColor(mForeGround);
            } else {
                ((TextView) getChildAt(i)).setTextColor(mBackGround);
            }
        }
    }

    /**
     * 移动到指定position
     *
     * @param position 想要移动到的position
     * @param last     上次选中的position
     */
    public void scrollTo(int position, int last) {
        mCurrentItem = position;
        currentItemCopy = position;
        if (mRequestLayout) return;
        if (mAnimationRunning) {
            if (AnimationRunningCount < 1) {
                if (position > last) {
                    scrollToRight = false;
                } else {
                    scrollToRight = true;
                }
                AnimationRunningCount++;
            }
            return;
        }
        startTraAnimation(position, last);
        updateTextColor();
    }

    /**
     * 向右
     */
    public void scrollRight() {
        if (mRequestLayout) return;
        if (mCurrentItem > 0) {
            if (mAnimationRunning) {
                if (AnimationRunningCount < 1) {
                    currentItemCopy = mCurrentItem - 1;
                    AnimationRunningCount++;
                    scrollToRight = true;
                }
                return;
            }
            mCurrentItem--;
            startTraAnimation(mCurrentItem, mCurrentItem + 1);
            updateTextColor();
        }
    }


    /**
     * 向左
     */
    public void scrollLeft() {
        if (mRequestLayout) return;
        if (mCurrentItem < getChildCount() - 1) {
            if (mAnimationRunning) {
                if (AnimationRunningCount < 1) {
                    currentItemCopy = mCurrentItem + 1;
                    AnimationRunningCount++;
                    scrollToRight = false;
                }
                return;
            }
            mCurrentItem++;
            startTraAnimation(mCurrentItem, mCurrentItem - 1);
            updateTextColor();
        }
    }

    /**
     * 添加数据源
     *
     * @param name 数据
     */
    public void addIndicator(String[] name) {
        for (int i = 0; i < name.length; i++) {
            TextView mTextView = new TextView(getContext());
            mTextView.setText(name[i]);
            mTextView.setTextSize(mTextSize);
            mTextView.setTextColor(mBackGround);
            mTextView.setLines(1);
            LayoutParams ll = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            ll.setMargins(20, 0, 20, 0);
            final int finalI = i;
            mTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickText.onClick(finalI, mCurrentItem);
                }
            });
            addView(mTextView, ll);
        }
    }

    /**
     * 动画
     */
    public class mAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG, "onAnimationStart ");
            mAnimationRunning = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            Log.d(TAG, "onAnimationEnd ");

            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).clearAnimation();
            }
            mRequestLayout = true;
            requestLayout();
            mAnimationRunning = false;
            if (AnimationRunningCount > 0) {
                CustomView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        AnimationRunningCount--;
                        mCurrentItem = currentItemCopy;
                        int lastItem = scrollToRight ? currentItemCopy + 1 : currentItemCopy - 1;
                        startTraAnimation(currentItemCopy, lastItem);
                        updateTextColor();
                    }
                });
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

    public void startTraAnimation(int item, int last) {
        Log.d(TAG, "startTraAnimation item = " + item);
        Log.d(TAG, "startTraAnimation last = " + last);
        View v = getChildAt(item);
        final int width = v.getWidth();
        final int childCount = getChildCount();
        int traslate = getWidth() / 2 - v.getLeft() - width / 2;

        int currentItemWidthScale = (int) (width * mItemScale);

        for (int i = 0; i < childCount; i++) {
            int delta = currentItemWidthScale / 2;
            Log.d(TAG, " i = " + i + " delta before = " + delta);
            if (i < item) {
                delta = -delta;
                for (int j = i; j < item; j++) {
                    int a;
                    if (i == j) {
                        a = (int) (getChildAt(j).getWidth() * mItemScale / 2);
                    } else {
                        a = (int) (getChildAt(j).getWidth() * mItemScale);
                    }
                    delta = item < last ? delta - a : delta + a;
                }
            } else if (i > item) {
                for (int j = item + 1; j <= i; j++) {
                    int a;
                    if (j == i) {
                        a = (int) (getChildAt(j).getWidth() * mItemScale / 2);
                    } else {
                        a = (int) (getChildAt(j).getWidth() * mItemScale);
                    }
                    delta = item < last ? delta - a : delta + a;
                }
            } else {
                delta = 0;
            }
            Log.d(TAG, "delta = " + delta);
            delta += traslate;
            TranslateAnimation translateAni = new TranslateAnimation(0, delta, 0, 0);
            translateAni.setDuration(AnimationDurationTime);
            translateAni.setFillAfter(true);
            if (i == item) translateAni.setAnimationListener(new mAnimationListener());
            mAnimationRunning = true;
            getChildAt(i).startAnimation(translateAni);
        }
    }

    public interface onClickText {
        void onClick(int i, int last);
    }

    public float getItemScale() {
        return mItemScale;
    }

    public void setItemScale(float itemScale) {
        mItemScale = itemScale;
    }

    public int getForeGround() {
        return mForeGround;
    }

    public void setForeGround(int foreGround) {
        mForeGround = foreGround;
    }

    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int backGround) {
        mBackGround = backGround;
    }

    public onClickText getOnClickText() {
        return mOnClickText;
    }

    public void setOnClickText(onClickText onClickText) {
        mOnClickText = onClickText;
    }


    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(int currentItem) {
        mCurrentItem = currentItem;
    }
}
