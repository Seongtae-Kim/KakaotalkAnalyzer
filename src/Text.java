import java.util.ArrayList;

public class Text {
	// 해당 파일의 언어: 영어, 한국어 지원
	private Model model;
	private boolean eng = false, kor = false;
	private String fileName;
	private ArrayList <Line> lineList;
	private ArrayList<Word> wordList;
	private Time createdTime, chatStart, chatEnd;

	private Time savedTime;

	Text(){
		wordList = new ArrayList<Word>();
		lineList = new ArrayList<Line>();
		createdTime = new Time();
		chatStart = new Time();
		chatEnd = new Time();
		
	}

	void printWordList() {
		int num = 0;
		for (Word w : wordList) {
			num++;
			System.out.println("token " + num + " : " + w.getContent());
		}
	}
	
	void setInWordList(Word w) {
		wordList.add(w);
	}
	

	Word getFromWordList(int i) {
		return wordList.get(i);
	}
	
	public ArrayList<Word> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<Word> wordList) {
		this.wordList = wordList;
	}
	
	void lineListPrint() {
		for(Line l : lineList) {
			System.out.println(l.getRaw().toString());
		}
	}
	
	void addLine(Line l) {	// 라인을 추가
		lineList.add(l);
		l.setBelongTo(this);
	}
	Line getLine(int i) {	// i번째 라인을 리턴
		return lineList.get(i);
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ArrayList<Line> getLineList() {
		return lineList;
	}

	public void setLineList(ArrayList<Line> lineList) {
		this.lineList = lineList;
	}
	
	public Time getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Time createdTime) {
		this.createdTime = createdTime;
	}

	public Time getChatStart() {
		return chatStart;
	}

	public void setChatStart(Time chatStart) {
		this.chatStart = chatStart;
	}

	public Time getChatEnd() {
		return chatEnd;
	}

	public void setChatEnd(Time chatEnd) {
		this.chatEnd = chatEnd;
	}


	public boolean isEng() {
		return eng;
	}

	public void setEng(boolean eng) {
		this.eng = eng;

	}

	public boolean isKor() {
		return kor;
	}

	public void setKor(boolean kor) {
		this.kor = kor;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Time getSavedTime() {
		return savedTime;
	}

	public void setSavedTime(Time savedTime) {
		this.savedTime = savedTime;
	}

	
}