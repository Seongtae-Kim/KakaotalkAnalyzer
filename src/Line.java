import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Line extends Text {
	private int lineNum = 1;
	// 원문은 raw, 구두점을 없앤 문장 전체는 refined
	// conv가 true일 경우엔 해당 라인의 시간은 time, 해당 라인의 발화자는 speaker, 해당 라인의 대화 본문은 con
	private String raw, refined, time, con;
	private Speaker speaker;
	private ArrayList<Word> convList;
	private Time lineTime;
	private Text belongTo;
	/*
	 * media = 사진이나 동영상인 경우 conv = 일반 대화의 경우 timeAlert = 대화가 아니라 대화방 날짜인 경우 alert =
	 * 공지사항인 경우
	 */
	private boolean media = false, conv = false, timeAlert = false, alert = false;

	// 라인을 생성시 텍스트를 지정해주면 해당 텍스트에도 자동으로 라인을 추가
	Line(String content, int lineNum, Text t) {
		convList = new ArrayList<Word>();
		this.setRaw(content);
		this.setLineNum(lineNum);
		this.setBelongTo(t);
		t.addLine(this);
	}

	Line() {
		convList = new ArrayList<Word>();
	}

	Line(String cont) {
		this.raw = cont;
		convList = new ArrayList<Word>();
	}

	void printConvList() {
		int num = 0;
		System.out.println("라인넘버: " + lineNum);
		for (Word w : convList) {
			num++;
			System.out.println("token " + num + " : " + w.getContent());
		}
	}

	void checkChatOrAlert() {
		if(this.getLineNum() == 12991 && this.getBelongTo().getModel().textComplete == 1) {
			System.out.println();
		}
		ArrayList<Integer> index = new ArrayList<Integer>();
		index = Model.matchChar(getRaw(), ":");
		tokenize();
		
		if(index.size() == 1) {
			setAlert(true);
			getBelongTo().getModel().setDateStart(true);
		}
		else if (index.get(1) >= 23) {
			setConv(true);
			getBelongTo().getModel().setConvStart(true);
		} else {
			setAlert(true);
			getBelongTo().getModel().setDateStart(true);
		}
	}

	void mainLineSeparator() {
		if (getBelongTo().isEng()) {
			// 첫번째 콤마 전까지는 월/일
			// 두번째 콤마 전까지는 연도+시간+오전/오후
			// :전까지는 화자의 이름
			// : 뒤는 대화 내용

			String r = getRaw();
			ArrayList<Integer> com = Model.matchChar(r, ",");
			ArrayList<Integer> col = Model.matchChar(r, ":");

			String mandD = r.substring(0, com.get(0)); // 월 일
			String yandH = r.substring(com.get(0) + 1, col.get(0)); // 연도+시
			String mandAP = r.substring(col.get(0) + 1, com.get(1)); // 분 + 오전/오후
			String speaker = r.substring(com.get(1) + 2, col.get(1) - 1); // 화자
			String conv = r.substring(col.get(1) + 2, r.length());

			String[] m = Time.engMonth;

			Time t = new Time();

			String[] monthDate = mandD.split("\\s+");
			String[] yearHour = yandH.split("\\s+");
			String[] minuteAMPM = mandAP.split("\\s+");

			for (String s : m) {
				if (s.equals(monthDate[0])) {
					t.setMonth(monthDate[0]);
				}
			}

			t.setDate(monthDate[1]);
			t.setYear(yearHour[1]);

			t.setHour(yearHour[2], minuteAMPM[1]);

			t.setMinute(minuteAMPM[0]);
			t.setFullDateAndTime();

			setLineTime(t);
			setSpeaker(new Speaker(speaker));
			setCon(conv);
			convTokenize();
			setLineTime(t);

			getBelongTo().getModel().setPrevConv(getLineNum() - 1);
			getBelongTo().getModel().setConvStart(true);

		} else if (getBelongTo().isKor() && !getBelongTo().getModel().isOldKakao()) {
			// 첫번째 . 전은 연도
			// 두번째 . 전은 월
			// 세번째 . 전은 일
			// 첫번째 , 전은 오전/오후 + 시간
			// : 전까지는 화자의 이름
			// : 뒤는 대화 내용
			String r = getRaw();
			ArrayList<Integer> com = Model.matchChar(r, ",");
			ArrayList<Integer> stop = Model.matchChar(r, "\\.");
			ArrayList<Integer> col = Model.matchChar(r, ":");

			String year = r.substring(0, stop.get(0));
			String month = r.substring(stop.get(0) + 1, stop.get(1)).replaceAll(" ", "");
			String date = r.substring(stop.get(1) + 1, stop.get(2)).replaceAll(" ", "");
			String temp = r.substring(stop.get(2) + 2, col.get(0));
			String[] tmp = temp.split("\\s+");
			String ampm = tmp[0];
			String hour = tmp[1];
			String minute = r.substring(col.get(0) + 1, com.get(0));
			String speaker = r.substring(com.get(0) + 1, col.get(1) - 1);
			String conv = r.substring(col.get(1) + 2, r.length());

			Time t = new Time();

			t.setYear(year);
			t.setMonth(month);
			t.setDate(date);
			t.setHour(hour, ampm);
			t.setMinute(minute);
			t.setFullDateAndTime();
			setSpeaker(new Speaker(speaker));
			setCon(conv);
			convTokenize();
			setLineTime(t);

			getBelongTo().getModel().setPrevConv(getLineNum() - 1);
			getBelongTo().getModel().setConvStart(true);
		}
		else if (getBelongTo().isKor() && getBelongTo().getModel().isOldKakao()) {
			String r = getRaw();
			ArrayList<Integer> y = Model.matchChar(r, "년");
			ArrayList<Integer> m = Model.matchChar(r, "월");
			ArrayList<Integer> d = Model.matchChar(r, "일");
			ArrayList<Integer> com = Model.matchChar(r, ",");
			ArrayList<Integer> col = Model.matchChar(r, ":");

			String year = r.substring(0, y.get(0));
			String month = r.substring(y.get(0) + 1, m.get(0)).replaceAll(" ", "");
			String date = r.substring(m.get(0) + 1, d.get(0)).replaceAll(" ", "");
			String temp = r.substring(d.get(0) + 2, col.get(0));
			String[] tmp = temp.split("\\s+");
			String ampm = tmp[0];
			String hour = tmp[1];
			String minute = r.substring(col.get(0) + 1, com.get(0));
			String speaker = r.substring(com.get(0) + 1, col.get(1) - 1);
			String conv = r.substring(col.get(1) + 2, r.length());

			Time t = new Time();

			t.setYear(year);
			t.setMonth(month);
			t.setDate(date);
			t.setHour(hour, ampm);
			t.setMinute(minute);
			t.setFullDateAndTime();
			setSpeaker(new Speaker(speaker));
			setCon(conv);
			convTokenize();
			setLineTime(t);

			getBelongTo().getModel().setPrevConv(getLineNum() - 1);
			getBelongTo().getModel().setConvStart(true);			
		}
	}

	void removePunctuation() {
		String[] words = this.getRaw().replaceAll("[^0-9a-zA-Z가-힣\\s]", "").split("\\s+");
		String modified = "";
		for (int i = 0; i < words.length; i++) {
			modified = modified.concat(words[i]);
			modified = modified.concat(" ");
		}

		setRefined(modified);
	}

	// 라인 전체를 토크나이즈 할 경우
	// tokenize를 할 경우 반드시 raw값이 있어야 함
	void tokenize() {
		String[] cSplit = raw.split("\\s+");
		if (cSplit == null && !raw.equals("")) {
			Word w = new Word();
			w.setContent(raw);
			super.setInWordList(w);
			getBelongTo().getModel().wordComplete++;
		} else {
			for (int i = 0; i < cSplit.length; i++) {
				// wordList 추가용
				Word w = new Word();
				w.setContent(cSplit[i]);
				super.setInWordList(w);
				getBelongTo().getModel().wordComplete++;
			}
		}
	}

	// 토큰화 된 라인을 라인 자체의 워드리스트에 저장하지 않고 이용하기 위한 토크나이즈 함수
	ArrayList<Word> tempTokenize() {
		ArrayList<Word> list = new ArrayList<Word>();
		removePunctuation();
		String[] cSplit = refined.split("\\s+");
		if (getLineNum() == 8132) {
			System.out.print("t");
		}
		if (cSplit.length == 0 && !getRaw().equals("")) {
			Word w = new Word();
			w.setContent(raw);
			list.add(w);
		} else {
			for (int i = 0; i < cSplit.length; i++) {
				// wordList 추가용
				Word w = new Word();
				w.setContent(cSplit[i]);
				list.add(w);
			}
		}
		return list;
	}

	// conv가 true라고 확인된 라인의 con 부분만의 구두점을 없애고 tokenize할 경우
	void convTokenize() {
		if (getCon().equals(null)) {
			System.out.println("대화내용이 없습니다.");
		} else if (getCon().equals("Emoticons") || getCon().equals("이모티콘")) {
			Word w = new Word();
			w.setContent(getCon());
			w.setEmoji(true);
			convList.add(w);

		} else if (getCon().equals("Photo") || getCon().equals("사진")) {
			Word w = new Word();
			w.setContent(getCon());
			w.setMedia(true);
			convList.add(w);

		} else if (!Model.matchCharWhole(getCon(), "\\s+")) { // 즉 공백 없는 짧은 문장이라면
			Word w = new Word();
			w.setContent(getCon());
			convList.add(w);

		} else {
			String conv = getCon();
			String[] tokens = conv.replaceAll("[^0-9a-zA-Z가-힣\\s]", "").split("\\s+");
			String modified = "";
			for (int i = 0; i < tokens.length; i++) {
				Word w = new Word();
				modified = modified.concat(tokens[i]);
				modified = modified.concat(" ");
				w.setContent(tokens[i]);
				convList.add(w);

			}
			setCon(modified);
		}
	}

	@Override
	public String toString() {
		return raw;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;

	}

	public Time getLineTime() {
		return lineTime;
	}

	public void setLineTime(Time lineTime) {
		this.lineTime = lineTime;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public boolean isConv() {
		return conv;
	}

	public void setConv(boolean conv) {
		this.conv = conv;
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	public boolean isTimeAlert() {
		return timeAlert;
	}

	public void setTimeAlert(boolean timeAlert) {
		this.timeAlert = timeAlert;
	}

	public boolean isMedia() {
		return media;
	}

	public void setMedia(boolean media) {
		this.media = media;
	}

	public String getRefined() {
		return refined;
	}

	public void setRefined(String refined) {
		this.refined = refined;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public Text getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(Text belongTo) {
		this.belongTo = belongTo;
	}

	public void setInConvList(Word w) {
		convList.add(w);
	}

	public ArrayList<Word> getConvList() {
		return convList;
	}

	public void setConvList(ArrayList<Word> convList) {
		this.convList = convList;
	}

}
