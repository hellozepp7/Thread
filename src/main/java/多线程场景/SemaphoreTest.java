package 多线程场景;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * <pre>
 * Description:
 *          用于限流，控制访问特定资源的线程数目，底层依赖AQS的状态state
 *          资源访问，服务限流，Hystrix里基于信号量方式
 * @author Zepp Deng
 * @date 2021/3/8
 * </pre>
 */
public class SemaphoreTest {

    private  static Semaphore semaphore = new Semaphore(10);

    /**
     * @Description:
     *          模拟100辆车进入停车场，只有10个车位的场景
     * @Author Zepp Deng [2021-03-08 14:18]
     */
    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                try {
                    System.out.println("===="+Thread.currentThread().getName()+"来到停车场");
                    //返回可用的令牌数量
                    if(semaphore.availablePermits()==0){
                        System.out.println("车位不足，请耐心等待");
                    }
                    //获取令牌尝试进入停车场
                    semaphore.acquire();

                    System.out.println(Thread.currentThread().getName()+"成功进入停车场");
                    //模拟车辆在停车场停留的时间
                    Thread.sleep(new Random().nextInt(10000));
                    System.out.println(Thread.currentThread().getName()+"驶出停车场");

                    //释放令牌，腾出停车场车位
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
