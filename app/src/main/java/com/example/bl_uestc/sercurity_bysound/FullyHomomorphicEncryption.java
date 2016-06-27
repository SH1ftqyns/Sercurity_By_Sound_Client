package com.example.bl_uestc.sercurity_bysound;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
 
public class FullyHomomorphicEncryption {
 
 
    private int gamma;
    private int rho;
    private int eta;
    private int tau;
 
    private BigInteger secretKey;
    private ArrayList<BigInteger> pkey;
 
    public FullyHomomorphicEncryption() {
 
        gamma = 1300;
        rho = 26;
        eta = 830;
        tau = 158;
     
	}
	 
    public FullyHomomorphicEncryption(int gamma, int rho, int eta, int tau) {
 
        this.gamma = gamma;
        this.rho = rho;
        this.eta = eta;
        this.tau = tau;
     
	}
 
    public BigInteger getSecretKey() {
 
        return secretKey;
     
	}	
 
    public void setSecretKey(BigInteger secretKey) {
 
        this.secretKey = secretKey;
     
	}
 
    /**
     * ����һ��BigInteger �б�
     * @return
     */
    public ArrayList<BigInteger> getPkey() {
 
        return pkey;
     
	}
	
	
	/*
	���²�����������Կ����Կ��˽Կ��
	
	*/
	
    public void generateKey() {
 
        pkey = new ArrayList<>();
		
		//eta=830,����һ��2��eta-1�ε�2��eta�ε�������Ϊ˽Կ
        BigInteger ll = new BigInteger("2").pow(eta - 1);
        BigInteger ul = new BigInteger("2").pow(eta);
        BigInteger r;
        Random rnd = new Random();
        while (true) {
 
            r = new BigInteger(ul.bitLength(), rnd);
            if ((r.compareTo(ll) == 1 && r.compareTo(ul) == -1) && (r.mod(new BigInteger("2")) != BigInteger.ZERO)) {
 
                break;
             
			}
         
		}
        secretKey = r;
        BigInteger p = secretKey;
 
//        System.out.println("secretKey = " + secretKey);
        ll = BigInteger.ZERO;
		
		//ul=��2��gamma�γ���˽Կ-1��
        ul = new BigInteger("2").pow(gamma).divide(secretKey).subtract(BigInteger.ONE);
 
        ArrayList<BigInteger> qtau = new ArrayList<>();
		
		//����һ������qtau������Ϊtau+1�����е�ֵ�ķ�Χ��0��ul��������
        for (int i = 0; i <= tau; i++) {
 
            while (true) {
 
                r = new BigInteger(ul.bitLength(), rnd);
                if ((r.compareTo(ll) == 1 && r.compareTo(ul) == -1)) {
 
                    break;
                 
				}
             
			}
            qtau.add(r);
         
		}
        Collections.sort(qtau);
        Collections.reverse(qtau);
		
		//��qtau�е�һ����λż���������1
        if (qtau.get(0).mod(new BigInteger("2")) == BigInteger.ZERO) {
 
            BigInteger ti = qtau.get(0).add(BigInteger.ONE);
            qtau.set(0, ti);
         
		}
 
        ll = BigInteger.ONE;
		//ul=2��rho��-1
        ul = new BigInteger("2").pow(rho).subtract(BigInteger.ONE);
		
		//
        while (true) {
 
            r = new BigInteger(ul.bitLength(), rnd);
            if ((r.compareTo(ll) == 1 && r.compareTo(ul) == -1)) {
 
                break;
             
			}
         
		}
        BigInteger x0 = qtau.get(0).multiply(p).add(new BigInteger("2").multiply(r));
        pkey.add(x0);
        for (int i = 1; i <= tau; i++) {
 
            while (true) {
 
                r = new BigInteger(ul.bitLength(), rnd);
                if ((r.compareTo(ll) == 1 && r.compareTo(ul) == -1)) {
 
                    break;
                 
				}
             
			}
            BigInteger x1 = qtau.get(i).multiply(p).add((new BigInteger("2").multiply(r)).mod(x0));
            pkey.add(x1);
         
		}
 
     
	}
 
	/*
	
		���²�����̬ͬ���ܵļ��ܲ���
		
	*/
		
	
    public ArrayList<BigInteger> encrypt(String bits) {
 
        ArrayList<BigInteger> enc = new ArrayList<>();
        int subSize = (int) (Math.random() * (double) tau);
        if (subSize == 0) {
 
            subSize = 1;
         
		}
        int rn;
        ArrayList<Integer> S = new ArrayList<>();
        for (int i = 0; i < subSize; i++) {
 
            do {
 
                rn = (int) (Math.random() * (double) tau);
                if (rn == 0) {
 
                    rn = 1;
                 
				}
 
             
			} while (S.contains(rn));
            S.add(rn);
         
		}
 
        BigInteger sum = BigInteger.ZERO;
        for (Integer i : S) {
 
            sum = sum.add(pkey.get(i).mod(pkey.get(0)));
         
		}
 
        BigInteger ll = BigInteger.ONE;
        BigInteger ul = new BigInteger("2").pow(rho).subtract(BigInteger.ONE);
        Random rnd = new Random();
        BigInteger r;
        while (true) {
 
            r = new BigInteger(ul.bitLength(), rnd);
            if ((r.compareTo(ll) == 1 && r.compareTo(ul) == -1)) {
 
                break;
             
			}
         
		}
 
        for (int i = 0; i < bits.length(); i++) {
 
            String substring = bits.substring(i, i + 1);
            BigInteger m = new BigInteger(substring);
            BigInteger c = m.add(new BigInteger("2").multiply(r)).add(sum);
			while (true) {
 
            r = new BigInteger(ul.bitLength(), rnd);
            if ((r.compareTo(ll) == 1 && r.compareTo(ul) == -1)) {
 
                break;
             
			}
         
		}
			//System.out.println(c);
            enc.add(c);
         
		}
        return enc;
     
	}
	
	/*
	
		������̬ͬ���ܵĽ��ܲ���
		
	*/
    public String decrypt(ArrayList<BigInteger> enc) {
 
        StringBuilder decoded = new StringBuilder();
        for (BigInteger enc1 : enc) {
 
            BigInteger mod = enc1.mod(secretKey).mod(new BigInteger("2"));
            decoded.append(mod.toString());
         
		}
        return decoded.toString();
     
	}
	

	
}

class Adder{
	
	public static void ADD(ArrayList<BigInteger> encrypt1, ArrayList<BigInteger> encrypt2, ArrayList<BigInteger> encrypt3){
		
		BigInteger C = BigInteger.ZERO;
        for (int i = encrypt1.size()-1; i >= 0; i--) {
			
            encrypt3.set(i, encrypt1.get(i).add(encrypt2.get(i)).add(C));
			C = (encrypt1.get(i).add(encrypt2.get(i))).multiply(C).add(encrypt1.get(i).multiply(encrypt2.get(i)));

		}

	}
 
}
