import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConcurrentBank {
    private final Map<Long, BankAccount> accounts = new TreeMap<>();
    private long nextId = 1;

    public synchronized BankAccount createAccount(long initialBalance) {
        BankAccount account = new BankAccount(nextId, initialBalance);
        accounts.put(nextId, account);
        nextId += 1;
        return account;
    }

    public void transfer(BankAccount from, BankAccount to, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                 "Сумма перевода должна быть положительной"
            );
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать оба счёта"
            );
        }

        synchronized (this) {
            if (from != accounts.get(from.getId()) || to != accounts.get(to.getId())) {
                throw new IllegalArgumentException(
                        "Счета должны принадлежать этому банку"
                );
            }
        }

        if (from == to) {
            return;
        }

        BankAccount first;
        BankAccount second;

        if (from.getId() < to.getId()) {
            first = from;
            second = to;
        }
        else {
            first = to;
            second = from;
        }

        first.getLock().lock();
        try {
            second.getLock().lock();
            try {
                Math.addExact(to.getBalance(), amount);
                from.withdraw(amount);
                to.deposit(amount);
            } finally {
                second.getLock().unlock();
            }
        } finally {
            first.getLock().unlock();
        }
    }


    public synchronized long getTotalBalance() {
        List<BankAccount> accountList = new ArrayList<>(accounts.values());
        int lockedCount = 0;

        try {
            for (BankAccount account : accountList) {
                account.getLock().lock();
                lockedCount += 1;
            }

            long total = 0;

            for (BankAccount account : accountList) {
                total = Math.addExact(total, account.getBalance());
            }

            return total;

        } finally {
            for (int i = lockedCount - 1; i >= 0; i--) {
                BankAccount account = accountList.get(i);
                account.getLock().unlock();
            }
        }

    }
}
