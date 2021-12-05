package rays.telegram.components;

public class StockPrice {

    private float lastPrice;
    private float closePrice;
    private String tradeStatus;
    private String name;
    private float minPriceIncrement;

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public float getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public float getPercentageChangeForDay() {
        return (closePrice/lastPrice * 100);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMinPriceIncrement() {
        return minPriceIncrement;
    }

    public void setMinPriceIncrement(float minPriceIncrement) {
        this.minPriceIncrement = minPriceIncrement;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "lastPrice=" + lastPrice +
                ", closePrice=" + closePrice +
                ", tradeStatus='" + tradeStatus + '\'' +
                '}';
    }
}
