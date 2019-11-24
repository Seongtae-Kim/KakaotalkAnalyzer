import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		QuickSort q = new QuickSort();
		int [] test = {213, 12, 2, 434, 82, 72, 12, 66};
		q.sort(test, 0, test.length);
		
		System.out.println(test.toString());
	}
}
