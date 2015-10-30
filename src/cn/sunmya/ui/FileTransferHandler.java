package cn.sunmya.ui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

public class FileTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textarea;
	private MainFrame mainFrame;
	private String projectNamePrefix;
	private String projectName;
	private JTextField packageNameField;
	private JPasswordField passwordField;
	private JLabel message;// 用于显示错误信息的label
	private JLabel targetCopyPath;
	private String defaultTargetDir;
	private String currTimeStr;
	private JList fileJlist;
	private List<String> listFile;
	private JScrollPane jListScrollPane;
	private DefaultListModel<String> listModel;;

	public FileTransferHandler(JTextArea filePathList, MainFrame mainFrame) {
		this.textarea = filePathList;
		this.mainFrame = mainFrame;
		this.packageNameField = mainFrame.getPackageNameField();
		this.message = mainFrame.getMessage();
		this.targetCopyPath = mainFrame.getTargetCopyPath();
		this.defaultTargetDir = mainFrame.getDefaultTargetDir();
		this.currTimeStr = mainFrame.getCurrTimeStr();
		this.fileJlist = mainFrame.getFileJlist();
		this.listFile = mainFrame.getListFile();
		this.jListScrollPane = mainFrame.getjListScrollPane();
		this.listModel = mainFrame.getListModel();
		this.projectNamePrefix = mainFrame.getProjectNamePrefix();
		this.projectName = mainFrame.getProjectName();
	}

	@Override
	public boolean importData(JComponent c, Transferable t) {
		try {
			List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			Iterator<File> iterator = files.iterator();
			while (iterator.hasNext()) {
				File f = iterator.next();
				String filePath = f.getAbsolutePath();
				System.out.println(filePath);
				int firstIndex = filePath.indexOf(projectNamePrefix + File.separator + this.projectName);
				if (-1 == firstIndex) {
					message.setText("非" + projectName + "项目文件!");
				} else if (f.isHidden()) {
					message.setText("不能添加隐藏文件!");
				} else if (f.isFile()) {
					String currFilePath = filePath.substring(firstIndex + projectNamePrefix.length());
					String currFileTargetPath = mainFrame.getDefaultTargetDir() + currFilePath;
					if (listModel.contains(filePath)) {
						message.setText("已经包含文件:" + filePath + "\n");
						continue;
					}
					listModel.addElement(filePath);
					listFile.add(filePath);
					message.setText(currFilePath + " 文件添加成功!");
					mainFrame.getFileJlist().setModel(listModel);
					int index = listModel.lastIndexOf(listModel.lastElement());
					Point p = mainFrame.getFileJlist().indexToLocation(index);// 获得index的位置
					mainFrame.getjListScrollPane().getViewport().setViewPosition(p);
				} else if (f.isDirectory()) {
					String currFilePath = filePath.substring(firstIndex + projectNamePrefix.length());
					listModel.addElement(filePath);
					listFile.add(filePath);
					message.setText(currFilePath + " 目录添加成功!");
					mainFrame.getFileJlist().setModel(listModel);
					int index = listModel.lastIndexOf(listModel.lastElement());
					Point p = mainFrame.getFileJlist().indexToLocation(index);// 获得index的位置
					mainFrame.getjListScrollPane().getViewport().setViewPosition(p);

				} else {
					message.setText("未识别的奇葩文件..");

				}
			}

			return true;
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}
}