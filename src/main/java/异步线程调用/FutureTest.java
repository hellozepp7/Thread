package 异步线程调用;

import java.util.concurrent.*;

/**
 * <pre>
 * Description:
 *          提交任务后，获取返回值再往下进行，如果没获取到会一直在submit.get()等待
 *          业务线程可以往下先走，提交任务异步执行，最终获取返回值前等待
 * @author Zepp Deng
 * @date 2021/3/7
 * </pre>
 */
public class FutureTest {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Future<?> futureTask = executor.submit(()->{
            //真正的任务在这里执行
            System.out.println("任务开始");
            Thread.sleep(2000);
            return "获取结果";
        });

        //在这里可以做别的任何事情
        try {
            //取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
            Object result = futureTask.get(5000, TimeUnit.MILLISECONDS);
            System.out.println(result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            futureTask.cancel(true);
        }finally {
            executor.shutdown();
        }

    }
}
