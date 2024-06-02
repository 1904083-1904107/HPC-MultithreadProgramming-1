import java.util.List;
import java.util.ArrayList;;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class OperationsQueue {
    private final List<Integer> operations = new ArrayList<>();
    private final Lock q_lock = new ReentrantLock();

    public void addSimulation(int totalSimulation) {
        for (int i = 0; i < totalSimulation; i++) {
            int random = (int) (Math.random() * 200) - 100;
            if (random != 0) {
                q_lock.lock();
                try {
                    operations.add(random);
                } finally {
                    q_lock.unlock();
                }
                System.out.println(i + ". New operation added: " + random);
            }
            try {
                Thread.sleep((int) (Math.random() * 80));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        add(-9999);
    }

    public void add(int amount) {
        q_lock.lock();
        try {
            operations.add(amount);
        } finally {
            q_lock.unlock();
        }
    }

    public int getNextItem() {
        while (true) {
            q_lock.lock();
            try {
                if (!operations.isEmpty()) {
                    return operations.remove(0);
                }
            } finally {
                q_lock.unlock();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
