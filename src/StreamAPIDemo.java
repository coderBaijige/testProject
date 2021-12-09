
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author xin.luo
 * @date 2017年7月1日 下午10:30:06
 */
public class StreamAPIDemo extends Thread {
	private String name;

	public StreamAPIDemo(String name) {
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println(name + "运行  :  " + i);
			try {
				int j = (int) Math.random() * 10000;
				System.out.println(j);
				sleep(j);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// public static void main(String[] args) {
	// Father mTh1 = new Father("A");
	// Father mTh2 = new Father("B");
	// mTh1.start();
	// mTh2.start();
	// }

	public static void main(String[] args) {
		// of();
		// generate();
		// iterate();
		// peek();
		// collect();
		collects();
//		reduce();
		// allMatch();
		// System.out.println("()");
		// Runnable r = () -> {
		// System.out.println("hello lambda!");
		// };
		// Comparator<Integer> cmp = (x, y) -> {
		// return (x < y) ? -1 : ((x > y) ? 1 : 0);
		// };
	}

	private static void peek() {
		System.out.println("peek()");
		List<Integer> nums = Arrays.asList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);
		System.out.println("sum is:" + nums.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2)
				.peek(System.out::println).skip(3).limit(4).sum());
	}

	private static void allMatch() {
		System.out.println("allMatch()");
		List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		System.out.println(ints.stream().allMatch(item -> item < 100));
		ints.stream().min((o1, o2) -> o1.compareTo(o2)).ifPresent(System.out::println);
	}

	/**
	 * 正如其名字显示的，它可以把Stream中的要有元素收集到一个结果容器中（比如Collection）
	 * 
	 * @author xin.luo
	 * @date 2017年7月2日 下午6:56:40
	 */
	private static void collect() {
		System.out.println("collect()");
		List<Integer> nums = Arrays.asList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);
		List<Integer> numsWithoutNull = nums.stream().filter(num -> (num != null)).collect(
				() -> new ArrayList<Integer>(), (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
		numsWithoutNull = nums.stream().filter(num -> num != null).collect(Collectors.toList());
		System.out.println(numsWithoutNull.toString());
		nums = nums.stream().filter(num -> num != null).collect(Collectors.toList());
		System.out.println(nums);
	}

	/**
	 * java.util.stream.Collectors 类的主要作用就是辅助进行各类有用的 reduction 操作，例如转变输出为
	 * Collection，把 Stream 元素进行归组
	 */
	private static void collects() {
		System.out.println("Collects");
		System.out.println("groupingBy/partitioningBy");
		Map<Integer, List<String>> personGroups = Stream.generate(new Supplier<String>(){
			@Override
			public String get() {
				
				return "sss";
			}}).limit(100)
				.collect(Collectors.groupingBy(String::length));
		Iterator it = personGroups.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, List<String>> persons = (Map.Entry) it.next();
			System.out.println("Age " + persons.getKey() + " = " + persons.getValue().size());
		}
		
		
		Map<Boolean, List<String>> children = Stream.generate(new Supplier<String>(){
			@Override
			public String get() {
				return "sss";
			}}).
				 limit(100).
				 collect(Collectors.partitioningBy(p -> p.toString().length() < 18));
				System.out.println("Children number: " + children.get(true).size());
				System.out.println("Adult number: " + children.get(false).size());
				
	}

	/**
	 * 根据一定的规则将Stream中的元素进行计算后返回一个唯一的值。
	 *
	 * @author xin.luo
	 * @date 2017年7月2日 下午6:56:38
	 */
	private static void reduce() {
		System.out.println("reduce()");
		List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		System.out.println("ints sum is:" + ints.stream().reduce((sum, item) -> sum + item).get());
		System.out.println("ints sum is:" + ints.stream().reduce(10, (sum, item) -> sum + item));

		// 字符串连接，concat = "ABCD"
		String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
		System.out.println("concat : " + concat);
		// 求最小值，minValue = -3.0
		double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
		System.out.println("minValue : " + minValue);
		// 求和，sumValue = 10, 有起始值
		int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
		System.out.println("sumValue : " + sumValue);
		// 求和，sumValue = 10, 无起始值
		sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
		// 过滤，字符串连接，concat = "ace"
		concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);
		System.out.println("concat : " + concat);
	}

	/**
	 * 也是生成无限长度的Stream，和generator不同的是， 其元素的生成是重复对给定的种子值(seed)调用用户指定函数来生成的。
	 * 其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环
	 *
	 * @author xin.luo
	 * @date 2017年7月2日 下午4:33:01
	 */
	private static void iterate() {
		System.out.println("iterate()");
		Stream.iterate(1, item -> item + 2).limit(8).forEach(System.out::println);
	}

	/**
	 * 生成一个无限长度的Stream,其元素的生成是通过给定的Supplier（这个接口可以看成一个对象的工厂，每次调用返回一个给定类型的对象）
	 * 
	 *
	 * @author xin.luo
	 * @date 2017年7月2日 下午4:32:27
	 */
	private static void generate() {
		System.out.println("generate()");
		Stream<Double> stream = Stream.generate(new Supplier<Double>() {
			public Double get() {
				return Math.random();
			}
		});
		// stream.forEach(System.out::println);
		Consumer<? super Double> action = System.out::println;
		Consumer<? super Integer> action2 = System.out::println;
		Stream.generate(() -> Math.random()).limit(10).forEach(action);
		;
		Stream.generate(Math::random).limit(10).forEach(action);
		;
		Random seed = new Random();
		Supplier<Integer> random = seed::nextInt;
		Stream.generate(random).limit(10).forEach(action2);
		// Another way
		IntStream.generate(() -> (int) (System.nanoTime() % 100)).limit(10).forEach(System.out::println);
	}

	private static void of() {
		System.out.println("of()");
		Stream.of(1, 2, 3, 8, 5).forEach(System.out::println);
		Stream.of("taobao").forEach(System.out::println);
		// System.out::println;
		Predicate<Integer> isGreaterThan50 = number -> number > 50;
		Predicate<Integer> isGreaterThan20 = s -> s < 10;
	}
}
