package com.splmeter.analysis;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

public class DrawProcess {
	//应该把处理前后处理后的普线都显示出来
	private ArrayList<int[]> outBuf = new ArrayList<int[]>();//处理后的数据
	private int shift = 20;

	//y轴缩小的比例
	public int rateY = 25;
	//y轴基线
	public int baseLine = 0;
	//画板
	private SurfaceView sfvSurfaceView;
	//画笔
	private Paint mPaint;

	public DrawProcess(SurfaceView sfvSurfaceView) {
		this.sfvSurfaceView = sfvSurfaceView;
		initDraw();
	}

	//初始化画图的一些参数
	public void initDraw() {
		//设置画笔属性
		mPaint = new Paint();
		mPaint.setColor(Color.argb(255, 7, 251, 251));
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
	}

	public void draw(int length, short[] buffer) {
		short[] tmpBuf = new short[length];
		System.arraycopy(buffer, 0, tmpBuf, 0, length);

		Complex[] complexs = new Complex[length];
		int[] outInt = new int[length];
		for (int i = 0; i < length; i++) {
			Short short1 = tmpBuf[i];
			complexs[i] = new Complex(short1.doubleValue());
		}
		//					fft(complexs, length);
		for (int i = 0; i < length; i++) {
			i = i + 1;
			outInt[i] = complexs[i].getIntValue();
		}
		synchronized (outBuf) {
			outBuf.add(outInt);
		}

		ArrayList<int[]> buf = new ArrayList<int[]>();
		synchronized (outBuf) {
			if (outBuf.size() == 0) {
				return;
			}
			buf = (ArrayList<int[]>) outBuf.clone();
			outBuf.clear();
		}
		//根据ArrayList中的short数组开始绘图
		for (int i = 0; i < buf.size(); i++) {
			int[] tmpBuf1 = buf.get(i);
			SimpleDraw(tmpBuf1, baseLine);
		}
	}

	private void SimpleDraw(int[] buffer, int baseLine) {
		Canvas canvas = sfvSurfaceView.getHolder().lockCanvas(new Rect(0, 0, buffer.length, sfvSurfaceView.getHeight()));
		canvas.drawColor(Color.BLACK);
		canvas.save();
		canvas.rotate(-60, sfvSurfaceView.getWidth() - 1, baseLine);
		canvas.restore();

		int y;
		for (int i = 0; i < buffer.length; i = i + 1) {
			y = baseLine - buffer[i] / rateY;
			canvas.drawLine(2 * i + shift, baseLine, 2 * i + shift, y, mPaint);
		}
		sfvSurfaceView.getHolder().unlockCanvasAndPost(canvas);
	}
}
