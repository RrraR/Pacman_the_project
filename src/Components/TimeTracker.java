package Components;

public class TimeTracker extends Thread {
    private boolean running;
    private int secondsPassed;

    public TimeTracker() {
        this.running = false;
        this.secondsPassed = 0;
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            try {
                Thread.sleep(1000);
                secondsPassed++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("TimeTracker interrupted");
            }
        }
    }

    public void stopTracking() {
        this.running = false;
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }
}
