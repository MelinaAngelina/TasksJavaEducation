import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class DataAggregator {

    public CompletableFuture<ProductInfo> aggregateProductInfoAsync(
            String productName) {

        CompletableFuture<Double> priceFuture = CompletableFuture
                .supplyAsync(() -> fetchPrice(productName))
                .exceptionally(error -> 0.0);

        CompletableFuture<String> descriptionFuture = CompletableFuture
                .supplyAsync(() -> fetchDescription(productName))
                .exceptionally(error -> "Нет данных");

        CompletableFuture<Double> ratingFuture = CompletableFuture
                .supplyAsync(() -> fetchRating(productName))
                .exceptionally(error -> 0.0);

        CompletableFuture<Void> allReady = CompletableFuture.allOf(
                priceFuture, descriptionFuture, ratingFuture
        );

        CompletableFuture<ProductInfo> productFuture =
                allReady.thenApply(ignored ->
                        new ProductInfo(
                                productName,
                                priceFuture.join(),
                                descriptionFuture.join(),
                                ratingFuture.join()
                        )
                );

        return productFuture;
    }

    public ProductInfo aggregateProductInfo(String productName) {
        return aggregateProductInfoAsync(productName).join();
    }

    private double fetchPrice(String productName) {
        try {
            Thread.sleep(
                    ThreadLocalRandom.current().nextInt(1, 4) * 1000
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Получение цены прервано", e);
        }

        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            throw new RuntimeException("Сервис цены недоступен");
        }

        return 899.99;
    }

    private String fetchDescription(String productName) {
        try {
            Thread.sleep(
                    ThreadLocalRandom.current().nextInt(1, 4) * 1000
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Получение описания прервано", e);
        }

        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            throw new RuntimeException("Сервис описания недоступен");
        }

        return "Ноутбук для работы и учёбы";
    }

    private double fetchRating(String productName) {
        try {
            Thread.sleep(
                    ThreadLocalRandom.current().nextInt(1, 4) * 1000
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Получение рейтинга прервано", e);
        }

        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            throw new RuntimeException("Сервис рейтинга недоступен");
        }

        return 4.7;
    }
}