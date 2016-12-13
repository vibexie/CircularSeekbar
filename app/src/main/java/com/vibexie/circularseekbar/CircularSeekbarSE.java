package com.vibexie.circularseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CircularSeekbarSE extends View {
	private static final String TAG = "CircularSeekbarSE";

	private Context mContext;

	private Bitmap mPointEnd;// 起始点的图片

	private Bitmap mPointStart;// 点的图片

	private Paint backRing;// 背景圆环画笔

	private Paint frontRing;// 前面圆环画笔

	private Paint progressTextPaint; // 显示进度文字的画笔

	private RectF rect;// 渐变颜色环外接矩形

	private float cx;// 圆环中心位置x坐标

	private float cy;// 圆环中心位置y坐标

	private float pointXStart;// 当前点的x坐标

	private float pointYStart;// 当前点的y坐标

	private float pointXEnd;// 当前点的x坐标

	private float pointYEnd;// 当前点的y坐标

	private float ringRadius;// 圆环的半径

	private float ringWidth;// 圆环的宽度

	private float outerRadius;// 圆环外部，即可控拖拽区域外圆环

	private float innerRadius;// 圆环内部的半径，即可控拖拽区域内圆环

	private float angleStart = 0;// 起点弧度值

	private float angleEnd = 0;// 终点弧度值

	private float sweepAngle = 0;

	private float left, right, top, bottom;

	private boolean isDraging = false;

	private boolean isInited = false;

	// 设定默认颜色值
	private static int[] mColors={Color.GREEN, Color.BLUE, Color.RED};

	public CircularSeekbarSE(Context context) {
		super(context);
	}

	public CircularSeekbarSE(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircularSeekbarSE(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

		ringWidth = dp2Px(mContext, 4);
		// 初始化设置背景圆环
		backRing = new Paint();
		backRing.setColor(Color.WHITE);
		backRing.setAntiAlias(true);
		backRing.setStrokeWidth(ringWidth + dp2Px(mContext, 2));
		backRing.setStyle(Paint.Style.STROKE);
		// 初始化前面圆环
		frontRing = new Paint();
		frontRing.setColor(Color.parseColor("#ffffd2a9"));
		frontRing.setAntiAlias(true);
		frontRing.setStrokeWidth(ringWidth * 2 + dp2Px(mContext, 1));
		frontRing.setStyle(Paint.Style.STROKE);

		rect = new RectF();
		mPointEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch2);
		mPointStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch);
		mPointStart = ThumbnailUtils.extractThumbnail(mPointStart, dp2Px(mContext, 20), dp2Px(mContext, 20));
		mPointEnd = ThumbnailUtils.extractThumbnail(mPointEnd, dp2Px(mContext, 20), dp2Px(mContext, 20));

		progressTextPaint = new Paint();
		progressTextPaint.setAntiAlias(true);
		progressTextPaint.setColor(Color.WHITE);
		progressTextPaint.setTextSize(dp2Px(mContext, 69));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width;
		int height;
		width = getWidth();
		height = getHeight();

		int size = (width > height) ? height : width; // 选择最小的值作为圆环视图的直径

		cx = width / 2; // 得到圆环视图的中心x坐标
		cy = height / 2; // 得到圆环视图的中心y坐标

		ringRadius = size / 2 - dp2Px(mContext, 10) - ringWidth / 2;// 设置圆环的半径
		outerRadius = size / 2 - dp2Px(mContext, 10) + dp2Px(mContext, 50); // 得到圆环外部半径,这里减去10是为了防止点的图片被遮挡
		innerRadius = outerRadius - ringWidth - dp2Px(mContext, 100); // 设置圆环内部的半径

		left = cx - ringRadius; // 渐变圆环外接矩形左边坐标
		right = cx + ringRadius;// 渐变圆环外接矩形右边坐标
		top = cy - ringRadius;// 渐变圆环外接矩形上边坐标
		bottom = cy + ringRadius;// 渐变圆环外接矩形底部坐标

		pointXStart = cx;
		pointYStart = top;

        pointXEnd = cx;
        pointYEnd = top;

		rect.set(left, top, right, bottom); // 设置渐变圆环的位置
	}

	private float[] positions = new float[mColors.length];
	@Override
	protected void onDraw(Canvas canvas) {
		// 画背景圆环
		canvas.drawCircle(cx, cy, ringRadius, backRing);

		// 设定当前比例
		for (int i = 0; i < mColors.length; i++) {
			positions[i] = (((float) (i) / (mColors.length - 1)) * (sweepAngle / 360f));
		}

		// 新建渲染器
		SweepGradient shader = new SweepGradient(cx, cy, mColors, positions);
		// 新建矩阵,将渲染器旋转90度,从正上方开始
		Matrix matrix = new Matrix();
		Log.e(TAG, "angleStart = " + angleStart + "   angleEnd = " + angleEnd);
		matrix.setRotate(angleStart, cx, cy);
		shader.setLocalMatrix(matrix);
		// 设置渲染器
		frontRing.setShader(shader);

		// 画前面的圆环，每次刷新界面主要是改变这里的angle的值
		canvas.drawArc(rect, angleStart, sweepAngle, false, frontRing);

		// 画触摸点的图片
		canvas.drawBitmap(mPointStart, pointXStart - (mPointStart.getWidth()) / 2, pointYStart - (mPointStart.getWidth()) / 2, null);

		// 画触摸点的图片
		canvas.drawBitmap(mPointEnd, pointXEnd - (mPointEnd.getWidth()) / 2, pointYEnd - (mPointEnd.getWidth()) / 2, null);

		if (!isInited) {
			isInited = true;
		}
		super.onDraw(canvas);
	}

	private  boolean isDragStart = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// y向上变小，x向右变大
		// y最小8，最大870 cx 420 + innerRadius 400 是856
		// x最小10最大830
		float x = event.getX();
		float y = event.getY();
		this.getParent().requestDisallowInterceptTouchEvent(true);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			double tmp =  (x - cx) * (x - cx) + (y - cy) * (y - cy);
			if (tmp > (outerRadius) * (outerRadius) || tmp < (innerRadius) * (innerRadius)) {
				isDraging = false;
				return  true;
			}

			if (Math.abs(pointXEnd - x) < 60 && Math.abs(pointYEnd - y) < 60) {
				isDragStart = false;
				movedEnd(x, y);
				isDraging = true;
			} else if (Math.abs(pointXStart - x) < 60 && Math.abs(pointYStart - y) < 60) {
				isDragStart = true;
				movedStart(x, y);
				isDraging = true;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (!isDraging) {
				return true;
			}

			if (isDragStart) {
				movedStart(x, y);
			} else {
				movedEnd(x, y);
			}

			break;
		case MotionEvent.ACTION_UP:
			isDraging = false;
			break;
		}
		return true;
	}

	public void movedEnd(float x, float y) {
		// 如果触摸点在外圆半径的一个适配区域内

		// 将角度转换成弧度
		angleEnd = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 360.0)) % 360.0);

		// 使弧度值永远为正
		if (angleEnd < 0) {
			angleEnd += 2 * Math.PI;
		}

		angleStart = (float) ((float) ((Math.toDegrees(Math.atan2(pointXStart - cx, cy - pointYStart)) + 360.0)) % 360.0);

		if (angleEnd < angleStart) {
			sweepAngle = 360 - angleStart + angleEnd;
		} else {
			sweepAngle = angleEnd - angleStart;
		}

		angleStart += 270;

		pointXEnd = (float) (cx + ringRadius * Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));
		pointYEnd = (float) (cy + ringRadius * Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));

		invalidate();
	}

	public void movedStart(float x, float y) {
		// 如果触摸点在外圆半径的一个适配区域内

		// 将角度转换成弧度
		angleStart = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 360.0)) % 360.0);
		angleEnd = (float) ((float) ((Math.toDegrees(Math.atan2(pointXEnd - cx, cy - pointYEnd)) + 360.0)) % 360.0);

		// 使弧度值永远为正
		if (angleStart < 0) {
			angleStart += 2 * Math.PI;
		}

		if (angleStart > angleEnd) {
			sweepAngle = 360 - angleStart + angleEnd;
		} else {
			sweepAngle = angleEnd - angleStart;
		}

		angleStart -= 90;

		pointXStart = (float) (cx + ringRadius * Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));
		pointYStart = (float) (cy + ringRadius * Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));

		invalidate();
	}

	public static int dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2Dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}