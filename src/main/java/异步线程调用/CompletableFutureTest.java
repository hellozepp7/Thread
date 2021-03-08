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
        //模拟执行异步任务并获取结果
        supplyAsync();
        //模拟多个CompletableFuture串行执行
        serialAsync();
        //模拟多个CompletableFuture并行执行
        parallelAsync();
    }

    /**
     * @Description:
     *          场景一：异步调用，获取返回结果后继续处理
     *          runAsync方法不支持返回值。
     *          supplyAsync可以支持返回值。
     * @Author Zepp Deng [2021-03-07 22:09]
     * @throws InterruptedException
     */
    public static void supplyAsync() throws InterruptedException {
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
     *          模拟异步调用执行任务
     * @Author Zepp Deng [2021-03-07 22:10]
     * @Return Double
     */
    private static Double fetchPrice(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("查询价格失败!");
        }
        return 5 + Math.random() * 20;
    }

    /**
     * @Description:
     *          场景二：第一个任务执行成功后串行执行第二个任务
     * @Author Zepp Deng [2021-03-08 10:37]
     */
    public static void serialAsync() throws InterruptedException {

        //第一个任务
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(()-> queryCode("中国石油"));
        //第一个任务成功后串行执行第二个
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync(CompletableFutureTest::fetchPriceSerial);
        //成功后处理
        cfFetch.thenAccept(System.out::println);
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(2000);
    }

    /**
     * @Description:
     *          模拟场景二执行任务
     * @Author Zepp Deng [2021-03-08 10:29]
     * @param name
     * @Return String
     */
    private static String queryCode(String name) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "601857";
    }

    /**
     * @Description:
     *          模拟场景二执行任务
     * @Author Zepp Deng [2021-03-08 11:01]
     * @param code
     * @Return Double
     */
    private static Double fetchPriceSerial(String code) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }

    /**
     * @Description:
     *          场景三：并行两个任务，任意一个返回结果就算完成
     * @Author Zepp Deng [2021-03-08 10:37]
     */
    public static void parallelAsync() throws InterruptedException {

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() ->
                queryCodeParallel("中国石油", "https://finance.sina.com.cn/code/"));
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() ->
                queryCodeParallel("中国石油", "https://money.163.com/code/"));
        // 用anyOf合并为一个新的CompletableFuture
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFrom163,cfQueryFromSina);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) ->
                fetchPriceParallel((String) code, "https://finance.sina.com.cn/price/"));
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) ->
                fetchPriceParallel((String) code, "https://money.163.com/price/"));
        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetch.thenAccept(System.out::println);
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }

    private static String queryCodeParallel(String taskName, String url)
    {
        System.out.println("query code from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "601857";
    }

    private static Double fetchPriceParallel(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }


}
