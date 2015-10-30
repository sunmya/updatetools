package cn.sunmya.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class FileUtil {
	
	private static Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * 复制一个文件夹或文件
	 * @param sourcePath 源路径
	 * @param targetPath 目的路径
	 * @throws IOException
	 */
	public static void copyFileOrDir(String sourcePath, String targetPath) throws IOException {
		File file = new File(sourcePath);
		// 获取源文件夹当前下的文件或目录
		if (file.isDirectory()) {
			copyDir(sourcePath, targetPath);
		} else if (file.isFile()) {
			copyFile(file, new File(targetPath));
		}

	}

	/**
	 * 判空字串
	 * 
	 * @param str
	 * @return 为空true
	 */
	public static boolean strIsNull(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 将目录文件打包成zip
	 * 
	 * @param srcPathName
	 * @param zipFilePath
	 * @return 成功打包true 失败false
	 */
	public static boolean compress(String srcPathName, String zipFilePath) {
		if (strIsNull(srcPathName) || strIsNull(zipFilePath))
			return false;

		File zipFile = new File(zipFilePath);
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			return false;
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		zip.addFileset(fileSet);
		zip.execute();
		return zipFile.exists();
	}


	/**
	 * 复制文件夹
	 * @param sourceDirPath
	 * @param targetDirPath
	 * @throws IOException
	 */
	public static void copyDir(String sourceDirPath, String targetDirPath) throws IOException {
		// 创建目标文件夹
		(new File(targetDirPath)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDirPath)).listFiles();
		for (int i = 0; i < file.length; i++) {

			if (file[i].isHidden()) {
				logger.info(file[i].getName());
			} else if (file[i].isFile()) {
				// 复制文件
				copyFile(file[i], new File(targetDirPath + file[i].getName()));
			} else if (file[i].isDirectory()) {
				// 复制目录
				String sourceDir = sourceDirPath + File.separator + file[i].getName();
				String targetDir = targetDirPath + File.separator + file[i].getName();
				copyDirectiory(sourceDir, targetDir);
			}

		}
	}

	/**
	 * 复制文件夹
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 复制文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	public static void makeSheel(List<String> listModel, String packagePath,String packageName) throws Exception {
		File shellfile = new File(packagePath+"\\install.sh");
		
		if(!shellfile.exists()){
			shellfile.createNewFile();
		}
		FileWriter writer = new FileWriter(shellfile);
		
		String bakPath = "/home/bak/"+packageName;
		
		writer.write("#!/bin/bash\n\n");
		writer.write("mkdir -p "+bakPath+"/mspweb\n");
		writer.write("echo 请输入系统所在路径,默认为[/home]\n");
		writer.write("read projectpath\n");
		writer.write("if [ x$projectpath = \"x\" ]\n");
		writer.write("then\n");
		writer.write("projectpath=/home\n");
		writer.write("fi\n");
		writer.write("echo 系统所在路径：$projectpath/mspweb\n");
		int i=4;
		for(String filePath:listModel){
			filePath=filePath.replaceAll("\\\\", "/");
			System.out.println(filePath);
			if(filePath.lastIndexOf("/")>0){
				writer.write("mkdir -p "+bakPath+filePath.substring(0, filePath.lastIndexOf("/"))+"\n");
				i++;
			}
			writer.write("cp -r $projectpath"+filePath+" "+bakPath+filePath+"\n");
			i++;
		}
		writer.write("mkdir -p "+"/home/upload/\n");
		writer.write("tail  -n+"+(i+37)+" $0 >/home/upload/"+packageName+".zip\n");
		writer.write("unzip /home/upload/"+packageName+".zip -d $projectpath/\n");
		//重启tomcat
		writer.write("PROCEFULLNAME=tomcat\n");
		writer.write("prognum=`ps -ef | grep $PROCENAME | grep -v grep | wc -l`\n");
		writer.write("source /etc/profile \n");
		writer.write("\n");
		writer.write("if [ $prognum -ne 0  ]\n");
		writer.write("then\n");
		writer.write("		sshpid=`/bin/ps -ef | /bin/grep $PROCENAME  | /bin/grep -v grep | /bin/awk '{print $2}'`\n");
		writer.write("		echo `date` $sshpid \"will be killed\"\n");
		writer.write("    /bin/kill -9 $sshpid\n");
		writer.write("    sleep 3\n");
		writer.write("    if [ $prognum -eq 0  ]\n");
		writer.write("		then\n");
		writer.write("			echo `date` $PROCENAME \"is killed\"\n");
		writer.write("		else\n");
		writer.write("			echo `date` $PROCENAME \"is not killed\"\n");
		writer.write("		fi\n");
		writer.write("fi\n");
		writer.write("echo `date` \"restarting\" $PROCENAME\n");
		writer.write("$tomcatpath/bin/start.sh\n");
		writer.write("sleep 3\n");
		writer.write("if [ $prognum -eq 0  ]\n");
		writer.write("then\n");
		writer.write("	echo `date` \"restart\" $PROCENAME \"failure, please manual restart \"\n");
		writer.write("else\n");
		writer.write("	echo `date`  $PROCENAME \"restart success\"\n");
		writer.write("fi\n");
		writer.write("exit 0");
		
		writer.close();
		
	}
}
