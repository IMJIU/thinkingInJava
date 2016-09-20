package containers;

//: containers/MapPerformance.java
// Demonstrates performance differences in Maps.
// {Args: 100 5000} Small to keep build testing short
import java.util.*;

public class MapPerformance {

	static List<Test<Map<Integer, Integer>>> tests = new ArrayList<Test<Map<Integer, Integer>>>();
	static {
		tests.add(new Test<Map<Integer, Integer>>("put") {

			int test(Map<Integer, Integer> map, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					map.clear();
					for (int j = 0; j < size; j++)
						map.put(j, j);
				}
				return loops * size;
			}
		});
		tests.add(new Test<Map<Integer, Integer>>("get") {

			int test(Map<Integer, Integer> map, TestParam tp) {
				int loops = tp.loops;
				int span = tp.size * 2;
				for (int i = 0; i < loops; i++)
					for (int j = 0; j < span; j++)
						map.get(j);
				return loops * span;
			}
		});
		tests.add(new Test<Map<Integer, Integer>>("iterate") {

			int test(Map<Integer, Integer> map, TestParam tp) {
				int loops = tp.loops * 10;
				for (int i = 0; i < loops; i++) {
					Iterator it = map.entrySet().iterator();
					while (it.hasNext())
						it.next();
				}
				return loops * map.size();
			}
		});
	}

	public static void main(String[] args) {
		if (args.length > 0)
			Tester.defaultParams = TestParam.array(args);
		Tester.run(new TreeMap<Integer, Integer>(), tests);
		Tester.run(new HashMap<Integer, Integer>(), tests);
		Tester.run(new LinkedHashMap<Integer, Integer>(), tests);
		Tester.run(new IdentityHashMap<Integer, Integer>(), tests);
		Tester.run(new WeakHashMap<Integer, Integer>(), tests);
		Tester.run(new Hashtable<Integer, Integer>(), tests);
	}
} /*
 * Output: (Sample) ---------- TreeMap ---------- size put get iterate 10 748 168 100 100 506 264 76 1000 771 450 78 10000 2962 561 83 ---------- HashMap ---------- size put get iterate 10 281 76 93
---------- TreeMap ----------
 size     put     get iterate
   10     421     160      45
  100      67      43       7
 1000      68      52       6
10000      81      65       6
---------- HashMap ----------
 size     put     get iterate
   10     321     185      57
  100      22       6       9
 1000      22       7       6
10000      23       5       7
------- LinkedHashMap -------
 size     put     get iterate
   10     344      55      18
  100      33      13       9
 1000      36      15       7
10000      30      13       6
------ IdentityHashMap ------
 size     put     get iterate
   10     149      41     126
  100      31      32      18
 1000      71      70      18
10000      78      79      20
-------- WeakHashMap --------
 size     put     get iterate
   10     130      59      32
  100      52      10      13
 1000      26      11      19
10000      27      10      78
--------- Hashtable ---------
 size     put     get iterate
   10     243      65      32
  100      52      23      12
 1000      26      25       9
10000      35      26      10

* --------- Hashtable --------- size put get iterate 10 264 113 113 100 181 105 76 1000 260 201 80 10000 1245 134 77
 */// :~
