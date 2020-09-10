package com;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class Text {
    public static void main(String[] args) throws Exception {
        String s = "12";
        int num = 0;
        for (int i =0;i< 2;i++){
            num += (s.charAt(i)-'0');
        }
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        System.out.println(stack.pop());
        System.out.println(calculate("2+2*3/2-2"));
        System.out.println(Character.isDigit('1'));
        ;
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}};
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        ListNode listNode3 = new ListNode(3);
        ListNode listNode4 = new ListNode(4);
        ListNode listNode5 = new ListNode(5);
        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode4;
        listNode4.next = listNode5;
        removeNthFromEnd(listNode1, 2);
        merge1(intervals);
        System.out.println(lengthOfLongestSubstring("dvdf"));
        System.out.println(lengthOfLongestSubstring1("abcab"));
        System.out.println(lengthOfLongestSubstring1("aaa"));
        int[][] a = new int[1][2];
        System.out.println(a.length);
        System.out.println(a[0].length);
        System.out.println(maxDepthAfterSplit("()")[0]);
        System.out.println(consecutiveNumbersSum(15));
        int[] nums = {1, 2, 3, 4, 2};
        System.out.println("123".charAt(2));
        System.out.println(longestConsecutive1(nums, 10));
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("a");
        list.remove("c");
        list.forEach(System.out::print);
        Map<String, String> map = new HashMap<>();
        map.entrySet().stream().sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList());
        List<String> list1 = new ArrayList<>();
        list1.sort(Comparator.reverseOrder());

    }


    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        first.next = first.next.next;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }


    public static int lengthOfLongestSubstring1(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int n = s.length();
        int res = 1;
        Map<Character, Integer> map = new HashMap<>(); // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            if (map.containsKey(s.charAt(j))) {
                res = Math.max(map.get(s.charAt(j - 1)), res);
                map.put(s.charAt(j), 1);
                map.put(s.charAt(j - 1), 1);
            } else {
                int value = 1;
                if (j - 1 >= 0) {
                    value = map.getOrDefault(s.charAt(j - 1), 1) + 1;
                }
                res = Math.max(value, res);
                map.put(s.charAt(j), value);
            }

        }

        return res;
    }

    int fib(int n) {
        if (n == 2 || n == 1)
            return 1;
        int prev = 1, curr = 1;
        for (int i = 3; i <= n; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }


    public static int[][] merge1(int[][] intervals) {
        Arrays.sort(intervals, (v1, v2) -> v1[0] - v2[0]);
        int[][] res = new int[intervals.length][2];
        int idx = -1;
        for (int i = 0; i < intervals.length; i++) {
            if (idx == -1 || intervals[i][0] > res[idx][1]) {
                res[++idx] = intervals[i];
            } else {
                res[idx][1] = Math.max(res[idx][1], intervals[i][1]);
            }
        }
        return Arrays.copyOf(res, idx + 1);
    }

    public static int[][] merge(int[][] intervals) {
        // 先按照区间起始位置排序
        Arrays.sort(intervals, Comparator.comparingInt(v -> v[0]));
        // 遍历区间
        int[][] res = new int[intervals.length][2];
        int idx = -1;
        for (int[] interval : intervals) {

            // 如果结果数组是空的，或者当前区间的起始位置 > 结果数组中最后区间的终止位置，
            // 则不合并，直接将当前区间加入结果数组。
            if (idx == -1 || interval[0] > res[idx][1]) {
                res[++idx] = interval;
            } else {
                // 反之将当前区间合并至结果数组的最后区间
                res[idx][1] = Math.max(res[idx][1], interval[1]);
            }
        }
        return Arrays.copyOf(res, idx + 1);
    }


    public static int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        Map<Character, Integer> map = new HashMap<>(); // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)), i);
            }
            ans = Math.max(ans, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }
        return ans;
    }

    public static int easyMath(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int num : nums) {
            if (map.containsKey(num)) {
                map.remove(num);
            } else {
                map.put(num, 1);
            }
        }
        List<Integer> list = new CopyOnWriteArrayList<>(map.keySet());
        list.sort(Comparator.reverseOrder());
        int res = 0;
        int temp = 0;
        int min = list.get(list.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            if (i + 1 < list.size()) {
                temp = list.get(i) - list.get(i + 1);
                if (temp != 0 && temp < min) {

                }
            }
        }

        return res;
    }
