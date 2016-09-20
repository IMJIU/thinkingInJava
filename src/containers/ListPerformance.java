package containers;

//: containers/ListPerformance.java
// Demonstrates performance differences in Lists.
// {Args: 100 500} Small to keep build testing short
import java.util.*;
import net.mindview.util.*;

public class ListPerformance {

	static Random rand = new Random();

	static int reps = 1000;

	static List<Test<List<Integer>>> tests = new ArrayList<Test<List<Integer>>>();

	static List<Test<LinkedList<Integer>>> qTests = new ArrayList<Test<LinkedList<Integer>>>();
	static {
		tests.add(new Test<List<Integer>>("add") {

			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int listSize = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					for (int j = 0; j < listSize; j++)
						list.add(j);
				}
				return loops * listSize;
			}
		});
		tests.add(new Test<List<Integer>>("get") {

			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops * reps;
				int listSize = list.size();
				for (int i = 0; i < loops; i++)
					list.get(rand.nextInt(listSize));
				return loops;
			}
		});
		tests.add(new Test<List<Integer>>("set") {

			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops * reps;
				int listSize = list.size();
				for (int i = 0; i < loops; i++)
					list.set(rand.nextInt(listSize), 47);
				return loops;
			}
		});
		tests.add(new Test<List<Integer>>("iteradd") {

			int test(List<Integer> list, TestParam tp) {
				final int LOOPS = 1000000;
				int half = list.size() / 2;
				ListIterator<Integer> it = list.listIterator(half);
				for (int i = 0; i < LOOPS; i++)
					it.add(47);
				return LOOPS;
			}
		});
		tests.add(new Test<List<Integer>>("insert") {

			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops;
				for (int i = 0; i < loops; i++)
					list.add(5, 47); // Minimize random-access cost
				return loops;
			}
		});
		tests.add(new Test<List<Integer>>("remove") {

			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					list.addAll(new CountingIntegerList(size));
					while (list.size() > 5)
						list.remove(5); // Minimize random-access cost
				}
				return loops * size;
			}
		});
		// Tests for queue behavior:
		qTests.add(new Test<LinkedList<Integer>>("addFirst") {

			int test(LinkedList<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					for (int j = 0; j < size; j++)
						list.addFirst(47);
				}
				return loops * size;
			}
		});
		qTests.add(new Test<LinkedList<Integer>>("addLast") {

			int test(LinkedList<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					for (int j = 0; j < size; j++)
						list.addLast(47);
				}
				return loops * size;
			}
		});
		qTests.add(new Test<LinkedList<Integer>>("rmFirst") {

			int test(LinkedList<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					list.addAll(new CountingIntegerList(size));
					while (list.size() > 0)
						list.removeFirst();
				}
				return loops * size;
			}
		});
		qTests.add(new Test<LinkedList<Integer>>("rmLast") {

			int test(LinkedList<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					list.addAll(new CountingIntegerList(size));
					while (list.size() > 0)
						list.removeLast();
				}
				return loops * size;
			}
		});
	}

	static class ListTester extends Tester<List<Integer>> {

		public ListTester(List<Integer> container, List<Test<List<Integer>>> tests) {
			super(container, tests);
		}
		// Fill to the appropriate size before each test:
		@Override
		protected List<Integer> initialize(int size) {
			container.clear();
			container.addAll(new CountingIntegerList(size));
			return container;
		}
		// Convenience method:
		public static void run(List<Integer> list, List<Test<List<Integer>>> tests) {
			new ListTester(list, tests).timedTest();
		}
	}

	public static void main(String[] args) {
		if (args.length > 0)
			Tester.defaultParams = TestParam.array(args);
		// Can only do these two tests on an array:
		Tester<List<Integer>> arrayTest = new Tester<List<Integer>>(null, tests.subList(1, 3)) {

			// This will be called before each test. It
			// produces a non-resizeable array-backed list:
			@Override
			protected List<Integer> initialize(int size) {
				Integer[] ia = Generated.array(Integer.class, new CountingGenerator.Integer(), size);
				return Arrays.asList(ia);
			}
		};
		arrayTest.setHeadline("Array as List");
		arrayTest.timedTest();
		Tester.defaultParams = TestParam.array(10, 5000, 100, 5000, 1000, 1000, 10000, 200);
		if (args.length > 0)
			Tester.defaultParams = TestParam.array(args);
		ListTester.run(new ArrayList<Integer>(), tests);
		ListTester.run(new LinkedList<Integer>(), tests);
		ListTester.run(new Vector<Integer>(), tests);
		Tester.fieldWidth = 12;
		Tester<LinkedList<Integer>> qTest = new Tester<LinkedList<Integer>>(new LinkedList<Integer>(), qTests);
		qTest.setHeadline("Queue tests");
		qTest.timedTest();
	}
} /*
 * Output: (Sample) --- Array as List --- size get set 10 130 183 100 130 164 1000 129 165 10000 129 165 --------------------- ArrayList --------------------- size add get set iteradd insert remove 10
--- Array as List ---
 size     get     set
   10      15      16
  100      14      15
 1000      13      13
10000      13      16
--------------------- ArrayList ---------------------
 size     add     get     set iteradd  insert  remove
   10      92      16      16      36     463     290
  100      22      15      14      21     474      48
 1000      25      16      15      80     281     106
10000       7      19      20     692    1874     768
--------------------- LinkedList ---------------------
 size     add     get     set iteradd  insert  remove
   10     145      29      30      83     327     154
  100      19      47      49      12     119      50
 1000      19     409     415      12      78      42
10000      16    4585    4560      56     124      21
----------------------- Vector -----------------------
 size     add     get     set iteradd  insert  remove
   10     155      17      18      33     633      92
  100      16      16      18      27     403      49
 1000      18      16      18      84     279     107
10000      17      17      23     683   12773     747
-------------------- Queue tests --------------------
 size    addFirst     addLast     rmFirst      rmLast
   10          94          77         129         133
  100          27          23          25          20
 1000          11          11          14          18
10000          10          10          12          11

 * -------------------- size addFirst addLast rmFirst rmLast 10 199 163 251 253 100 98 92 180 179 1000 99 93 216 212 10000 111 109 262 384
 */// :~
