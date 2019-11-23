import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

public class Window extends JFrame {
	Color kakaoBackground = new Color(191, 209, 221);
	Color kakaoChatBubble = new Color(254, 240, 27);
	File[] textList;

	Window(JPanel bd) {
		setSize(800, 830);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		this.getContentPane().setBackground(kakaoBackground);

		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu help = new JMenu("Help");

		Font buttonFont = new Font("HY견고딕", Font.PLAIN, 13);
		Font tailFontBold = new Font("HY헤드라인M", Font.BOLD, 15);

		JTextField searchField = new JTextField(10);
		JButton search = new JButton("찾기");
		JButton analyze = new JButton("분석 모델 생성!");
		JLabel readFirst = new JLabel("파일을 먼저 읽어들이세요!");

		readFirst.setFont(buttonFont);

		analyze.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (textList == null)
					JOptionPane.showMessageDialog(bd, readFirst);
				else {
					Model m = new Model(textList);
					if (m != null)
						JOptionPane.showMessageDialog(bd,
								"성공적으로 분석 모델을 생성하였습니다.\n 분석한 텍스트: " + String.valueOf(m.textComplete) + "\n 분석한 라인: "
										+ String.valueOf(m.lineComplete) + "\n 분석한 단어:"
										+ String.valueOf(m.wordComplete));
				}

			}

		});

		JButton fileList = new JButton("파일 목록");
		fileList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (textList == null)
					JOptionPane.showMessageDialog(bd, readFirst);
				else {
					System.out.println("textList있음");
					FileList f = new FileList(textList);
				}
			}

		});

		ImageIcon img = new ImageIcon("./Title.png", "title");
		ImageIcon cont = new ImageIcon("./Content.png", "content");

		JPanel head = new JPanel();
		JPanel tail = new JPanel(new BorderLayout());
		JPanel bgPnl = new JPanel() { // 이미지파일을 배경으로 넣기 위한 패널
			public void paintComponent(Graphics g) {
				g.drawImage(cont.getImage(), 10, 0, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};

		JPanel searchPnl = new JPanel();
		JPanel uploadPnl = new JPanel();

		JLabel imgLabel = new JLabel(img, JLabel.CENTER);
		// JLabel contLabel = new JLabel(cont, JLabel.CENTER);

		JLabel currentNote = new JLabel("현재 불러온 대화 파일: ");
		JLabel current = new JLabel("불러온 파일 없음"); // 파일이 하나일 경우 파일 이름 하나만. 두 개 이상일 경우 첫번째 파일 외 몇 개의 파일인지
		JButton upload = new JButton("업로드");

		currentNote.setFont(tailFontBold);
		current.setFont(tailFontBold);
		upload.setFont(buttonFont);

		JPanel uploadBox = new JPanel();
		uploadBox.add(currentNote);
		uploadBox.add(current);
		uploadBox.add(upload);
		uploadBox.add(fileList);
		uploadBox.add(analyze);

		upload.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (textList == null)
					textList = UploadTxt(uploadBox);
				else {
					Arrays.fill(textList, null); // 배열 비우기
					textList = UploadTxt(uploadBox);
				}

				for (int i = 0; i < textList.length; i++) {
					if (textList[i].canRead()) {
						// pass
					} else {
						JOptionPane.showMessageDialog(bd, "올바른 파일이 아닙니다.");
						break;
					}
				}
				String name = textShorten(textList[0].getName());
				if (textList.length >= 2) {
					JOptionPane.showMessageDialog(bd, "파일 " + name + " 외 " + (textList.length - 1) + "건의 자료를 불러왔습니다.");
					current.setText(name + "외 " + (textList.length - 1) + "건");

				} else {
					JOptionPane.showMessageDialog(bd, "파일 " + name + "을 불러왔습니다.");
					current.setText(name);
				}
			}

		});

		menu.add(file);
		menu.add(help);
		setJMenuBar(menu);

		bgPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		bd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		head.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tail.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		searchPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		bd.setBackground(Color.WHITE);
		bgPnl.setBackground(kakaoBackground);
		// searchPnl.setBackground(kakaoBackground);
		head.setBackground(kakaoBackground);
		tail.setBackground(kakaoBackground);
		searchPnl.setBackground(kakaoBackground);
		uploadPnl.setBackground(kakaoBackground);
		uploadBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		uploadBox.setBackground(kakaoChatBubble);

		add(bgPnl, BorderLayout.CENTER);
		bgPnl.add(bd);
		add(head, BorderLayout.NORTH);
		add(tail, BorderLayout.SOUTH);
		// bd.add(contLabel);
		head.add(imgLabel);
		tail.setLayout(new GridLayout(2, 1));
		tail.add(searchPnl);
		tail.add(uploadPnl);
		searchPnl.add(searchField);
		searchPnl.add(search);
		uploadPnl.setLayout(new BorderLayout());
		uploadPnl.add(uploadBox, BorderLayout.EAST);

		setVisible(true);

	}

	File[] UploadTxt(JPanel parent) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] selected = fileChooser.getSelectedFiles();
			return selected;
		}
		return null;

	}

	static String textShorten(String txt) { // 글자가 25자 이상인 경우 ... 으로 생략
		if (txt.length() >= 28)
			return txt.substring(0, 5) + "..." + txt.substring(txt.length() - 10, txt.length());
		else
			return txt;
	}
}

class TextField extends JTextField {
	int maxTxt;
	JScrollBar scBar;

	TextField() {
		scBar = new JScrollBar(JScrollBar.VERTICAL);
		BoundedRangeModel brm = getHorizontalVisibility();
		scBar.setModel(brm);
		add(scBar);
		setText("작업 시작...");
		setEditable(false);
		setVisible(true);
	}

	void setEntireNum(int num) {
		maxTxt = num;
	}

	void addProgress(String txt) {
		setText(getText() + txt + "..." + "\n");
	}
}


class Progress extends JFrame {
	JProgressBar progressBar;
	TextField tField;

	Progress() {
		setSize(500, 300);
		setResizable(false);
		setLayout(new BorderLayout());

		tField = new TextField();

		progressBar = new JProgressBar();

		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		add(progressBar, BorderLayout.SOUTH);
		add(tField, BorderLayout.CENTER);

		setVisible(true);
	}
}

class FileList extends JFrame {
	FileList(File[] e) {
		setSize(300, 500);
		setResizable(false);

		JPanel controlPnl = new JPanel();
		JPanel listMain = new JPanel();
		listMain.setLayout(new BorderLayout());

		String[] columnName = { "파일 이름" };
		String[][] list = new String[e.length][columnName.length];
		for (int i = 0; i < e.length; i++) {
			list[i][0] = e[i].getName();

		}
		JTable fileList = new JTable(list, columnName);
		fileList.setEnabled(false);

		JButton close = new JButton("닫기");
		close.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				dispose();
			}
		});

		add(listMain, BorderLayout.CENTER);
		listMain.add(fileList);
		listMain.add(new JScrollPane(fileList));
		add(controlPnl, BorderLayout.SOUTH);
		controlPnl.add(close);

		setVisible(true);

	}
}

class MainPg extends JPanel {
	MainPg() {

	}

}