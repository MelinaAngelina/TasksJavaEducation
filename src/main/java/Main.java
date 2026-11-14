import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        DataAggregator dataAggregator = new DataAggregator();
        CompletableFuture<ProductInfo> productFuture =
                dataAggregator.aggregateProductInfoAsync("Ноутбук");
        System.out.println("Запросы запущены, основной поток продолжает работу");
        ProductInfo info = productFuture.join();
        System.out.println(info);
    }
}