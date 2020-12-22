package com.seanyj.mysamples.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class SquareLayout extends ViewGroup {
    private static final int ORIENTATION_HORIZONTAL = 0, ORIENTATION_VERTICAL = 1;// 排列方向的常量标识值
    private static final int DEFAULT_MAX_ROW = Integer.MAX_VALUE, DEFAULT_MAX_COLUMN = Integer.MAX_VALUE;// 最大行列默认值

    private int mMaxRow = DEFAULT_MAX_ROW;// 最大行数
    private int mMaxColumn = DEFAULT_MAX_COLUMN;// 最大列数

    private int mOrientation = ORIENTATION_HORIZONTAL;// 排列方向默认横向

    public SquareLayout(Context context) {
        super(context);
    }

    // 省去构造方法…………

    @SuppressLint("NewApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
		 * 声明临时变量存储父容器的期望值
		 * 该值应该等于父容器的内边距加上所有子元素的测量宽高和外边距
		 */
        int parentDesireWidth = 0;
        int parentDesireHeight = 0;

        // 声明临时变量存储子元素的测量状态
        int childMeasureState = 0;

		/*
		 * 如果父容器内有子元素
		 */
        if (getChildCount() > 0) {
			/*
			 * 那么就遍历子元素
			 */
            for (int i = 0; i < getChildCount(); i++) {
                // 获取对应遍历下标的子元素
                View child = getChildAt(i);

				/*
				 * 如果该子元素没有以“不占用空间”的方式隐藏则表示其需要被测量计算
				 */
                if (child.getVisibility() != View.GONE) {

                    // 测量子元素并考量其外边距
                    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                    // 比较子元素测量宽高并比较取其较大值
                    int childMeasureSize = Math.max(child.getMeasuredWidth(), child.getMeasuredHeight());

                    // 重新封装子元素测量规格
                    int childMeasureSpec = MeasureSpec.makeMeasureSpec(childMeasureSize, MeasureSpec.EXACTLY);

                    // 重新测量子元素
                    child.measure(childMeasureSpec, childMeasureSpec);

                    // 获取子元素布局参数
                    MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();

					/*
					 * 考量外边距计算子元素实际宽高
					 */
                    int childActualWidth = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
                    int childActualHeight = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

					/*
					 * 如果为横向排列
					 */
                    if (mOrientation == ORIENTATION_HORIZONTAL) {
                        // 累加子元素的实际宽度
                        parentDesireWidth += childActualWidth;

                        // 获取子元素中高度最大值
                        parentDesireHeight = Math.max(parentDesireHeight, childActualHeight);
                    }

					/*
					 * 如果为竖向排列
					 */
                    else if (mOrientation == ORIENTATION_VERTICAL) {
                        // 累加子元素的实际高度
                        parentDesireHeight += childActualHeight;

                        // 获取子元素中宽度最大值
                        parentDesireWidth = Math.max(parentDesireWidth, childActualWidth);
                    }

                    // 合并子元素的测量状态
                    childMeasureState = combineMeasuredStates(childMeasureState, child.getMeasuredState());
                }
            }

			/*
			 * 考量父容器内边距将其累加到期望值
			 */
            parentDesireWidth += getPaddingLeft() + getPaddingRight();
            parentDesireHeight += getPaddingTop() + getPaddingBottom();

			/*
			 * 尝试比较父容器期望值与Android建议的最小值大小并取较大值
			 */
            parentDesireWidth = Math.max(parentDesireWidth, getSuggestedMinimumWidth());
            parentDesireHeight = Math.max(parentDesireHeight, getSuggestedMinimumHeight());
        }

        // 确定父容器的测量宽高
        setMeasuredDimension(resolveSizeAndState(parentDesireWidth, widthMeasureSpec, childMeasureState),
                resolveSizeAndState(parentDesireHeight, heightMeasureSpec, childMeasureState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    // 省去onLayout方法…………

    // 省去四个屌毛方法……
}
