package com.willen.topFloatDemo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ScrollView;

/*
 * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听，
 * 我们很自然的想到在onTouchEvent()方法中实现对滚动Y轴进行监听
 * ScrollView的滚动Y值进行监听
 */
public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	/** 
	 * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较 
	 */
	private int lastScrollY;
	/** 暂时没用的 */
	GestureDetector mGestureDetector;
	OnScrollChangedListener onScrollChangedListener;

	public MyScrollView(Context context) {
		super(context, null);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		mGestureDetector = new GestureDetector(context, new CustomGestureListener());
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/** 
	 * 设置滚动接口 
	 * @param onScrollListener 
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	/** 
	 * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = MyScrollView.this.getScrollY();

			//此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息  
			if (lastScrollY != scrollY) {
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
			}
			if (onScrollListener != null) {
				onScrollListener.onScroll(scrollY);
			}

		};

	};

	/** 
	 * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候， 
	 * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候， 
	 * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理 
	 * MyScrollView滑动的距离 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (onScrollListener != null) {
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			handler.sendMessageDelayed(handler.obtainMessage(), 20);
			break;
		default:
			//			return mGestureDetector.onTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	/**
	*
	 * @param onScrollChangedListener
	*/
	public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
		this.onScrollChangedListener = onScrollChangedListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {
		super.onScrollChanged(x, y, oldX, oldY);
		if (onScrollChangedListener != null) {
			onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
		}
	}

	public interface OnScrollChangedListener {
		public void onScrollChanged(int x, int y, int oldxX, int oldY);
	}

	/** 
	 * 滚动的回调接口 
	 */
	interface OnScrollListener {
		/** 
		 * 回调方法， 返回MyScrollView滑动的Y方向距离 
		 */
		public void onScroll(int scrollY);
	}

	class CustomGestureListener implements GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			int dis = (int) ((distanceY - 0.5) / 2);
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			onScrollListener.onScroll((int) velocityY);
			return false;
		}
	}

}