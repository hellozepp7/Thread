package 线程池;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * Description:
 *          自定义线程池，线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式
 *          这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险
 * @author Zepp Deng
 * @date 2021/3/7
 * </pre>
 */
public class MyThreadPoolTest {

    public static void main(String[] args) {

        int corePoolSize = 2;//核心线程数
        int maximumPoolSize = 4;//最大线程数
        long keepAliveTime = 10;//保持活跃时间
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(3);//队列长度
        ThreadFactory threadFactory = new NameTreadFactory();
        RejectedExecutionHandler handler = new MyIgnorePolicy();//拒绝策略

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
        executor.prestartAllCoreThreads(); // 预启动所有核心线程
        for (int i = 1; i <= 10; i++) {
            MyTask task = new MyTask(String.valueOf(i));
            executor.execute(task);
        }
    }

    /**
     * @Description:
     *          线程池拒绝策略
     * @Author Zepp Deng [2021-03-07 18:06]
     * @Return MyIgnorePolicy
     */
    private static class MyIgnorePolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 可做日志记录等
            System.err.println( r.toString() + " 阻塞");
        }
    }

    /**
     * @Description:
     *          线程名
     * @Author Zepp Deng [2021-03-07 18:07]
     * @Return NameTreadFactory
     */
    private static class NameTreadFactory implements ThreadFactory {
        private final AtomicInteger mThreadNum = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "自定义线程-" + mThreadNum.getAndIncrement());
            System.out.println(t.getName() + " 已创建");
            return t;
        }
    }

    static class MyTask implements Runnable {
        private final String name;
        public MyTask(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            try {
                System.out.println(this.toString() + "执行中!");
                Thread.sleep(3000); //让任务执行慢点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public String getName() {
            return name;
        }
        @Override
        public String toString() {
            return "任务 [名称=" + name + "]";
        }
    }
}