//       # 1个数时，必然有一个数可构成N
//    # 2个数若要构成N，第2个数与第1个数差为1，N减掉这个1能整除2则能由商与商+1构成N
//    # 3个数若要构成N，第2个数与第1个数差为1，第3个数与第1个数的差为2，N减掉1再减掉2能整除3则能由商、商+1与商+2构成N
//    # 依次内推，当商即第1个数小于等于0时结束

    public static int consecutiveNumbersSum(int N) {
        if (N == 0) {
            return 0;
        }
        N = N - 1;
        int res = 1;
        int i = 2;
        while (N > 0) {
            if (N % i == 0) {
                res++;

            }

            N -= i;
            i++;

        }
        return res;
    }

    public static int[] maxDepthAfterSplit(String seq) {
        int deep = 0;
        int len = seq.length();
        int[] answer = new int[len];
        for (int i = 0; i < len; i++) {
            if (seq.charAt(i) == '(') {
                deep++;
                answer[i] = deep % 2;
            } else {
                answer[i] = deep % 2;
                deep--;
            }
        }
        return answer;
    }

    public static int test(int[] nums, int targetSum) {
        int maxSum = nums[0];
        int tempSum = nums[0];

        for (int i = 0; i < nums.length; i++) {
            if (i + 1 < nums.length && nums[i + 1] - nums[i] == 1) {
                tempSum += nums[i + 1];

            } else {
                maxSum = Math.max(maxSum, tempSum);
            }
        }
        return maxSum;
    }


    public static int longestConsecutive1(int[] nums, int targetSum) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);

        int tempLength = 1;
        int sum = nums[0];
        for (int i = 0; i < nums.length; i++) {
            if (i + 1 < nums.length) {
                int diff = nums[i + 1] - nums[i];
                if (diff == 0) continue;
                if (diff == 1) {
                    tempLength++;
                    sum += nums[i + 1];
                    if (sum == targetSum) {
                        return tempLength;
                    }
                }
            }

        }
        return 0;
    }

    public static int longestConsecutive1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int maxLength = 1;
        int tempLength = 1;

        for (int i = 0; i < nums.length; i++) {
            if (i + 1 < nums.length) {
                int diff = nums[i + 1] - nums[i];
                if (diff == 0) continue;
                if (diff == 1) {
                    tempLength++;
                } else {
                    maxLength = Math.max(maxLength, tempLength);
                    tempLength = 1;
                }
            } else {
                maxLength = Math.max(maxLength, tempLength);
                tempLength = 1;
            }
        }
        return maxLength;
    }

    public static int longestConsecutive(int[] nums) {
//         用hashmap记录每个数当前的连续长度并不断更新
        Map<Integer, Integer> map = new HashMap<>();
        int maxLength = 0;
        for (int num : nums) {
//             重复的数不计入
            if (map.containsKey(num)) continue;
            int left = map.getOrDefault(num - 1, 0);
            int right = map.getOrDefault(num + 1, 0);
            int temp = 1 + left + right;
            map.put(num, temp);
            maxLength = maxLength > temp ? maxLength : temp;
//             更新hashmap中key的左右数字对应的value
            map.put(num - left, temp);
            map.put(num + right, temp);
        }
        return maxLength;
    }

    public static int calculate(String s) {
        Stack<Integer> numStack = new Stack<>();

        char lastOp = '+';
        char[] arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ' ') continue;

            if (Character.isDigit(arr[i])) {
                int tempNum = arr[i] - '0';
                while (++i < arr.length && Character.isDigit(arr[i])) {
                    tempNum = tempNum * 10 + (arr[i] - '0');
                }
                i--;

                if (lastOp == '+') numStack.push(tempNum);
                else if (lastOp == '-') numStack.push(-tempNum);
                else numStack.push(res(lastOp, numStack.pop(), tempNum));
            }
            else {
                lastOp = arr[i];
            }
        }

        int ans = 0;
        for (int num : numStack) ans += num;
        return ans;
    }

    private static int res(char op, int a, int b) {
        if (op == '*') return a * b;
        else if (op == '/') return a / b;
        else if (op == '+') return a + b; //其实加减运算可以忽略
        else return a - b;
    }

}