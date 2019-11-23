import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Model { // 파일 각각 모델생성
	Model model; // 모든 파일 정보를 저장하는 모델
	String createdDate; // 모델이 생성된 날짜&시간
	ArrayList<Text> list; // 파일 리스트
	private boolean convStart = false, dateStart = false;
	private int prevConv = 0;
	public int wordComplete = 0;
	public int lineComplete = 0;
	public int textComplete = 0;
	TextField tf = new TextField();
	
	Model(File[] f) { // 모델 생성
		ArrayList<File> fileList = new ArrayList<File>();
		tf.setEntireNum(f.length);	// Progress Bar에 넘기기 위한 전체 텍스트 수
		
		for (File file : f) { // ArrayList로 변환
			fileList.add(file);
		}
		readTextList(fileList);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setCreatedDate(t.toString());
	}

	void readTextList(ArrayList<File> a) {

		try {
			list = new ArrayList<Text>();
			for (File file : a) {
				Text t = new Text();
				ArrayList<Line> lineList = new ArrayList<Line>();
				Line l = new Line();
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				int lineNum = 0;
				t.setModel(this);

				while ((line = br.readLine()) != null) {
					l = readLine(line, ++lineNum, t);
					lineList.add(l);
					lineComplete++;
				}
				
				t.setLineList(lineList);
				list.add(t);

				System.out.println("파일 " + t.getFileName() + "추가 완료");
				textComplete++;
			}
			
			System.out.println();
			System.out.println("텍스트 " + textComplete + "개");
			System.out.println("라인 " + lineComplete + "개");
			System.out.println("단어" + wordComplete + "개 처리 완료");
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file");
		} catch (IOException ex) {
			System.out.println("Error reading file");
		}
		
		

	}

	// readTextList에서 건너옴
	Line readLine(String content, int lineNum, Text t) {
		Line line = new Line(content, lineNum, t);

		if (line.getLineNum() == 1) { // 첫번째 줄에는 무조건 파일의 이름
			firstLineAnalyze(line);
		} else if (line.getLineNum() == 2) { // 2번째 줄에서 언어, 저장 날짜 생성
			secondLineAnalyze(line);

		} else { // 3번째 줄 이후로 본격적인 분석
			lineAnalyze(line);
		}

		return line;
	}

	// readLine에서 건너서 옴
	void lineAnalyze(Line line) {
		ArrayList<Word> test = line.tempTokenize();
		String[] engMonth = Time.engMonth; // 영어일 경우 월 이름과 일치하면 대화(conv)이거나 대화방 공지(alert)사항
		String[] engDay = Time.engDay; // 영어일 경우 요일 이름과 일치하면 대화방 날짜 타임
		String[] korDay = Time.korDay;
		boolean exception = true;

		if (line.getLineNum() == 6 && textComplete == 1) {
			System.out.println();
		}

		if (line.getBelongTo().isEng()) {
			for (String s : engMonth) {
				if (line.getRaw().equals("")) {
					break;
				}
				if (test.get(0).getContent().equals(s)) {
					exception = false;
					line.checkChatOrAlert();
					if (line.isAlert()) { // 채팅방 공지사항일 경우

					} else if (line.isConv()) { // 실제 채팅의 경우
						line.mainLineSeparator();
					}
					break;
				} // 대화 혹은 대화방 공지일 경우
			}
			if (exception) {
				for (String s : engDay) {
					if (line.getRaw().equals("")) {
						break;
					}
					if (test.get(0).getContent().equals(s)) {
						line.setTimeAlert(true);
						exception = false;
						break;
					} // 대화방 타임일 경우
				}
			}
			// 텍스트 파일의 라인피드를 위한 띄어쓰기이거나, 이전의 대화와 이어지는 라인피드이다.
			if (exception) {
				if (convStart) { // 만약 채팅이 이어져 나갔던 거라면
					for (Word w : test) {
						line.getBelongTo().getLine(prevConv).setInWordList(w);
						line.getBelongTo().getLine(prevConv).setInConvList(w);
						wordComplete++;
						// 바로 전 라인에 이어서 토큰 저장
					}

				} else {
					// 아니라면 텍스트 파일 맨 앞일것으로 예상
				}
			}
			if (!exception) { // 라인피드가 아니라면

				if (!line.isTimeAlert()) { // 채팅방 시간이 아니면 메인 분석 단계 돌입

				} else {
					// 채팅방 시간 처리

					if (!dateStart) { // 앞에서 대화방 타임이 없었다면
						dateStart = true; // 새로운 대화방 타임을 발견한 것이므로 타임 시작
					} else { // 앞에서 대화방 타임을 이미 발견했다면
						dateStart = false; // 여기까지가 대화방 타임
					}
					if (convStart) { // 하루의 마지막 대화를 끝으로 대화 타임의 끝을 만났을때
						convStart = false; // 다시 대화를 끝냄
					}
				}
			}
			exception = true;
			System.out.println("텍스트" + (textComplete + 1) + ": 라인" + line.getLineNum() + "분석완료");
		}

		if (line.getBelongTo().isKor()) {

			if (!line.getRaw().equals("") && test.size() >= 4) {

				for (String s : korDay) {
					if (test.get(3).getContent().equals(s)) { // 대화방 타임
						line.setTimeAlert(true);
						exception = false;
					}
				}
				if (exception && !line.getRaw().equals("")
						&& matchCharWhole(test.get(0).getContent(), "\\d\\d\\d\\d")) {
					if (test.get(3).getContent().equals("오전") || test.get(3).getContent().equals("오후")) {
						line.checkChatOrAlert();
						if (line.isAlert()) { // 채팅방 공지사항일 경우

						} else if (line.isConv()) { // 실제 채팅의 경우
							line.mainLineSeparator();
						}
						exception = false;
					}
				}
			}
			// 텍스트 파일의 라인피드를 위한 띄어쓰기이거나, 이전의 대화와 이어지는 라인피드이다.
			if (exception) {
				if (convStart) { // 만약 채팅이 이어져 나갔던 거라면
					for (Word w : test) {
						line.getBelongTo().getLine(prevConv).setInWordList(w);
						line.getBelongTo().getLine(prevConv).setInConvList(w);
						wordComplete++;
						// 바로 전 라인에 이어서 토큰 저장
					}

				} else {
					// 아니라면 텍스트 파일 맨 앞일것으로 예상
				}
			}
			if (!exception) { // 라인피드가 아니라면

				if (!line.isTimeAlert()) { // 채팅방 시간이 아니면 메인 분석 단계 돌입

				} else {
					// 채팅방 시간 처리

					if (!dateStart) { // 앞에서 대화방 타임이 없었다면
						dateStart = true; // 새로운 대화방 타임을 발견한 것이므로 타임 시작
					} else { // 앞에서 대화방 타임을 이미 발견했다면
						dateStart = false; // 여기까지가 대화방 타임
					}
					if (convStart) { // 하루의 마지막 대화를 끝으로 대화 타임의 끝을 만났을때
						convStart = false; // 다시 대화를 끝냄
					}
				}
			}
			exception = true;
			System.out.println("텍스트" + (textComplete + 1) + ": 라인" + line.getLineNum() + "분석완료");
			String complete = "텍스트" + (textComplete + 1) + ": 라인" + line.getLineNum() + "분석완료";
			
			tf.addProgress(complete);
			
		}

	}

	void firstLineAnalyze(Line line) {
		// list.add(line.getRaw());
		line.getBelongTo().setFileName(line.getRaw());
	}

	void secondLineAnalyze(Line line) {
		// 언어 판별
		String eng = "[a-zA-Z]";
		Pattern e = Pattern.compile(eng);
		Matcher lang = e.matcher(line.getRaw());
		Text t = line.getBelongTo();

		if (lang.find())
			t.setEng(true);
		else
			t.setKor(true);

		line.tokenize();
		// 날짜 설정
		if (t.isEng()) {
			Time.savedTimeparseEng(line, t);
		} else if (t.isKor()) {
			Time.savedTimeparseKor(line, t);
		}
		line.tokenize();
	}

	// 전체 토큰 내에 해당 패턴이 존재하는지 그 여부를 리턴
	static boolean matchCharWhole(String token, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(token);
		boolean match = m.find();

		return match;
	}

	// 토큰의 문자 하나하나를 들여다보고 해당 패턴의 인덱스들을 반환
	// 없으면 null 상태의 ArrayList 반환
	static ArrayList<Integer> matchChar(String token, String pattern) {
		int len = token.length();
		ArrayList<Integer> index = new ArrayList<Integer>();
		String cursor = "";

		for (int i = 0; i < len; i++) {
			cursor = token.substring(i, i + 1);
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(cursor);
			if (m.find()) {
				index.add(i);
			}

		}
		return index;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public ArrayList<Text> getList() {
		return list;
	}

	public void setList(ArrayList<Text> list) {
		this.list = list;
	}

	public boolean isConvStart() {
		return convStart;
	}

	public void setConvStart(boolean convStart) {
		this.convStart = convStart;
	}

	public boolean isDateStart() {
		return dateStart;
	}

	public void setDateStart(boolean dateStart) {
		this.dateStart = dateStart;
	}

	public int getPrevConv() {
		return prevConv;
	}

	public void setPrevConv(int prevConv) {
		this.prevConv = prevConv;
	}
}
