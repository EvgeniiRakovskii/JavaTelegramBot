package rays.telegram.components;

public class StockInfo {

    private String name;
    private String figi;
    private StockPrice stockPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFigi() {
        return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public StockPrice getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(StockPrice stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return "StockInfo{" +
                "name='" + name + '\'' +
                ", figi='" + figi + '\'' +
                ", stockPrice=" + stockPrice +
                '}';
    }
}
