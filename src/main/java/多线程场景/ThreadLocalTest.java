package 多线程场景;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 * Description:
 *          可以把ThreadLocal看成一个全局Map<Thread, Object>：每个线程获取ThreadLocal变量时，总是使用Thread自身作为key
 *          ThreadLocal相当于给每个线程都开辟了一个独立的存储空间，各个线程的ThreadLocal关联的实例互不干扰
 *          ThreadLocal适合在一个线程的处理流程中保持上下文（避免了同一参数在所有方法中传递）；
 *          使用ThreadLocal要用try ... finally结构，并在finally中清除。
 * @author Zepp Deng
 * @date 2021/3/8
 * </pre>
 */
public class ThreadLocalTest {

    public static ExecutorService threadPool = Executors.newFixedThreadPool(16);

    /**
     * @Description:
     *          当多个线程同时使用相同的SimpleDateFormat对象（如static修饰）的话，
     *          如调用format方法时，多个线程会同时调用calender.setTime方法，导致time被别的线程修改，因此线程是不安全的。
     * @Author Zepp Deng [2021-03-08 16:45]
     */
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                //每个任务都创建了一个 simpleDateFormat 对象
                String data = new ThreadLocalTest().date(finalI);
                System.out.println(data);
            });
        }
        threadPool.shutdown();
    }

    private String date(int seconds){
        Date date = new Date(1000 * seconds);
        SimpleDateFormat dateFormat = ThreadSafeFormater.dateFormatThreadLocal.get();
        return dateFormat.format(date);
    }

    /**
     * @Description:
     *          ThreadLocal给每个线程维护一个自己的simpleDateFormat对象
     *          这个对象在线程之间是独立的，互相没有关系的。这也就避免了线程安全问题
     * @Author Zepp Deng [2021-03-08 16:43]
     */
    static class ThreadSafeFormater{
        public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() ->
                new SimpleDateFormat("mm:ss"));
    }

}
