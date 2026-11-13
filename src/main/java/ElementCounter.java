import java.util.HashMap;
import java.util.Map;

public class ElementCounter {

    public static <T> Map<T, Integer> countElements(T[] elements) {

        Map<T, Integer> counts = new HashMap<>();
        for (T element:elements) {
            if (!counts.containsKey(element)) {
                counts.put(element, 1);
            }
            else {
                counts.put(element, counts.get(element)+1);
            }
        }
        return counts;
    }
}
