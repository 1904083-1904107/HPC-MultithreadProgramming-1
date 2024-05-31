import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {
    private final String accountNumber;
    private final OperationsQueue operationsQueue;
    private int balance = 0;
    private final Lock b_Lock = new ReentrantLock();

    public Bank(String accountNumber, OperationsQueue operationsQueue) {
        this.accountNumber = accountNumber;
        this.operationsQueue = operationsQueue;
    }

    public void deposit() {
        while (true) {
            int amount = operationsQueue.getNextItem();
            if (amount == -9999) {
                break;
            }
            if (amount > 0) {
                b_Lock.lock();
                try {
                    balance += amount;
                    System.out.println("Deposited: " + amount + " Balance: " + balance);
                } finally {
                    b_Lock.unlock();
                }
            } else {
                operationsQueue.add(amount);
                System.out.println("Operation added back: " + amount);
            }
        }
    }

    public void withdraw() {
        while (true) {
            int amount = operationsQueue.getNextItem();
            if (amount == -9999) {
                break;
            }
            if (balance + amount < 0) {
                System.out.println("Not enough balance to withdraw: " + amount);
                continue;
            }
            if (amount < 0) {
                b_Lock.lock();
                try {
                    balance += amount;
                    System.out.println("Withdrawn: " + amount + " Balance: " + balance);
                } finally {
                    b_Lock.unlock();
                }
            } else {
                operationsQueue.add(amount);
                System.out.println("Operation added back: " + amount);
            }
        }
    }
}