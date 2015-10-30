package cn.sunmya.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.io.FileUtils;

import cn.sunmya.util.CMD;
import cn.sunmya.util.FileUtil;
import cn.sunmya.util.PropertiesUtils;

/**
 * 菜单窗口
 * 
 */
public class MainFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectNamePrefix = PropertiesUtils.getProperty("projectNamePrefix");
	private String projectName = PropertiesUtils.getProperty("projectname");
	private String version = PropertiesUtils.getProperty("version");
	private String defaultTargetDir = PropertiesUtils.getProperty("defaultTargetDir");
	private String title = PropertiesUtils.getProperty("title");
	
	private String packageDir = "_UpdatePackages";
	private String packageName = projectName + "_" + version + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	private JTextField packageNameField = new JTextField(packageName);
	private JLabel message = new JLabel("准备就绪", JLabel.LEFT);// 用于显示错误信息的label
	private JLabel targetCopyPath = new JLabel(defaultTargetDir + packageName);
	

	private List<String> listFile = new ArrayList<String>();
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> fileJlist = new JList<String>(listModel);
	private JScrollPane jListScrollPane = new JScrollPane(fileJlist);
	private List<String> pathList = new ArrayList<String>();

	JButton changePackageNameBtn = new JButton("更改包名");
	final JButton delFileBtn = new JButton("撤销选中对象");
	final JButton openTargetDirectoryBtn = new JButton("打开生成目录");
	final JButton createZipBtn = new JButton("生成压缩包");
	final JButton startCopyFileBtn = new JButton("开始复制文件");

	JPanel pnlMain;
	JTextField txtfile;
	JButton btnSelect;
	JFileChooser fc = new JFileChooser();

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

	/**
	 * 构造方法
	 */
	public MainFrame() {
		init();// 初始化界面

	}

	/**
	 * 界面初始化方法
	 */
	private void init() {
		// 设置窗口标题
		this.setTitle(title);
		// 设置窗口大小
		this.setSize(650, 400);
		// 居中
		this.setLocationRelativeTo(null);
		// 向窗口中添加组件
		this.setContentPane(createContentPane());
		// 设置是为可以最大化
		this.setResizable(true);
		// this.setAlwaysOnTop(true);
		// 设置图标
		ImageIcon icon = new ImageIcon(this.getClass().getResource("logo-.jpg"));
		this.setIconImage(icon.getImage());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		/**
		 * 为当前窗口添加窗口监听器,对窗口关闭事件作出相应
		 */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});

		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("选项");
		JMenu m3 = new JMenu("帮助");
		JMenuItem mi1 = new JMenuItem("新建升级包");

		JMenuItem mi31 = new JMenuItem("使用说明");
		m1.add(mi1);
		m3.add(mi31);

		mb.add(m1);

		mb.add(m3);
		this.setJMenuBar(mb);

		mi1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem target = (JMenuItem) e.getSource();
				String actionCommand = target.getActionCommand();
				if (actionCommand.equals("新建升级包")) {// do something}
					// System.out.println("新建升级包");
					showMessage("准备就绪");
					message.setForeground(Color.GREEN);
					packageName = projectName + "_" + version + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					message.setText("准备就绪");
					packageNameField.setText(packageName);
					targetCopyPath.setText(defaultTargetDir + packageName);
					listModel.clear();
					changePackageNameBtn.setEnabled(true);
					delFileBtn.setEnabled(true);
					startCopyFileBtn.setEnabled(true);
					openTargetDirectoryBtn.setEnabled(false);
					createZipBtn.setEnabled(false);

					MainFrame.this.repaint();
				}

			}
		});

		mi31.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem target = (JMenuItem) e.getSource();
				String actionCommand = target.getActionCommand();
				if (actionCommand.equals("使用说明")) {// do something}
					System.out.println("使用说明");
					StringBuffer sb = new StringBuffer();
					File file = new File("");
					FileReader reader;
					try {
						reader = new FileReader(file);
					
						char[] cbuf = new char[1024];
						while (reader.read(cbuf)!=-1) {
							sb.append(cbuf);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					new AboutFrame(sb.toString());
					// JOptionPane.showMessageDialog(null, sb.toString(),
					// "使用说明", JOptionPane.NO_OPTION);
				}

			}
		});
		message.setForeground(Color.GREEN);
		message.setFont(message.getFont().deriveFont(16));
	}

	// 创建主Panel
	private JPanel createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		// 为Panel加入边框 上 左 下 右

		panel.setBorder(new EmptyBorder(1, 1, 10, 1));
		final JTextArea textarea = new JTextArea("将文件拖进此区域", 5, 10);
		textarea.setEditable(false);

		textarea.setTransferHandler(new FileTransferHandler(textarea, this));

		DocumentListener myListener = new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				JOptionPane.showMessageDialog(null, "changedUpdate");
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

		};
		textarea.getDocument().addDocumentListener(myListener);
		JScrollPane scrollPane = new JScrollPane(textarea);
		panel.add(scrollPane, BorderLayout.NORTH);
		panel.add(createCenterPane(), BorderLayout.CENTER);
		panel.add(createSouthPane(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createSouthPane() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(new EmptyBorder(8, 8, 8, 8));
		panel.add(message, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createCenterPane() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(new EmptyBorder(8, 8, 8, 8));
		panel.add(createShowCopyFilePathPane(), BorderLayout.NORTH);

		panel.add(createShowFileListPane(), BorderLayout.CENTER);
		return panel;
	}

	private JPanel createShowFileListPane() {

		JPanel panel = new JPanel(new BorderLayout(6, 0));
		panel.setBorder(new EmptyBorder(6, 0, 10, 5));

		panel.add(createShowFileJListPane(), BorderLayout.CENTER);

		return panel;
	}

	private JPanel createShowCopyFilePathPane() {
		JPanel panel = new JPanel(new BorderLayout(6, 0));
		panel.setBorder(new EmptyBorder(6, 0, 10, 5));
		panel.add(selectPane(), BorderLayout.WEST);
		panel.add(createTargetCopyFilePathPane(), BorderLayout.EAST);

		return panel;
	}

	private JPanel createTargetCopyFilePathPane() {
		JPanel panel = new JPanel();
		// 注意!!!!!!!!!!!!!!!!!!!!!
		panel.add(new JLabel("包名为:"));

		panel.add(packageNameField);

		// 注意!!!!!!!!!!!!!!!!!!!

		// 给登陆按钮添加一个单击事件
		changePackageNameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "更改包名");
				String newPackageName = defaultTargetDir + packageNameField.getText();
				packageName = packageNameField.getText();
				targetCopyPath.setText(newPackageName);
				message.setForeground(Color.GREEN);
				message.setText("包名更改为:" + newPackageName);
			}
		});
		panel.add(changePackageNameBtn);

		return panel;
	}

	private JPanel createShowFileJListPane() {
		JPanel panel = new JPanel(new BorderLayout(6, 0));

		panel.add(jListScrollPane, BorderLayout.CENTER);

		panel.add(createBtnPane(), BorderLayout.EAST);

		return panel;
	}

	/**
	 * 选择框
	 * 
	 * @return
	 */
	private JPanel selectPane() {
		pnlMain = new JPanel();
		this.getContentPane().add(pnlMain);
		// 构造一个具有指定列数的新的空 TextField
		txtfile = new JTextField(10);
		txtfile.setText(defaultTargetDir);
		pnlMain.add(new JLabel("复制到:"));
		btnSelect = new JButton("选择");
		btnSelect.addActionListener(this);
		pnlMain.add(txtfile);
		pnlMain.add(btnSelect);
		return pnlMain;
	}

	/**
	 * 创建存放按钮的Panel
	 */
	private JPanel createBtnPane() {
		JPanel panel = new JPanel(new GridLayout(4, 1, 0, 6));

		delFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int[] selectIndexArr = fileJlist.getSelectedIndices();
				if (selectIndexArr.length <= 0) {
					showError("你没有选择文件!");
				} else {
					DefaultListModel<String> listModelTemp = new DefaultListModel<String>();
					for (int i = 0; i < selectIndexArr.length; i++) {
						System.out.println(i);
						listModelTemp.addElement(listModel.get(selectIndexArr[i]));
					}

					for (int i = 0; i < listModelTemp.size(); i++) {
						listModel.removeElement(listModelTemp.getElementAt(i));
					}
					message.setForeground(Color.red);
					message.setText("撤销了" + selectIndexArr.length + "个对象. 剩余" + listModel.getSize() + "个对象.");
				}

			}
		});

		createZipBtn.setEnabled(false);
		/**
		 * 压缩文件
		 */
		createZipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "删除选中文件");
				String packagePath = txtfile.getText() + "\\" + packageNameField.getText() + packageDir + File.separator;
				FileUtil.compress(getTargetCopyPathTextVal(), packagePath + packageName + ".zip");
				try {
					File packagePathDir = new File(packagePath);
					if (!packagePathDir.exists()) {
						packagePathDir.mkdirs();
					}
					FileUtil.makeSheel(pathList,packagePath,packageName);
					CMD.makeRun(packagePath, packageName);
					CMD.openTargetDir(packagePath);
				} catch (Exception e1) {
					showError("生成失败!请手动检查.");
					e1.printStackTrace();
				}
			}
		});

		startCopyFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "删除选中文件");
				message.setForeground(Color.BLACK);
				if (listModel.size() == 0) {
					showError("没有文件可以复制!");
					return;
				}
				startCopyFile();

				changePackageNameBtn.setEnabled(false);
				startCopyFileBtn.setEnabled(false);
				delFileBtn.setEnabled(false);
				createZipBtn.setEnabled(true);
				openTargetDirectoryBtn.setEnabled(true);

			}
		});

		openTargetDirectoryBtn.setEnabled(false);
		openTargetDirectoryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CMD.openTargetDir(getTargetCopyPathTextVal());
				} catch (IOException e1) {
					showError("打开失败!请手动检查.");
					e1.printStackTrace();
				}

			}
		});

		panel.add(delFileBtn);
		panel.add(startCopyFileBtn);
		panel.add(openTargetDirectoryBtn);
		panel.add(createZipBtn);
		return panel;
	}

	// 获取复制后的路径
	public String getTargetCopyPathTextVal() {
		System.out.println(packageNameField.getText());
		String text = txtfile.getText() + "\\" + packageNameField.getText();
		return text + File.separator;
	}

	
	public void startCopyFile() {
		pathList.clear();
		for (int i = 0; i < listModel.size(); i++) {
			String sourceFilePath =listModel.get(i);
			int firstIndex = sourceFilePath.indexOf(projectNamePrefix + File.separator + this.projectName);
			String projectFilePath = sourceFilePath.substring(firstIndex + projectNamePrefix.length());
			String targetFilePath = getTargetCopyPathTextVal() + projectFilePath;
			pathList.add(projectFilePath);
			try {
				File sourceFile = new File(sourceFilePath);
				File targetFile = new File(targetFilePath);
				message.setText(targetFilePath + "正在复制...");
				if (sourceFile.isDirectory()) {
					FileUtils.copyDirectory(sourceFile, targetFile);
				} else if (sourceFile.isFile()) {
					FileUtils.copyFile(sourceFile, targetFile);
				}

				message.setText(targetFilePath + "复制成功!");

			} catch (IOException e) {
				System.out.println("FileUtils.copyDirectory(new File(sourceFilePath),new File(targetFilePath));");
				e.printStackTrace();
			}
		}
	}

	public int getPackageName() {
		String info = packageNameField.getText();
		return Integer.parseInt(info);
	}

	public void showMessage(String message) {
		this.message.setText(message);
		// 设置文字颜色
		this.message.setForeground(Color.GREEN);
		/**
		 * 窗口抖动
		 */
		final Timer timer = new Timer();
		// 获取当前窗口的坐标,用一个Point实例保存,其中有x和y
		final Point start = this.getLocation();
		// 给Timer添加一个任务,用于周期性改变窗口坐标,模拟晃动效果
		timer.schedule(new TimerTask() {
			int[] offset = { -1, -2, -1, 0, 1, 2, 1, 0 };
			int i = 0;

			public void run() {
				Point p = getLocation();// 获取当前窗口坐标
				p.x += offset[i++ % offset.length];
				p.y += offset[i++ % offset.length];
				setLocation(p);
			}
		}, 0, 5);
		// 给Timer添加一个任务,用于在若干秒后停止Timer,停止晃动效果
		timer.schedule(new TimerTask() {
			public void run() {
				setLocation(start);// 停止前还原窗口坐标
				timer.cancel();// 停止Timer
			}
		}, 300);

	}

	public void showError(String message) {
		this.message.setText(message);
		this.message.setForeground(Color.RED);
		final Timer timer = new Timer();
		// 获取当前窗口的坐标,用一个Point实例保存,其中有x和y
		final Point start = this.getLocation();
		// 给Timer添加一个任务,用于周期性改变窗口坐标,模拟晃动效果
		timer.schedule(new TimerTask() {
			int[] offset = { -1, -2, -1, 0, 1, 2, 1, 0 };
			int i = 0;

			public void run() {
				Point p = getLocation();// 获取当前窗口坐标
				p.x += offset[i++ % offset.length];
				setLocation(p);
			}
		}, 0, 10);
		// 给Timer添加一个任务,用于在若干秒后停止Timer,停止晃动效果
		timer.schedule(new TimerTask() {
			public void run() {
				setLocation(start);// 停止前还原窗口坐标
				timer.cancel();// 停止Timer
			}
		}, 500);

	}

	public String getProjectNamePrefix() {
		return projectNamePrefix;
	}

	public void setProjectNamePrefix(String projectNamePrefix) {
		this.projectNamePrefix = projectNamePrefix;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	public void setListModel(DefaultListModel<String> listModel) {
		this.listModel = listModel;
	}

	public JScrollPane getjListScrollPane() {
		return jListScrollPane;
	}

	public void setjListScrollPane(JScrollPane jListScrollPane) {
		this.jListScrollPane = jListScrollPane;
	}


	public List<String> getListFile() {
		return listFile;
	}

	public void setListFile(List<String> listFile) {
		this.listFile = listFile;
	}

	public String getCurrTimeStr() {
		return packageName;
	}

	public void setCurrTimeStr(String currTimeStr) {
		this.packageName = currTimeStr;
	}

	public JList<String> getFileJlist() {
		return fileJlist;
	}

	public void setFileJlist(JList<String> fileJlist) {
		this.fileJlist = fileJlist;
	}

	public JTextField getPackageNameField() {
		return packageNameField;
	}

	public void setPackageNameField(JTextField packageNameField) {
		this.packageNameField = packageNameField;
	}

	public JLabel getMessage() {
		return message;
	}

	public void setMessage(JLabel message) {
		this.message = message;
	}

	public JLabel getTargetCopyPath() {
		return targetCopyPath;
	}

	public void setTargetCopyPath(JLabel targetCopyPath) {
		this.targetCopyPath = targetCopyPath;
	}

	public String getDefaultTargetDir() {
		return defaultTargetDir;
	}

	public void setDefaultTargetDir(String defaultTargetDir) {
		this.defaultTargetDir = defaultTargetDir;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSelect) {
			/*
			 * 这是尤为重要的。因为JFileChooser默认的是选择文件，而需要选目录。 故要将DIRECTORIES_ONLY装入模型
			 * 另外，若选择文件，则无需此句
			 */
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int intRetVal = fc.showOpenDialog(this);
			if (intRetVal == JFileChooser.APPROVE_OPTION) {
				txtfile.setText(fc.getSelectedFile().getPath());
			}
		}
	}
}
