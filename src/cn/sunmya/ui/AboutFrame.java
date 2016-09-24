package cn.sunmya.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * 菜单窗口
 * 
 */
public class AboutFrame extends JFrame implements Runnable {
	final JTextArea textarea = new JTextArea();
	private JLabel message = new JLabel("By Sunmengya1991@126.com", JLabel.RIGHT);// 用于显示错误信息的label

	@Override
	public void run() {  
	}

	/**
	 * 构造方法
	 */
	public AboutFrame(String message) {
		init();// 初始化界面
		textarea.setText(message);
		this.setVisible(true);
	}

	/**
	 * 界面初始化方法
	 */
	private void init() {
		// 设置窗口标题
		this.setTitle("关于");
		// 设置窗口大小
		this.setSize(350, 320);
		// 居中
		this.setLocationRelativeTo(null);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("icon_net_96.png"));
		this.setIconImage(icon.getImage());
		this.setContentPane(createContentPane());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AboutFrame.this.setVisible(false);
				// System.exit(0);
			}
		});
	}

	// 创建主Panel
	private JPanel createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		// 为Panel加入边框 上 左 下 右

		panel.setBorder(new EmptyBorder(0, 0, 0, 0));

		textarea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textarea);
		panel.add(scrollPane, BorderLayout.CENTER);
		this.setResizable(false);
		return panel;
	}

	private JPanel createSouthPane() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(new EmptyBorder(8, 8, 8, 8));
		panel.add(message, BorderLayout.CENTER);

		return panel;
	}

}
