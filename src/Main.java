public class Main {
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher("5937", "6043");

        while (!dispatcher.getExecutorService().isTerminated()) {
            dispatcher.start();
        }
    }
}
