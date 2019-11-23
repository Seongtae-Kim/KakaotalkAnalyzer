import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		String line = "all work and no play makes jack dull boy";
		String kor = "2018년 2월 19일 월요일";
		String pattern = "\\s";

		boolean english = false;
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(line);

		String[] list = new String[2];

		list[1] = line;
		list[0] = kor;

		String eng = "[a-zA-Z]";
		Pattern en = Pattern.compile(eng);
		for (String a : list) {
			Matcher lang = en.matcher(a);

			if (lang.find()) {
				english = true;
				System.out.println(english);
			} else {
				english = false;
				System.out.println(english);
			}
		}

		if (m.find()) {
			line = m.replaceAll("");
		} else {
			System.out.println("No result");
		}

		System.out.println(line);

		System.out.println();

		String shining = "all work and no play makes jack dull boy";



		Text t = new Text();
		Line l = new Line(shining);
		Line korL = new Line(kor);

		t.addLine(l);
		t.addLine(korL);

		t.lineListPrint();

		korL.tokenize();
		korL.printWordList();
		System.out.println();

		Line koreaTimeEx = Time.timeParseKor(korL);

		System.out.println();
		System.out.println("날짜 출력");
		Time korTime = koreaTimeEx.getLineTime();
		System.out.println("연도 : " + korTime.getYear());
		System.out.println("월 : " + korTime.getMonth());
		System.out.println("일 : " + korTime.getDate());
		System.out.println("요일 : " + korTime.getDay());

		korL.getBelongTo().setKor(true);

		String engDate = "Monday, February 19, 2018";
		Line engLine = new Line();
		engLine.setRaw(engDate);
		t.addLine(engLine);
		engLine.tokenize();

		System.out.println();
		System.out.println(korL.getWordList().toString());
		System.out.println(korL.getRaw());
		System.out.println("시간 라인인가? " + korL.isTimeAlert());

		t.setEng(true);
		
		System.out.println();
		System.out.println(engLine.getWordList().toString());
		System.out.println(engLine.getRaw());
		System.out.println("시간 라인인가? " + engLine.isTimeAlert());

		String puncTest = "내가 알고있는, 모든 것이 사라질 때쯤이면! (너도 알고 있겠지만...) 2/3정도는 다 마음이 바뀌어 있을 것이 팩-트";
		
		String convEng = "Feb 19, 2018 10:17 AM, 김성태 (Seongtae Kim) : 피할 수 없는 통살의 유혹...";
		String alrtEng = "Feb 19, 2018 10:17 AM: 김성태 (Seongtae Kim) invited Samson, 전두엽 and 식윤우 로대반.";
		
		String convKor = "2018. 12. 9. 오후 9:05, 김성태 (Seongtae Kim) : 피할 수 없는 통살의 유혹...";
		String alrtKor = "2018. 2. 19. 오전 10:17: 김성태 (Seongtae Kim)님이 식윤우 로대반님, 전두엽님과 Samson님을 초대했습니다.";

		Line n3 = new Line();

		n3.setRaw(puncTest);
		n3.tokenize();

		System.out.println(n3.getRaw());
		System.out.println(n3.getRefined());
		n3.printWordList();

		Text caTest = new Text();
		Line n4 = new Line(convEng);
		Line n5 = new Line(alrtEng);
		
		n4.setBelongTo(caTest);
		n5.setBelongTo(caTest);
		n4.getBelongTo().setEng(true);
		

		n4.checkChatOrAlert();
		n5.checkChatOrAlert();
		
		
		System.out.println("테스트!");
		n4.mainLineSeparator();
		
		Line n6 = new Line(alrtKor);
		Line n7 = new Line(convKor);
		Text caTest1 = new Text();
		n6.setBelongTo(caTest1);
		n7.setBelongTo(caTest1);
		n6.getBelongTo().setKor(true);
		
		
		n7.mainLineSeparator();
		
		
		n6.checkChatOrAlert();
		n7.checkChatOrAlert();
		
		System.out.println(n6.isConv());
		System.out.println(n7.isConv());
		
		
		
		
	}
}
