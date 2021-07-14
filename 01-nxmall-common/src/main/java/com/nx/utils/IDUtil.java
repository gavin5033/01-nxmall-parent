package com.nx.utils;

import java.util.Random;

/**
 * 各种id生成策略
 * @version 1.0
 */
public class IDUtil {

	/**
	 * 图片名生成
	 */
	public static String genImageName() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
		//如果不足三位前面补0
		String str = millis + String.format("%03d", end3);
		return str;
	}
	
	/**
	 * 商品id生成
	 * orderid生成
	 * seckillid生产
	 *
	 */
	public static long nextId() {
		IdWorker idWorker=new IdWorker(0,0); //机器id 与序列id ，也可以不传
		return idWorker.nextId();
	}
	
	public static void main(String[] args) {
		for(int i=0;i< 100;i++)
		System.out.println(nextId());
	}


}
