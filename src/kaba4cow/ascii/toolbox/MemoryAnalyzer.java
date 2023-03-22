package kaba4cow.ascii.toolbox;

public final class MemoryAnalyzer {

	private static long maxUsage;
	private static long currentUsage;
	private static long deltaUsage;

	private static long totalUsage;
	private static int updates;

	private static final float invMaxMemory;

	private MemoryAnalyzer() {

	}

	static {
		maxUsage = Long.MIN_VALUE;
		currentUsage = 0l;
		deltaUsage = 0l;

		totalUsage = 0l;
		updates = 0;

		invMaxMemory = 100f / (float) Runtime.getRuntime().maxMemory();
	}

	public static void update() {
		deltaUsage = currentUsage;
		currentUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		deltaUsage = currentUsage - deltaUsage;

		if (currentUsage > maxUsage)
			maxUsage = currentUsage;

		totalUsage += currentUsage;
		updates++;
	}

	public static void printCurrentInfo() {
		float current = getCurrentUsage() * invMaxMemory;
		float delta = getDeltaUsage() * invMaxMemory;
		Printer.println(String.format("Memory Analyzer: \t%.2f %% \t+ %.2f %%", current, delta));
	}

	public static void printFinalInfo() {
		if (updates < 1)
			return;
		float avg = (totalUsage / (long) updates) * invMaxMemory;
		float max = getMaxUsage() * invMaxMemory;
		Printer.println(String.format("Memory Analyzer:\nAverage usage: \t%.2f %%\nMax usage: \t%.2f %%", avg, max));
	}

	public static long getMaxUsage() {
		return maxUsage;
	}

	public static long getCurrentUsage() {
		return currentUsage;
	}

	public static long getDeltaUsage() {
		return deltaUsage;
	}

	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

}
