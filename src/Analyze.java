import java.util.ArrayList;

public class Analyze {

	Analyze(Model m) {
		ArrayList<Text> list = m.getList();
		ArrayList<Rank> processed = new ArrayList<Rank>();

		for (Text t : list) {
			for (int i = 0; i < t.getLineList().size(); i++) {
				Line l = t.getLine(i);
				if(l.isConv()) {
					for(int j = 0; j < l.getConvList().size(); j++) {
						
					}
				}
			}
		}
	}
}

class Rank{
	int rank, freq;
	String word;
	
	Rank(){
		
	}
	
	
	
}
