package 创建线程;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * <pre>
 * Description:
 *    使用Callable和Future接口创建线程
 *    不能使用Callable直接创建线程，只能使用Runnable创建线程
 *    Callable创建新的线程时，是通过FutureTask来包装MyCallable对象
 *
 *    a:创建Callable接口的实现类 ，并实现Call方法
 * 　　b:创建Callable实现类的实现，使用FutureTask类包装Callable对象，该FutureTask对象封装了Callable对象的Call方法的返回值
 * 　　c:使用FutureTask对象作为Thread对象的target创建并启动线程
 * 　　d:调用FutureTask对象的get()来获取子线程执行结束的返回值
 * @author Zepp Deng
 * @date 2021/3/6
 * </pre>
 */
public class CallableTest {

    public static void main(String[] args) {

        // 创建MyCallable对象
        Callable<Integer> myCallable = new MyCallable();
        //使用FutureTask来包装MyCallable对象
        FutureTask<Integer> futureTask = new FutureTask<Integer>(myCallable);

        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i == 30) {
                //FutureTask对象作为Thread对象的target创建新的线程
                Thread thread = new Thread(futureTask);
                //线程进入到就绪状态
                thread.start();
            }
        }

        System.out.println("主线程for循环执行完毕..");
        try {
            //取得新创建的新线程中的call()方法返回的结果
            int sum = futureTask.get();
            System.out.println("sum = " + sum);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    private static class MyCallable implements Callable<Integer> {

        private int i = 0;

        // 与run()方法不同的是，call()方法具有返回值
        @Override
        public Integer call() throws Exception {

            int sum = 0;
            for (; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
                sum += i;
            }
            return null;
        }
    }

}
