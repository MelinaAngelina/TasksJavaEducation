
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Deque;

public class MyStringBuilder {

    private char[] value;
    private int count;

    private final Deque<Snapshot> history = new ArrayDeque<>();

    // Конструктор
    public MyStringBuilder() {
        value = new char[16];
        count = 0;
    }

    // Снимок состояния
    private static class Snapshot {
        private final char[] value;
        private final int count;

        Snapshot(char[] value, int count) {
            this.value = Arrays.copyOf(value, count);
            this.count = count;
        }
    }

    // Добавление строки
    public MyStringBuilder append(String str) {
        history.push(new Snapshot(value, count));

        if (value.length < count + str.length()) {
            int newCapacity = Math.max(
                    value.length * 2,
                    count + str.length()
            );

            value = Arrays.copyOf(value, newCapacity);
        }

        str.getChars(0, str.length(), value, count);

        count += str.length();

        return this;
    }

    // Отмена последнего изменения
    public void undo() {
        if (!history.isEmpty()) {
            Snapshot snapshot = history.pop();

            value = snapshot.value;
            count = snapshot.count;
        }
    }

    // Преобразование в строку
    @Override
    public String toString() {
        return new String(value, 0, count);
    }
}

