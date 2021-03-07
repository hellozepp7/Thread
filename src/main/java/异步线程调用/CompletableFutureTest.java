package 异步线程调用;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Description:
 *      使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，
 *      这两种方法都不是很好，因为主线程也会被迫等待
 *
 *      从Java 8开始引入了CompletableFuture，它针对Future做了改进
 *      并且提供了函数式编程的能力，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法
 * @author Zepp Deng
 * @date 2021/3/7
 * </pre>
 */
public class CompletableFutureTest {

    public static void main(String[] args) throws InterruptedException {
        runAsync();
    }

    /**
     * @Description:
     *          runAsync方法不支持返回值。
     *          supplyAsync可以支持返回值。
     * @Author Zepp Deng [2021-03-07 22:09]
     * @throws InterruptedException
     */
    public static void runAsync() throws InterruptedException {
        // 创建异步执行任务
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(CompletableFutureTest::fetchPrice);
        // 如果执行成功
        completableFuture.thenAccept(System.out::println);
        //如果执行异常
        completableFuture.exceptionally((e)->{
            e.printStackTrace();
            return null;
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }

    /**
     * @Description:
     *          模拟执行任务
     * @Author Zepp Deng [2021-03-07 22:10]
     * @Return Double
     */
    public static Double fetchPrice(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }


}
