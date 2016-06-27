package com.example.bl_uestc.sercurity_bysound;

public class StringTransfer {

	/**
	 * ���ַ���ת������Ӧ�Ķ�����
	 * @param s
	 * @return
	 */
	public String StringToBin(String s){
		
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<s.length();i++){
			StringBuffer temp=new StringBuffer();
			byte b= (byte)s.charAt(i);
			temp.append(String.valueOf(Integer.toBinaryString(b)) );
			/*
			 * ǰ�治��8λ����0����
			 */
			for(int k=0;k<(8-temp.length());k++){
				sb.append("0");
			}
			sb.append(temp.toString());
		}
		return sb.toString();
	}
	
	
	/**
	 * ������ת���ַ���
	 * @param s
	 * @return
	 */
	public String BinToString(String s){
		StringBuffer sb=new StringBuffer();
		for (int i=0;i<s.length()/8;i++){
			int k=8*i;
			String buff=s.substring(k,k+8);
			int temp=Integer.parseInt(buff,2);
			sb.append((char)temp);
		
	}
	
		return sb.toString();
	}
	
}
