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

	double maxSPL = 0;
	double maxFrequency = 0;
	double SPLValue = 0;
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
