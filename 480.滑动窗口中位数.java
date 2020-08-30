import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/*
 * @lc app=leetcode.cn id=480 lang=java
 *
 * [480] 滑动窗口中位数
 */

// @lc code=start
class Solution {
    public double[] medianSlidingWindow(int[] nums, int k) {
        if (nums == null || k == 0) {
            return null;
        }

        // 小根堆, 存放大的一半元素
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // 大根堆，存放小的一半元素
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> Integer.compare(o2, o1));
        // 存放每次的中位数
        List<Double> medians = new ArrayList<>();
        // 参与需要删除元素
        Map<Integer, Integer> deleteNums = new HashMap<>();

        int right = 0;

        while (right < k) {
            maxHeap.add(nums[right]);
            right++;
        }

        for (int i = 0; i < k / 2; i++) {
            // 将大根堆里较大的一半元素放到小根堆里
            minHeap.add(maxHeap.poll());
        }

        System.out.println("max");
        System.out.println(maxHeap);
        System.out.println("min");
        System.out.println(minHeap);

        // 此时小根堆存放了窗口里较大的一半元素，大根堆存放了较小的一半元素
        // 排除掉删除的元素，大根堆的元素个数最多比小根堆多一个
        while (true) {
            medians.add(k % 2 == 1 ? maxHeap.peek() : ((double) minHeap.peek() + (double) maxHeap.peek()) * 0.5);

            if (right >= nums.length) {
                break;
            }

            int inNum = nums[right];
            int outNum = nums[right - k];
            right++;
            // 加入大根堆balance - 1, 加入小根堆balance + 1
            int balance = 0;

            // 处理out的元素
            if (outNum <= maxHeap.peek()) {
                // 在大根堆里移除
                balance++;
            } else {
                balance--;
            }

            // 元素标记为要删除
            deleteNums.merge(outNum, 1, (oldVal, newVal) -> oldVal + newVal);

            // 处理要in的元素
            if (!minHeap.isEmpty() && inNum > minHeap.peek()) {
                minHeap.add(inNum);
                balance++;
            } else {
                maxHeap.add(inNum);
                balance--;
            }

            // 大根堆元素多的添加到小根堆，使两个堆平衡
            if (balance < 0) {
                minHeap.add(maxHeap.poll());
                balance++;
            }

            if (balance > 0) {
                maxHeap.add(minHeap.poll());
                balance--;
            }

            Integer maxHeapTop = deleteNums.get(maxHeap.peek());

            while (maxHeapTop != null && maxHeapTop > 0) {
                deleteNums.compute(maxHeap.peek(), (key, val) -> val - 1);
                maxHeap.poll();
                maxHeapTop = deleteNums.get(maxHeap.peek());
                System.out.println("移除大根堆头");
            }

            Integer minHeapTop = deleteNums.get(minHeap.peek());

            // 当堆头是被标记了删除的元素时，才需要做操作
            while (minHeapTop != null && minHeapTop > 0) {
                deleteNums.compute(minHeap.peek(), (key, val) -> val - 1);
                minHeap.poll();
                minHeapTop = deleteNums.get(minHeap.peek());
                System.out.println("移除小根堆头");
            }
        }

        return medians.stream().mapToDouble(i -> i).toArray();
    }

    public static void main(String[] args) {
        new Solution().medianSlidingWindow(new int[] { 1, 1, 1, 1 }, 2);
    }
}
// @lc code=end
