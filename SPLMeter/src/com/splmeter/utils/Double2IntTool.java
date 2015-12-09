package com.splmeter.utils;


public class Double2IntTool {

	public Double2IntTool() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Double类型数组转换成Short型
	 * @param data
	 * @return
	 */
	public static short[] Double2Short(double[] data) {
		int length = data.length;
		short tempArr[] = new short[length];
		for(int i = 0;i < length;i++){
			tempArr[i] = (short)((data[i]*10)/10);
		}
		return tempArr;
	}
	
	/**
	 * Double类型数组转换成Int型
	 * @param data
	 * @return
	 */
	public static int[] Double2Int(double[] data) {
		int length = data.length;
		int tempArr[] = new int[length];
		for(int i = 0;i < length;i++)
			tempArr[i] = (int)data[i];//增加这一句
		return tempArr;
	}

}
