package rays.telegram.components;

public class StockInfo {

    private float lastPrice;
    private float closePrice;
    private String tradeStatus;
    private String name;
    private float minPriceIncrement;
    private String figi;


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
        float percentage = lastPrice/closePrice ;

        return (percentage - 1) * 100;
    }

    public float getMinPriceIncrement() {
        return minPriceIncrement;
    }

    public void setMinPriceIncrement(float minPriceIncrement) {
        this.minPriceIncrement = minPriceIncrement;
    }

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

    public void addInfo(StockInfo stockInfo){

        this.lastPrice = stockInfo.getLastPrice();
        this.closePrice = stockInfo.getClosePrice();
        this.tradeStatus = stockInfo.getTradeStatus();

    }

    @Override
    public String toString() {
        return "StockInfo{" +
                "lastPrice=" + lastPrice +
                ", closePrice=" + closePrice +
                ", tradeStatus='" + tradeStatus + '\'' +
                ", companyName='" + name + '\'' +
                ", minPriceIncrement=" + minPriceIncrement +
                ", figi='" + figi + '\'' +
                '}';
    }
}
