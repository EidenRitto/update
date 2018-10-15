
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * 提示小窗口基本类
 * 
 * @author zs614
 *
 */
public class BasicPromptDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	static boolean flag = false;
	private int dialogWidth = 300;
	private int dialogHeight = 150;
	Point origin = new Point();

	JPanel jplRoot = null;
	JLabel lblTitle;// 标题
	JLabel lblPrompt;// 提示

	private static BasicPromptDialog instance;

	public BasicPromptDialog(String title, String prompt) {
		if (instance != null) {
			instance.dispose();
		}
		instance = this;
		flag = false;

		jplRoot = new JPanel();
		jplRoot.setLayout(null);
		jplRoot.setBackground(new Color(236, 254, 253));
		jplRoot.setBorder(BorderFactory.createLineBorder(new Color(0, 158, 150)));
		lblTitle = new JLabel(title, JLabel.CENTER);
		lblTitle.setOpaque(true);
		lblTitle.setForeground(Color.white);
		lblTitle.setBackground(new Color(0, 158, 150));
		lblTitle.setFont(new Font("微软雅黑", Font.PLAIN, 20));

		lblPrompt = new JLabel("", JLabel.CENTER);
		lblPrompt.setSize(dialogWidth - 20, 0);
		lblPrompt.setFont(new Font("微软雅黑", Font.PLAIN, 16));

		lblTitle.addMouseListener(new MouseAdapter() {
			// 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
			public void mousePressed(MouseEvent e) {
				// 当鼠标按下的时候获得窗口当前的位置
				origin.x = e.getX();
				origin.y = e.getY();
			}
		});
		lblTitle.addMouseMotionListener(new MouseMotionAdapter() {
			// 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
			public void mouseDragged(MouseEvent e) {
				// 当鼠标拖动时获取窗口当前位置
				Point p = BasicPromptDialog.this.getLocation();
				// 设置窗口的位置
				// 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
				BasicPromptDialog.this.setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
			}
		});
		lblTitle.setBounds(0, 0, dialogWidth, 35);

		lblPrompt.setBounds(10, 35, dialogWidth - 20, 70);

		jplRoot.add(lblTitle);
		jplRoot.add(lblPrompt);

		this.setLabelText(lblPrompt, prompt);
		this.getContentPane().add(jplRoot);
		this.setModal(true);
		this.setUndecorated(true);
		this.setSize(dialogWidth, dialogHeight);
		this.setLocation(300,400);
		//this.setDialogLocation(com);
		this.setAlwaysOnTop(true); // 总在最前面
	}

	public void setLabelText(JLabel jLabel, String longString) {
		StringBuilder builder = new StringBuilder(
				"<html><div style='text-align:left;" + "text-indent:25px;width:" + jLabel.getWidth() / 4 * 3 + "px;'>");
		builder.append(longString);
		builder.append("</div></html>");
		jLabel.setText(builder.toString());
	}

	public void dispose() {
		super.dispose();
		flag = true;
	}

}
