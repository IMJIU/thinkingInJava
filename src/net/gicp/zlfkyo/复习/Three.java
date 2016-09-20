package net.gicp.zlfkyo.¸´Ï°;


import java.util.Random;
import java.util.*;
public class Three {
	public static void main(String[] args) {
		Random random=new Random();
		int a=random.nextInt();
		int b=random.nextInt();
		//f1(a);
		//f2(a);
		//f(a);
		//f(b);
		//f(a^b);
		a=-11;
		System.out.println(a);
		f(a);
		f(a>>4);
		f(a>>>4);
	}
	public static void f(int a){
		System.out.println(a+" -bit:"+Integer.toBinaryString(a));
		
	}
	public static void f1(int a){
		while (a>0) {
			System.out.println(Integer.toBinaryString(a>>=1));
		}
	}
	public static void f2(int a){
		while (a>0) {
			System.out.println(Integer.toBinaryString(a>>>=1));
		}
	}
	public static void f3(int a){
		while (a>0) {
			System.out.println(Integer.toBinaryString(a));
			a>>=1;
		}
	}
}
