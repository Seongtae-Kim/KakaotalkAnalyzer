import java.util.ArrayList;

public class Function {
	ArrayList<Speaker> chineseSpeaker = new ArrayList<Speaker>();
	ArrayList<Line> chineseEvidence = new ArrayList<Line>();
	static boolean found =false;

	static void chineseDetector(Model m) {
		String[] china = { "중국", "중국이", "중국은", "중국을", "중국에", "중국의", "중국에서", "중국인", "중국몽", "중국도", "중국으로", "중국발", "중국이랑",
				"중국산", "중국과", "중국한테", "중국이다", "중국어", "중국인들" };
		
		for (int i = 0; i < m.getList().size(); i++) {
			ArrayList<Line> lineList = m.getList().get(i).getLineList();
			for (int j = 0; j < lineList.size(); j++) {
				ArrayList<Word> wordList = lineList.get(j).getConvList();
				Line l = lineList.get(j);
				for (int k = 0; k < wordList.size(); k++) {
					for (String s : china) {
						if (wordList.get(k).getContent().equals(s)) {

						}
					}

				}
			}
		}

	}

}
