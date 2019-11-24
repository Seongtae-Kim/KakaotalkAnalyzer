
public class QuickSort_differentMethod {
static int i, j, pivot, temp, result, size;
	
	
	public static int partition(int[] a, int begin, int end) {
		pivot = a[end];		// 마지막 값을 pivot으로 선언
		i = begin-1;

		// pivot은 list의 제일 끝에두고 pivot보다 작은값과 큰값을 모아두고
		//pivot값보다 작은 값의 마지막 index의 다음 index(pivot보다 큰 값을 가진 제일 첫 index)의 값과 pivot index의 값을 맞바꿈 
		for(j = begin; j <= end-1; j++) {	// pivot의 바로 앞 index까지 탐색
			if(a[j] >= pivot) {	// 지금 다루고 있는 값이 pivot 값 보다 크거나 같다면
							// 올바른 자리에 위치하고 있으므로 다음 인덱스로 이동(j++)
			}
			else {				// 지금 다루고 있는 값이 pivot 값보다 작은 값이라면
				i++;			// pivot보다 큰 값들의 앞에 위치해야 하므로 index를 옮김
				temp = a[i];	// pivot보다 큰 숫자를 저장
				a[i] = a[j];	// 작은 숫자를 해당 index로 이동
				a[j] = temp;	// pivot보다 큰 숫자를 뒤로 옮김
			}
		}
		// 모든 정렬이 끝났으므로, pivot값과 pivot값보다 큰 값이 모여있는 부분의 제일 처음 index를 서로 맞바꿈
		i++;
		temp = a[i];
		a[i] = a[end];
		a[end] = temp; 
		
		return i;			// pivot index를 return
	}
	
	public static void QuickSort(int[] a, int begin, int end) {
		if(begin < end) {
			int p = partition(a, begin, end);
			QuickSort(a, begin, p-1);
			QuickSort(a, p+1, end);
			
			
		}
	}
	public static void main(String[]args) {
		int[] list = {69, 10, 30, 2, 16, 8, 31, 22};		// 69가 L이자 begin 값, 22가 R이자 end 값
		size = list.length;
		QuickSort(list, 0, size-1);
		printArray(list);
	}
	public static void printArray(int arr[]) {
		for(int i=0; i<arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
}
