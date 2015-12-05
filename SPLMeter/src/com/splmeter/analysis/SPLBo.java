/**
 * 
 */
package com.splmeter.analysis;

/**
 * SPL数值BO对象
 * @author lzjing
 *
 */
public class SPLBo {
	
	//最大SPL值
	private double maxSPL = 0;
	//对应的最大频率（即主频mainF）
	private double maxFrequency = 0;
	//循环累加的spl值
	private double SPLValue = 0;
	//未累加的声压级（已做A计权修正）
	private double f_LpA[];
	//未累加的声压级（未做A计权修正）
	private double f_Lp[];
	
	/**
	 * @return the f_Lp
	 */
	public double[] getF_Lp() {
		return f_Lp;
	}
	/**
	 * @param f_Lp the f_Lp to set
	 */
	public void setF_Lp(double[] f_Lp) {
		this.f_Lp = f_Lp;
	}
	/**
	 * @return the f_LpA
	 */
	public double[] getF_LpA() {
		return f_LpA;
	}
	/**
	 * @param f_LpA the f_LpA to set
	 */
	public void setF_LpA(double[] f_LpA) {
		this.f_LpA = f_LpA;
	}
	/**
	 * @return the maxSPL
	 */
	public double getMaxSPL() {
		return maxSPL;
	}
	/**
	 * @param maxSPL the maxSPL to set
	 */
	public void setMaxSPL(double maxSPL) {
		this.maxSPL = maxSPL;
	}
	/**
	 * @return the maxFrequency
	 */
	public double getMaxFrequency() {
		return maxFrequency;
	}
	/**
	 * @param maxFrequency the maxFrequency to set
	 */
	public void setMaxFrequency(double maxFrequency) {
		this.maxFrequency = maxFrequency;
	}
	/**
	 * @return the sPLValue
	 */
	public double getSPLValue() {
		return SPLValue;
	}
	/**
	 * @param sPLValue the sPLValue to set
	 */
	public void setSPLValue(double sPLValue) {
		SPLValue = sPLValue;
	}

}
