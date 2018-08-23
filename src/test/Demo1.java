package test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class Demo1 {
	@Test
	public void fun1() {
		/*
		 * 包含了点位符的字符串就是模板！
		 * 点位符：{0}、{1}、{2}
		 * 可变参数，需要指定模板中的点位符的值！有几个点位符就要提供几个参数
		 */
		String s = MessageFormat.format("{0}或{1}错误！", "用户名", "密码");
		System.out.println(s);
	}
	@Test
	public void fun2(){
		
		
	}
	@Test
	public void fun3(){
		BigInteger sum= BigInteger.valueOf(1);
		for (int i=1;i<100;i++){
		    BigInteger bi = BigInteger.valueOf(i);	
			sum=sum.multiply(bi);
		}
		System.out.print(sum);
		
	}
	@Test
	public void fun4(){
		/*
		 * 创建BigDecimal对象时候，必须使用string构造器
		 * 处理二进制一起的误差
		 * */
		BigDecimal d1 = new BigDecimal("2.0");
		BigDecimal d2 = new BigDecimal("1.1");
		BigDecimal d3 = d1.subtract(d2);
		System.out.println(d3);
		
	}
}
