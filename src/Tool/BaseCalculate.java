package Tool;

import java.math.BigDecimal;

public class BaseCalculate {
	
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * �ṩ��ȷ�ļӷ����� 
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������ĺ�
	 */
	public static float add(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.add(b2).floatValue();
	}
	public static float add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).floatValue();
	}
	
	/**
	 * �ṩ��ȷ�ļ������� 
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������Ĳ�
	 */
	public static float substract(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.subtract(b2).floatValue();
	}
	
	/**
	 * �ṩ��ȷ�ĳ˷�����
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������Ļ�
	 */
	public static float multiply(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.multiply(b2).floatValue();
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ�������,�����������������ʱ,
	 * ��ȷ��С�����Ժ�10λ,�Ժ��������������.
	 * @param v1 ������
	 * @param v2 ����
	 * @return ������������
	 */
	public static float divide(float v1, float v2) {
		return divide(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ�������.
	 * �����������������ʱ,��scale����ָ ������,�Ժ��������������.
	 * 
	 * @param v1 ������
	 * @param v2 ����
	 * @param scale ��ʾ��Ҫ��ȷ��С�����Ժ�λ
	 * @return ������������
	 */
	public static float divide(float v1, float v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * �ṩ��ȷ��С��λ�������봦��
	 * @param v ��Ҫ�������������
	 * @param scale С���������λ
	 * @return ���������Ľ��
	 */
	public static float round(float v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		
		BigDecimal b = new BigDecimal(Float.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static void main(String args[]){
		System.out.println(""+add(12.32, 12.912));
		
	}

	
}