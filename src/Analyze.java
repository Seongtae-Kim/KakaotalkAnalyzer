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
						if (!wl.get(j).isEmoji() && !wl.get(j).isMedia() && !wl.get(j).getContent().equals("")) {
							r = new Rank(wl.get(j).getContent());
							System.out.print("*");
						}

					}
					System.out.println();
				}
			}
			System.out.println(t.getFileName() + "완료");
		}
		r.quickSortStart();// 정렬 시작
		wordRankList = r.getProcessed();
		System.out.println();
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
	static int loading = 0;

	Rank() {

	}

	Rank(String word) {
		investigate(word);
	}

	void swap(ArrayList<Rank> rl, int r1, int r2) {
		int tempFreq;
		String tempWord;

		tempFreq = rl.get(r1).getFreq();
		tempWord = rl.get(r1).getWord();
		rl.get(r1).setFreq(rl.get(r2).getFreq());
		rl.get(r1).setWord(rl.get(r2).getWord());
		rl.get(r2).setFreq(tempFreq);
		rl.get(r2).setWord(tempWord);
	}

	int partition(ArrayList<Rank> rl, int begin, int end) {
		loading++;
		if (loading/100000 >= 1) {
			System.out.print("*");
		}
		if (loading == 10000000) {
			System.out.println();
			loading = 0;
		}
		
		int pivot, i;
		pivot = rl.get(end).getFreq();
		i = begin - 1;

		for (int j = begin; j < end; j++) {
			if (rl.get(j).getFreq() > pivot) {
				swap(rl, ++i, j);
			}
		}

		swap(rl, ++i, end);

		return i;

	}

	void quickSortStart() {
		System.out.println("퀵소팅 시작...");
		quickSort(processed, 0, processed.size() - 1);

		int r = 1, same = 0;
		for (int i = 0; i < processed.size(); i++) {
			if (i == 0)
				processed.get(i).setRank(r);
			else if (processed.get(i).getFreq() == processed.get(i - 1).getFreq()) {
				processed.get(i).setRank(r);
				same++;
			} else {
				if (same != 0) {
					r += same;
					same = 0;
				}
				r++;
				processed.get(i).setRank(r);
			}

		}
		System.out.println("퀵소팅 끝...");
	}

	void quickSort(ArrayList<Rank> rl, int begin, int end) { // 순위 파악 및 순위별로 processed 재정렬
		if (begin < end) {
			int p = partition(rl, begin, end);
			quickSort(rl, begin, p - 1);
			quickSort(rl, p + 1, end);
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