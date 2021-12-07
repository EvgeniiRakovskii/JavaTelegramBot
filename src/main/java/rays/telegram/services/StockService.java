package rays.telegram.services;

import rays.telegram.components.StockInfo;
import rays.telegram.components.StockPrice;
import rays.telegram.exceptions.AppException;


import java.io.IOException;

public class StockService {

    private final TinkoffApiService tinkoffApiService = new TinkoffApiService();

    public StockInfo getInfoAboutStock(String ticket) throws AppException {

        try {
            StockInfo stockInfo = tinkoffApiService.getCompanyNameAboutFigi(ticket);
            StockPrice stockPrice = tinkoffApiService.getStockPrice(stockInfo.getFigi());
            stockInfo.setStockPrice(stockPrice);
            return stockInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
