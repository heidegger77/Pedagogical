import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSortParallel extends RecursiveAction {
    public static void main(String... args) {
        int[] arr = new int[]{90,1,2,4,7,4,2,87};
        MergeSortParallel mergeSort = new MergeSortParallel(
                0,
                arr.length - 1,
                arr
        );
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mergeSort);
        StringJoiner sj = new StringJoiner(",");
        int[] sorted = mergeSort.result();
        int index = 0;
        for(int num: sorted) {
            sj.add(Integer.toString(num));
        }
        System.out.println(sj);
    }

    private int left;
    private int right;
    private int[] input;
    private int[] output;

    public MergeSortParallel(int left, int right, int[] input) {
        this.left = left;
        this.right = right;
        this.input = input;
    }

    int[] result() {
        return output;
    }

    @Override
    protected void compute() {
        if (left >= right) {
            computeDirect(left, right);
        } else {
            int mid = left + (right - left) / 2;
            MergeSortParallel task1 = new MergeSortParallel(left, mid, input);
            MergeSortParallel task2 = new MergeSortParallel(mid + 1, right, input);
            invokeAll(task1, task2);
            output = merge(task1.result(), task2.result());
        }
    }

    void computeDirect(int left, int right) {
        if (left > right) output = new int[]{};
        else {
            output = new int[]{input[left]};
        }
    }

    int[] merge(int[] leftSorted, int[] rightSorted) {
        int p1 = 0;
        int p2 = 0;
        int[] ans = new int[leftSorted.length + rightSorted.length];
        int index = 0;
        while(p1 < leftSorted.length || p2 < rightSorted.length) {
            int v1 = p1 < leftSorted.length ? leftSorted[p1] : Integer.MAX_VALUE;
            int v2 = p2 < rightSorted.length ? rightSorted[p2] : Integer.MAX_VALUE;
            if (v1 <= v2) {
                ans[index++] = v1;
                p1++;
            } else {
                ans[index++] = v2;
                p2++;
            }
        }
        return ans;
    }
}
