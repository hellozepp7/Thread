package 多线程场景;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * <pre>
 * Description:
 *      把一个大任务拆成多个小任务并行执行
 *      为了计算密集型而设计的框架。分治思维
 *      数据清洗，排序，查找，数据量特别大几个G甚至上T
 * @author Zepp Deng
 * @date 2021/3/8
 * </pre>
 */
public class ForkJoinTest {

    public static void main(String[] args) {

        // 创建3000个随机数组成的数组:
        long[] array = new long[3000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = random();
            expectedSum += array[i];
        }
        System.out.println("Expected sum: " + expectedSum);

        ForkJoinTask<Long> task = new SumTask(array,0,array.length);
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }

    static Random random = new Random(0);
    static long random() {
        return random.nextInt(10000);
    }

    private static class SumTask extends RecursiveTask<Long> {

        static final int THRESHOLD = 500;
        long[] array;
        int start;
        int end;

        SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // 如果任务足够小,直接计算:
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += this.array[i];
                    // 故意放慢计算速度:
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return sum;
            }
            // 任务太大,一分为二:
            int middle = (end + start) / 2;
            System.out.println(String.format("拆分 %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
            SumTask subtask1 = new SumTask(this.array, start, middle);
            SumTask subtask2 = new SumTask(this.array, middle, end);
            invokeAll(subtask1, subtask2);
            Long subresult1 = subtask1.join();
            Long subresult2 = subtask2.join();
            Long result = subresult1 + subresult2;
            System.out.println("结果 = " + subresult1 + " + " + subresult2 + " ==> " + result);
            return result;
        }
    }

}
