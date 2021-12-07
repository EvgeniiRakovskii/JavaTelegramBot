package rays.telegram.components;

public class StockPrice {

    private float lastPrice;
    private float closePrice;
    private String tradeStatus;
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
        return (lastPrice / closePrice - 1) * 100;
    }

    public float getMinPriceIncrement() {
        return minPriceIncrement;
    }

    public void setMinPriceIncrement(float minPriceIncrement) {
        this.minPriceIncrement = minPriceIncrement;
    }

}
