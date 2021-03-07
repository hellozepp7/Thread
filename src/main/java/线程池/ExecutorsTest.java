package 线程池;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * Description:
 *          通过Executors线程池创建线程
 *          这些创建线程池的方式最终都是调用了ThreadPoolExecutor类的构造方法返回了一个ExecutorService实例
 * @author Zepp Deng
 * @date 2021/3/6
 * </pre>
 */
public class ExecutorsTest {

    public static void main(String[] args) {
        cachedThreadPoolTest();
        fixedThreadPoolTest();
        singleThreadPoolTest();
        scheduledThreadPoolTest();
    }

    /**
     * @Description:
     *         可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
     * @Author Zepp Deng [2021-03-07 9:40]
     */
    public static void cachedThreadPoolTest(){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(()->{
            System.out.println("cachedThreadPool");
        });
    }

    /**
     * @Description:
     *         创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
     * @Author Zepp Deng [2021-03-07 9:40]
     */
    public static void fixedThreadPoolTest(){
        //获取cpu核心数
        int i = Runtime.getRuntime().availableProcessors();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(i);
        fixedThreadPool.execute(()->{
            System.out.println("fixedThreadPool");
        });
    }

    /**
     * @Description:
     *         创建一个定长线程池，支持定时及周期性任务执行
     * @Author Zepp Deng [2021-03-07 9:40]
     */
    public static void scheduledThreadPoolTest(){
        //获取cpu核心数
        int i = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(i);

        //schedule()保证任务在业务场景下延迟执行
        scheduledThreadPool.schedule(()->{
            System.out.println("延迟5s执行");
        },5,TimeUnit.SECONDS);

        //schedule()只能执行一次，像服务注册中心要不断发心跳注册信息的周期性任务，通过scheduleAtFixedRate()
        //线程启动1s后启动任务，每2s执行一次，如果执行时间较长，循环阻塞
        scheduledThreadPool.scheduleAtFixedRate(()->{
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                System.out.print(sdf.format(new Date()) + " 开始执行, ");
                Thread.sleep(3000);//3s
                System.out.println(sdf.format(new Date()) + "结束执行 ================");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1,2,TimeUnit.SECONDS);

        //阻塞过多任务堆积，改为每执行完一个任务再停顿5秒再执行第二个任务，用scheduleWithFixedDelay()
        scheduledThreadPool.scheduleWithFixedDelay(()->{
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                System.out.print(sdf.format(new Date()) + " 开始执行, ");
                Thread.sleep(3000);//3s
                System.out.println(sdf.format(new Date()) + "结束执行 ================");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1,5,TimeUnit.SECONDS);
    }

    /**
     * @Description:
     *         创建一个单线程化程池，只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     * @Author Zepp Deng [2021-03-07 9:40]
     */
    public static void singleThreadPoolTest(){
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(()->{
            System.out.println("singleThreadExecutor");
        });
    }


}
