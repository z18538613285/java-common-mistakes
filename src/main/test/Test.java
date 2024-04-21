import org.junit.runner.RunWith;

public class Test {

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        testThread();
    }


    public static void testThread() {
        String name = Thread.currentThread().getName();
        System.out.println(name);
        Thread thread = new Thread(new RunTarget());
        thread.start();

    }

    static class RunTarget implements Runnable {
        public void run() {
            String name = Thread.currentThread().getName();
//            String name = getCurThreadName();
            System.out.println(name);
            String curThreadName = getCurThreadName();

        }
    }

    static class ThreadTarget extends Thread {
        @Override
        public void run() {
            String name = getName();
        }
    }

}
