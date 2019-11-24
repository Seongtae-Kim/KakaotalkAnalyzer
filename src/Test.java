
public class Test {
	public static void main(String[] args) {
		Rank r5 = new Rank("5위");
		r5.setFreq(332);
		Rank r4 = new Rank("4위");
		r4.setFreq(7322);
		Rank r3 = new Rank("3위");
		r3.setFreq(8729);
		Rank r2 = new Rank("2위");
		r2.setFreq(113292);
		Rank r1 = new Rank("1위");
		r1.setFreq(214527);
		System.out.println();
		r1.quickSortStart();
		System.out.println();
		
	}
}
