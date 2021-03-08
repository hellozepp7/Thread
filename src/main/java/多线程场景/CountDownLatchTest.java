package 多线程场景;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * Description:
 *          同步工具类，允许一个或多个线程一直等待，直到其他线程运行完成后再执行。
 *          1.让多个线程等待，模拟高并发；2.等待单个线程，让多个线程完成任务后汇总
 * @author Zepp Deng
 * @date 2021/3/8
 * </pre>
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        //模拟并发
        countDownLatchIncr();
        //模拟主线程等待子线程
        countDownLatchAwait();
    }

    /**
     * @Description:
     *          模拟并发，让一组线程在指定时间一起执行
     * @Author Zepp Deng [2021-03-08 15:31]
     */
    public static void countDownLatchIncr() throws InterruptedException {

        final AtomicInteger atomicInteger = new AtomicInteger(0);
        // 相当于计数器，当所有都准备好了，再一起执行，模仿多并发，保证并发量
        final CountDownLatch countDownLatch1 = new CountDownLatch(1000);
        // 保证所有线程执行完了再打印atomicInteger的值
        final CountDownLatch countDownLatch2 = new CountDownLatch(1000);
        //定长线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++){
            //submit有返回值，而execute没有
            executorService.submit(()->{
                try {
                    //一直阻塞当前线程，直到计时器的值为0,保证同时并发
                    countDownLatch1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //每个线程增加1000次，每次加1
                for (int j = 0; j < 1000; j++) {
                    atomicInteger.incrementAndGet();
                }
                countDownLatch2.countDown();
            });
            //多线程一起执行命令
            countDownLatch1.countDown();
        }
        // 保证所有线程执行完
        countDownLatch2.await();
        System.out.println(atomicInteger);
        //关闭线程池
        executorService.shutdown();
    }

    /**
     * @Description:
     *          模拟主线程等待两个子线程
     * @Author Zepp Deng [2021-03-08 15:49]
     */
    public static void countDownLatchAwait() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行…… ……");
        //第1个子线程执行
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        executorService1.execute(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程："+Thread.currentThread().getName()+"执行");
            latch.countDown();
        });
        executorService1.shutdown();

        //第2个子线程执行
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        executorService2.execute(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程："+Thread.currentThread().getName()+"执行");
            latch.countDown();
        });
        executorService2.shutdown();

        System.out.println("等待两个线程执行完毕…… ……");
        latch.await();
        System.out.println("两个子线程都执行完毕，继续执行主线程");
    }

}
