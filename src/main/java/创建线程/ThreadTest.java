package 创建线程;

/**
 * <pre>
 * Description:
 *         创建多线程之继承Thread类，重写该类的run()方法
 * @author Zepp Deng
 * @date 2021/3/6
 * </pre>
 */
public class ThreadTest  {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i == 30) {
                // 创建2个新的线程,进入新建状态
                Thread myThread1 = new MyThread();
                Thread myThread2 = new MyThread();
                //调用start()方法使得线程进入就绪状态
                //此时此线程并不一定会马上得以执行，这取决于CPU调度时机
                myThread1.start();
                myThread2.start();
            }
        }
    }

    /**
     * @Description:
     *          run()方法的方法体代表了线程需要完成的任务，称之为线程执行体
     * @Author Zepp Deng [2021-03-06 22:33]
     */
    static class MyThread extends Thread {
        @Override
        public void run() {
            int i = 0;
            for (i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
        }
    }



}
