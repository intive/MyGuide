
package com.blstream.myguide.path;

public class Heap {

	private Vertex[] mValues;
	private int mLastIndex;

	public Heap(int maxSize) {
		mLastIndex = 0;
		mValues = new Vertex[maxSize + 1];
	}

	public void add(Vertex v) {
		if (mLastIndex + 1 < mValues.length) {
			mLastIndex++;
			int index = mLastIndex;
			mValues[index] = v;
			repairUp(index);
		}
	}

	public Vertex poll() {
		if (mLastIndex < 1) { return null; }
		Vertex v = mValues[1];
		mValues[1] = mValues[mLastIndex];
		mLastIndex--;
		repairDown(1);
		return v;
	}

	public void repairDown(int index) {
		Vertex v = mValues[index];
		while (left(index) <= mLastIndex) {
			int leftIndex = left(index);
			int rightIndex = right(index);
			int smallest = leftIndex;
			if (rightIndex <= mLastIndex && mValues[rightIndex].compareTo(mValues[leftIndex]) == -1) {
				smallest = rightIndex;
			}
			if (mValues[smallest].compareTo(v) != -1) {
				break;
			}
			mValues[index] = mValues[smallest];
			mValues[index].setHeapIndex(index);
			index = smallest;
		}
		mValues[index] = v;
		mValues[index].setHeapIndex(index);
	}

	public void repairUp(int index) {
		if (index > mLastIndex) { return; }
		Vertex v = mValues[index];
		while (parent(index) >= 1 && mValues[parent(index)].compareTo(v) == 1) {
			mValues[index] = mValues[parent(index)];
			mValues[index].setHeapIndex(index);
			index = parent(index);
		}
		mValues[index] = v;
		v.setHeapIndex(index);
	}

	private int parent(int i) {
		return i / 2;
	}

	private int left(int i) {
		return i * 2;
	}

	private int right(int i) {
		return i * 2 + 1;
	}
}
