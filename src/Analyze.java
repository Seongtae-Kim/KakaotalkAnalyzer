import java.util.ArrayList;

public class Analyze {
	Rank r;
	ArrayList<Word> wl;
	ArrayList<Rank> wordRankList;
	
	Analyze(Model m) {
		ArrayList<Text> list = m.getList();
		
		for (Text t : list) { // text별
			for (int i = 0; i < t.getLineList().size(); i++) { // line별
				Line l = t.getLine(i);
				System.out.print(t.getFileName() + "/ " + l.getLineNum() + "번째 라인");
				if (l.isConv() && l.getConvList().size() != 0) {
					wl = l.getConvList();
					for (int j = 0; j < wl.size(); j++) { // word 별
						r = new Rank(wl.get(j).getContent());
						System.out.print("*");
					}
					System.out.println();
				} 
			}
			System.out.println(t.getFileName() + "완료");
		}
		r.quickSortStart();// 정렬 시작
		wordRankList = r.getProcessed();
		
	
	}

	public ArrayList<Rank> getWordRankList() {
		return wordRankList;
	}

	public void setWordRankList(ArrayList<Rank> wordRankList) {
		this.wordRankList = wordRankList;
	}
}

class Rank {
	int rank, freq;
	String word;
	static ArrayList<Rank> processed = new ArrayList<Rank>();
	static boolean found = false, ranked = false;
	
	Rank(){
		
	}
	Rank(String word) {
		investigate(word);
	}
	
	int partition(ArrayList<Rank> rl, int begin, int end) {
		int p, pivot, temp, l;
		String tempWord;
		
		p = (int) Math.floor(Math.random() * rl.size());	// 피봇은 랜덤으로 설정
		pivot = rl.get(p).getFreq();
		l = begin-1;
		
		System.out.println("퀵소팅중: 파티션: begin: " + begin + " end: " + end + " pivot: " + p);
		for(int j = begin; j < end; j++) {
			if(rl.get(j).getFreq() >= pivot) {
				
			}
			else {
				l++;
				temp = rl.get(l).getFreq();
				tempWord = rl.get(l).getWord();
				rl.get(l).setFreq(rl.get(j).getFreq());
				rl.get(l).setWord(rl.get(j).getWord());
				rl.get(j).setFreq(temp);
				rl.get(j).setWord(tempWord);
			}
		}
		
		l++;
		temp = rl.get(l).getFreq();
		tempWord = rl.get(l).getWord();
		rl.get(l).setFreq(rl.get(end).getFreq());
		rl.get(l).setWord(rl.get(end).getWord());
		rl.get(end).setFreq(temp);
		rl.get(end).setWord(tempWord);
		
		return l;

	}
	void quickSortStart() {
		System.out.println("퀵소팅 시작...");
		quickSort(processed, 0, processed.size()-1);
	}

	void quickSort(ArrayList<Rank> rl, int begin, int end) { // 순위 파악 및 순위별로 processed 재정렬
		if(begin < end) {
			int p = partition(rl, begin, end);
			quickSort(rl, begin, p-1);
			quickSort(rl, p+1, end);
		}
	}

	void investigate(String s) {
		if (processed.size() != 0) {
			for (int i = 0; i < processed.size(); i++) {
				String current = processed.get(i).getWord();
				if (s.equals(current)) {
					processed.get(i).freq++;
					found = true;
				}
			}
		}
		if (!found) {
			this.word = s;
			this.freq++;
			processed.add(this);
		}
		found = false;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public ArrayList<Rank> getProcessed() {
		return processed;
	}

	public void setProcessed(ArrayList<Rank> processed) {
		this.processed = processed;
	}

}

// 랭킹 정렬을 위해 가져온 퀵소트

//Java program for implementation of QuickSort 
class QuickSort {
	/*
	 * This function takes last element as pivot, places the pivot element at its
	 * correct position in sorted array, and places all smaller (smaller than pivot)
	 * to left of pivot and all greater elements to right of pivot
	 */
	int partition(int arr[], int low, int high) {
		int pivot = arr[high];
		int i = (low - 1); // index of smaller element
		for (int j = low; j < high; j++) {
			// If current element is smaller than the pivot
			if (arr[j] < pivot) {
				i++;

				// swap arr[i] and arr[j]
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		// swap arr[i+1] and arr[high] (or pivot)
		int temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;

		return i + 1;
	}

	/*
	 * The main function that implements QuickSort() arr[] --> Array to be sorted,
	 * low --> Starting index, high --> Ending index
	 */
	void sort(int arr[], int low, int high) {
		if (low < high) {
			/*
			 * pi is partitioning index, arr[pi] is now at right place
			 */
			int pi = partition(arr, low, high);

			// Recursively sort elements before
			// partition and after partition
			sort(arr, low, pi - 1);
			sort(arr, pi + 1, high);
		}
	}

	/* A utility function to print array of size n */
	static void printArray(int arr[]) {
		int n = arr.length;
		for (int i = 0; i < n; ++i)
			System.out.print(arr[i] + " ");
		System.out.println();
	}

	// Driver program
	public static void main(String args[]) {
		int arr[] = { 10, 7, 8, 9, 1, 5 };
		int n = arr.length;

		QuickSort ob = new QuickSort();
		ob.sort(arr, 0, n - 1);

		System.out.println("sorted array");
		printArray(arr);
	}
}

/* This code is contributed by Rajat Mishra */