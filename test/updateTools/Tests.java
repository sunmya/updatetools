package updateTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tests {

	public static void main(String[] args) {
		System.out.println("请输入一个数字");
		Scanner scanner =  new Scanner(System.in);
		
		int num = scanner.nextInt();
		
		int[] nums = {1,2,3,4,5,6,7,8,9,0};
		String[] strs = {"+","-",""};
		String S="";
		int n=0;
		int temp=0;
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<nums.length;i++){
			int num2 = nums[i];
			for(String s:strs){
				switch (S) {
				case "+":
					if("".equals(s)){
						temp=num2;
					}else{
						n+=num2;
						temp=0;
					}
					break;
				case "-":
					if("".equals(s)){
						temp=num2;
					}else{
						n-=num2;
						temp=0;
					}
					break;
				case "":
					temp=temp*10+num2;
				break;
				
				default:
					break;
				}
				S=s;
				buffer.append(S+""+num2);
				System.out.println(buffer.toString());
			}
		}
		
		
		/*boolean flag=false;
		List<Integer> numList = new ArrayList<Integer>();
		while(num>10){
			numList.add(0, num%10);
			num = num/10;
		}
		numList.add(0, num%10);
		System.out.println(numList);
		int min=Integer.MAX_VALUE;
		for(int i=0;i<nums.length;i++){
			int nu = 0;
			for(int j=0;j<numList.size();j++){
				if(i+j<nums.length){
					nu=nu*10+nums[i+j];
				}else{
					break;
				}
			}
			//System.out.println(nu);
			if(Math.abs(num2-nu)<min){
				min=Math.abs(num2-nu);
				num = nu;
			}
			System.out.println(num);
		}
		*/
	}
}
