import java.util.ArrayList;

public class Time {
	private String fullDateAndTime, fullDate, year, month, date, day, time, hour, minute, second, season;
	public static String[] korDay = { "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일" };
	public static String[] engDay = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	public static String[] engMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	public String[] getKorDay() {
		return korDay;
	}
	public String[] getEngDay() {
		return engDay;
	}
	Time(){
		
	}
	Time(String year, String month, String date, String day, String hour, String minute) {
		fullDateAndTime = year + ". " + month + ". " + date + ". (" + day + ") : " + hour + ". " + minute;
		time = hour + ": " + minute;
		
	}
	
	static Line timeParseKor(Line l) {
		ArrayList <Integer> a = new ArrayList<Integer>();
		
		String year, month, date, day;
		
		year = l.getWordList().get(0).getContent().substring(0, 4);
		
		String temp = l.getWordList().get(1).getContent();
		String pattern = "\\D";
		a = Model.matchChar(temp, pattern);
		month = temp.substring(0, a.get(0));
		
		temp = l.getWordList().get(2).getContent();
		a.clear();
		a = Model.matchChar(temp, pattern);
		date = temp.substring(0, a.get(0));
		
		day = l.getWordList().get(3).getContent();
		
		Time t = new Time();
		t.setYear(year);
		t.setMonth(month);
		t.setDate(date);
		t.setDay(day);
		
		l.setLineTime(t);
		
		return l;
	}
	
	static Line timeParseEng(Line l) {
		return l;
	}
	
	
	


	static void savedTimeparseKor(Line l, Text t) {	// 한국어버전 파일 저장 날짜 파싱
		// 저장한 날짜 : 2019. 11. 10. 오전 8:20

		String month = l.getWordList().get(4).getContent().substring(0, 2);
		String date = l.getWordList().get(5).getContent().substring(0, 2); // 쉼표 삭제
		String year = l.getWordList().get(3).getContent().substring(0, 4);
		String time = l.getWordList().get(7).getContent();
		String hour = time.substring(0, 1);
		String minute = time.substring(2, 4);
		String ampm = l.getWordList().get(6).getContent();
		String fullDate = year + "." + month + "." + date;
		String fullDateAndTime = year + "." + month + "." + date + " " + hour + ":" + minute;
		
		

		if (ampm.equals("오후")) {
			int pmHour = Integer.valueOf(hour) + 12;
			hour = String.valueOf(pmHour);

			String temp = time.substring(1, 4);
			time = hour + temp;
		}

		Time fullTime = new Time();

		fullTime.setYear(year);
		fullTime.setMonth(month);
		fullTime.setDate(date);
		fullTime.setTime(time);
		fullTime.setHour(hour);
		fullTime.setMinute(minute);
		fullTime.setFullDateAndTime();
		fullTime.setFullDate();

		t.setCreatedTime(fullTime);

	}

	static void savedTimeparseEng(Line l, Text text) { // 영어용 저장한 날짜 파싱

		String month = l.getWordList().get(2).getContent();
		String date = l.getWordList().get(3).getContent(); // 쉼표 삭제
		String year = l.getWordList().get(4).getContent();
		String time = l.getWordList().get(5).getContent();
		String hour = time.substring(0, 1);
		String minute = time.substring(1, 3);
		String ampm = l.getWordList().get(6).getContent();

		if (ampm.equals("PM")) {
			int pmHour = Integer.valueOf(hour) + 12;
			hour = String.valueOf(pmHour);
		}

		Time t = new Time();

		t.setYear(year);
		t.setMonth(month);
		t.setDate(date);
		t.setHour(hour);
		t.setMinute(minute);
		t.setFullDateAndTime();

		text.setCreatedTime(t);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String m) {

		int monthInt = Integer.valueOf(m);	// 월이 정수가 아닐 경우 대비
		
		// 계절은 spring, summer, fall, winter, error로 나뉨
		switch(monthInt) {
		case 1:
			this.season = "winter";
			break;
		case 2:
			this.season = "winter";
			break;
		case 3:
			this.season = "spring";
			break;
		case 4:
			this.season = "spring";
			break;
		case 5:
			this.season = "spring";
			break;
		case 6:
			this.season = "summer";
			break;
		case 7:
			this.season = "summer";
			break;
		case 8:
			this.season = "summer";
			break;
		case 9:
			this.season = "fall";
			break;
		case 10:
			this.season = "fall";
			break;
		case 11:
			this.season = "fall";
			break;
		case 12:
			this.season = "winter";
			break;
		default:
			this.season = "error";
		}
		
	}
	public String getFullDateAndTime() {
		return fullDateAndTime;
	}

	public void setFullDateAndTime() {
		setFullDate();
		this.fullDateAndTime = "[ " + year + ". " + month + ". " + date + ". (" + day + ")" + " " + hour + ":" + minute +  " ]";
	}

	public String getFullDate() {
		return fullDate;
	}

	public void setFullDate() {
		this.fullDate = "[ " + year + ". " + month + ". " + date + ". (" + day + ")" + " ]";
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}
	public void setHour(String hour, String AMPM) {
		if(AMPM.equals("PM") || AMPM.equals("오후")) {
			int h = Integer.valueOf(hour);
			h = h + 12;
			this.hour = String.valueOf(h);
		}else {
			this.hour = hour;	
		}
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}
}
