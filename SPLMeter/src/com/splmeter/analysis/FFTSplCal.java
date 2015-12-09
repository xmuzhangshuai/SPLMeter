package com.splmeter.analysis;

import java.text.DecimalFormat;
import java.util.Arrays;

import com.splmeter.config.Constants.RecordValue;

/**
 * 
 * @author lzjing
 * E-mail：hanzhichu@qq.com
 * Describe:本类为提供声压级计算方法（采用快速傅里叶变换FFT提取音频信号的振幅和频率）
 * 
 */
public class FFTSplCal {

	int blockSize = RecordValue.BLOCKSIZE;
	int frequency = RecordValue.FREQUENCY;
	double cal_a = RecordValue.CALIBRATE_A;
	double cal_b = RecordValue.CALIBRATE_B;
	short[] buffer;
	private float[] toTransform;
	
	DecimalFormat nf = new DecimalFormat("0.0");
	FFT transformer;

	//初始化变量的值，以下值均为计算公式中固定的数值
	double f1 = 1.562339, f2 = Math.pow(107.65265, 2), f3 = Math.pow(737.86223, 2);
	double f4 = 2.242881E16, f5 = Math.pow(20.598997, 2), f6 = Math.pow(12194.22, 2);

	public FFTSplCal() {
		transformer = new FFT(blockSize, frequency);
	}

	/**
	 * 从声麦中读取数据到缓冲区中，再对缓冲区中内容做正向转换，主要用于初始化transformer
	 * @param bufferReadResult 通过audioRecord.read的返回值
	 */
	public void transBuffer(int bufferReadResult, short[] buffer) {
		toTransform = new float[blockSize];
		for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
			toTransform[i] = (float) (buffer[i]);
		}

		transformer.forward(toTransform);
	}

	/**
	 * 获取SPL等数值
	 * @return SPLBo
	 */
	public SPLBo getSPL() {
		//SPL的BO对象
		SPLBo splBo = new SPLBo();
		double spl = 0;

		int ALenght = transformer.specSize() * 2;

		double f_p[] = new double[ALenght];
		double f_Lp[] = new double[ALenght];
		double f_WA[] = new double[ALenght];
		double f_LpA[] = new double[ALenght];

		// 根据变换所产生的频谱的大小进行for循环（n次循环即为blockSize）处理得到最后的SPL
		for (int i = 1; i <= blockSize; i++) {

			//f为声音频率（Hz）
			double f = transformer.indexToFreq(i);
			double f_s = Math.pow(f, 2);
			double WA1, WA2;
			//计算WA1
			WA1 = 10 * Math.log10((f1 * Math.pow(f_s, 2)) / ((f_s + f2) * (f_s + f3)));
			//计算WA2
			WA2 = 10 * Math.log10((f4 * Math.pow(f_s, 2)) / (Math.pow((f_s + f5), 2) * Math.pow((f_s + f6), 2)));
			f_WA[i] = WA1 + WA2;
			//函数getBand(i)返回请求的频带的振幅
			f_p[i] = Math.pow(transformer.getBand(i), 2);
			//计算Lp（声压级）的数值，p0通过传声器和声卡量化后为1
			f_Lp[i] = 10 * Math.log10(f_p[i]);
			//计算Lpa（声压级）的数值(计算公式：Lpa = 10lg(10Lpa/10+10Wa/10))，A计权网络，对声压级进行计权修正
			f_LpA[i] = 10 * Math.log10(Math.pow(10,f_Lp[i]/10) + Math.pow(10,f_WA[i]/10));
//			System.out.println("声压级（振幅）："+Math.abs(f_LpA[i])+"  频率："+ f +" 角标："+ i +" 或者未修正-"+"声压级（振幅）："+Math.abs(f_Lp[i])+"  频率："+f +" 角标："+i);
			//最后进行叠加，得到总的声压级，循环累加计算SPL
			spl += Math.pow(10, f_LpA[i] / 10);
			//找出最大的声压级maxLpa,并找到对应的频率，即为主频mainF
			if (f_LpA[i] >= splBo.getMaxSPL()) {
				splBo.setMaxSPL(f_LpA[i]);
				splBo.setMaxFrequency(f);
			}
		}
		//循环累加计算得到的spl，得到SPL总声压级
		splBo.setSPLValue(spl);
		//将计权修正后的声压级存入数组中
		splBo.setF_LpA(f_LpA);
		return splBo;
	}
	

	/**
	 * 获得频率和声压级的数值用于绘制频谱图
	 * @return double[]
	 */
	public double[] getFrequencyAndSPL() {
		return getSPL().getF_LpA();
	}
	
	/**
	 * 获得校准之后的SPL值
	 * @param SPL 通过
	 * @param calibrateValue 配置中的基准值
	 * @return
	 */
	public String getCalibrateSPL(double SPL, double calibrateValue) {
		//return nf.format(10 * Math.log10(SPL) * cal_a - cal_b + calibrateValue);
		return nf.format(Math.round((10 * Math.log10(SPL) * cal_a  - cal_b + calibrateValue) * 10) / 10);
	}
	
	/**
	 * 获得校准之后的SPL值
	 * @param SPL 通过
	 * @param calibrateValue 配置中的基准值
	 * @return
	 */
	public float getCalibrateSPLDouble(double SPL, double calibrateValue) {
		return (float) Math.round((10 * Math.log10(SPL) * cal_a  - cal_b + calibrateValue) * 10) / 10;
		//	return (float)(10 * Math.log10(SPL) * 1.26067 - 82.00148 + calibrateValue);
	}
	
	/**
	 * 获取最大声压（声音的频率）的Hz，即单位为Hz
	 * @param maxFrequency 通过getSPL初始化后得到maxFrequency
	 * @return
	 */
	public String getMaxSudHz(double maxFrequency) {
		return nf.format(Math.round(maxFrequency * 10) / 10);
	}
	
	/**
	 * 获取最大声压（声音的频率）Hz，即单位为Hz
	 * @param maxFrequency 通过getSPL初始化后得到maxFrequency
	 * @return 保留一位小数
	 */
	public float getMaxSudHzDouble(double maxFrequency) {
		return (float) Math.round(maxFrequency * 10) / 10;
	}

	/**
	 * 获取最大声压（声音的强度）的dBA，即单位为dBA
	 * @param maxSPL 通过getSPL初始化后得到maxSPL
	 * @return
	 */
	public String getMaxSudBA(double maxSPL) {
		return nf.format(Math.round((maxSPL * cal_a - cal_b) * 10) / 10);
	}
	
	/**
	 * 获取最大声压（声音的强度）dBA，即单位为dBA
	 * @param maxSPL 通过getSPL初始化后得到maxSPL
	 * @return 保留一位小数
	 */
	public float getMaxSudBADouble(double maxSPL) {
		return (float) Math.round((maxSPL * cal_a - cal_b) * 10) / 10;
	}

	/**
	 * @return the toTransform
	 */
	public float[] getToTransform() {
		return toTransform;
	}

	/**
	 * @param toTransform the toTransform to set
	 */
	public void setToTransform(float[] toTransform) {
		this.toTransform = toTransform;
	}

}
