package com.vikings.sanguo.model;

public class Rect {
	private int left;
	private int top;
	private int right;
	private int bottom;

	public Rect() {

	}

	public Rect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public boolean isInRect(int x, int y) {
		return left <= x && right >= x && top <= y && bottom >= y;
	}

	public boolean isInRect(float x, float y) {
		return left <= x && right >= x && top <= y && bottom >= y;
	}

	public boolean isInRect(Rect rect) {
		return isInRect(rect.getLeft(), rect.getTop())
				|| isInRect(rect.getLeft(), rect.getBottom())
				|| isInRect(rect.getRight(), rect.getTop())
				|| isInRect(rect.getRight(), rect.getBottom());
	}
}
