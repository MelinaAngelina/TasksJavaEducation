import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final long id;
    private long balance;
    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(long id, long initialBalance){

        if (initialBalance < 0) {
            throw new IllegalArgumentException(
                    "Начальный баланс не может быть отрицательным"
            );
        }

        this.id = id;
        this.balance = initialBalance;
    }

    public long getId() {
        return id;
    }

    public long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public void deposit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Нулевое и отрицательное пополнение не допустимо"
            );
        }
        lock.lock();
        try {
            balance = Math.addExact(balance, amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(long amount) {
        if (amount <= 0) {
            throw new IllegalStateException(
                    "Недостаточно средств для снятия"
            );
        }
        lock.lock();
        try {
            if (amount > balance) {
                throw new IllegalArgumentException(
                        "Недостаточно средств"
                );
            }
            balance = balance - amount;
        } finally {
            lock.unlock();
        }
    }

    ReentrantLock getLock() {
        return lock;
    }
}
