package cn.sunmya.util;

import java.io.IOException;

public class CMD {

	/**
	 * 调用 cmd命令打开路径
	 * @param targetDir
	 * @throws IOException
	 */
	public static void openTargetDir(String targetDir) throws IOException {
		Runtime.getRuntime().exec("cmd.exe /c start " + targetDir);
	}
	
	public static void makeRun(String targetDir,String packagename) throws IOException {
		String cmd = "copy /b " + targetDir+"install.sh+"+targetDir+packagename+".zip "+targetDir+packagename+".run";
		System.out.println(cmd);
		Runtime.getRuntime().exec("cmd.exe /c "+cmd);
	}


}
