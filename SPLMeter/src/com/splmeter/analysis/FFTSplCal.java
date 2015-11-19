package com.splmeter.analysis;

import java.text.DecimalFormat;

/**
 * 
 * @author lzjing
 * E-mail：hanzhichu@qq.com
 * Describe:本类为提供声压级计算方法（采用快速傅里叶变换FFT提取音频信号的振幅和频率）
 * 
 */
public class FFTSplCal {

	int blockSize = 2048;
	int frequency = 8000;
	short[] buffer;
	DecimalFormat nf = new DecimalFormat("0.0");
	FFT transformer;
	
	//初始化变量的值，以下值均为计算公式中固定的数值
	double f1 = 1.562339, f2 = Math.pow(107.65265, 2), f3 = Math.pow(737.86223, 2);
	double f4 = 2.242881E16, f5 = Math.pow(20.598997, 2), f6 = Math.pow(12194.22, 2);
	
	public FFTSplCal(){
		transformer = new FFT(blockSize, frequency);
	}
	
	/**
	 * 从声麦中读取数据到缓冲区中，再对缓冲区中内容做正向转换，主要用于初始化transformer
	 * @param bufferReadResult 通过audioRecord.read的返回值
	 */
	protected void transBuffer(int bufferReadResult) {
		buffer = new short[blockSize];
		float[] toTransform = new float[blockSize];
		for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
			toTransform[i] = (float) (buffer[i]);
		}

		transformer.forward(toTransform);
	}
	
	/**
	 * 获取SPL等数值
	 * @return SPLBo
	 */
	protected SPLBo getSPL() {
		//SPL的BO对象
		SPLBo splBo = new SPLBo();

		int ALenght = transformer.specSize();

		double f_p[] = new double[ALenght];
		double f_Lp[] = new double[ALenght];
		double f_WA[] = new double[ALenght];
		double f_LpA[] = new double[ALenght];

		// 根据变换所产生的频谱的大小（为采样缓存的大小n/2+1）进行for循环处理得到最后的SPL
		for (int i = 1; i < transformer.specSize(); i++) {

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
			//计算Lp的数值
			f_Lp[i] = 10 * Math.log10(f_p[i]);
			//计算Lpa的数值
			f_LpA[i] = f_Lp[i] + f_WA[i];
			//最后进行叠加，得到总的声压级
			splBo.SPL += Math.pow(10, f_LpA[i] / 10);
			//找出最大的声压级
			if (f_LpA[i] >= splBo.maxSPL) {
				splBo.maxSPL = f_LpA[i];
				splBo.maxFrequency = f;
			}
		}
		return splBo;
	}
	
	/**
	 * 获得校准之后的SPL值
	 * @param SPL 通过
	 * @param calibrateValue 配置中的基准值
	 * @return
	 */
	protected String getCalibrateSPL(double SPL , double calibrateValue) {
		return nf.format(10 * Math.log10(SPL) * 1.26067 - 82.00148 + calibrateValue);
	}
	
	/**
	 * 获取最大声压的dBA，即最大声压单位为dBA
	 * @param maxSPL 通过getSPL初始化后得到maxSPL
	 * @return
	 */
	protected String getMaxSudBA(double maxSPL) {
		return nf.format(maxSPL * 1.26067 - 82.00148);
	}
	
	/**
	 * 获取最大声压的Hz，即最大声压单位为Hz
	 * @param maxFrequency 通过getSPL初始化后得到maxFrequency
	 * @return
	 */
	protected String getMaxSudHz(double maxFrequency) {
		return nf.format(maxFrequency);
	}

}
