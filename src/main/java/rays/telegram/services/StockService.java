package rays.telegram.services;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import rays.telegram.components.StockInfo;
import rays.telegram.exceptions.AppException;
import rays.telegram.exceptions.StockServerNotAvailableException;
import rays.telegram.exceptions.TicketNotFoundException;

import java.io.IOException;

public class StockService {

    private static final String TINKOFF_API_SANDBOX = "https://api-invest.tinkoff.ru/openapi/sandbox/market/";
    private static final String TICKER_URL = TINKOFF_API_SANDBOX + "search/by-ticker?ticker=";
    private static final String PRICE_URL = TINKOFF_API_SANDBOX + "orderbook?depth=1&figi=";
    private final Gson gson = new Gson();


    private final OkHttpClient client = new OkHttpClient();


    public StockInfo getInfoAboutStock(String ticket) throws AppException {

        try {
            return getStockPrice(getInfoAboutFigiAndCompany(ticket));
        } catch (IOException | TicketNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StockInfo getInfoAboutFigiAndCompany(String ticker) throws IOException, AppException {

        Request request = new Request.Builder()
                .url(TICKER_URL + ticker)
                .addHeader("Authorization", "Bearer " + System.getenv("TINKOFF_TOKEN"))
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() != null && response.isSuccessful()) {
            StockInfo stockInfo = getStockInfoFromResponse(response.body().string());
            if (stockInfo.getFigi().isEmpty()) {
                throw new TicketNotFoundException(String.format("Ticket %s not found", ticker));
            }
            return stockInfo;
        } else {
            throw new StockServerNotAvailableException("Response from server " + response.code());
        }

    }

    private StockInfo getStockInfoFromResponse(String responseBody) {

        var json = new JSONObject(responseBody);
        return gson.fromJson(json.getJSONObject("payload").getJSONArray("instruments").getJSONObject(0).toString(), StockInfo.class);

    }

    private StockInfo getStockPrice(StockInfo stockInfo) throws IOException, StockServerNotAvailableException {

        Request request = new Request.Builder()
                .url(PRICE_URL + stockInfo.getFigi())
                .addHeader("Authorization", "Bearer " + System.getenv("TINKOFF_TOKEN"))
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() != null) {
            stockInfo.addInfo(getStockPriceFromResponse(response.body().string()));
            return stockInfo;
        } else {
            throw new StockServerNotAvailableException("Response from server " + response.code());
        }
    }

    private StockInfo getStockPriceFromResponse(String responseBody) {
        var json = new JSONObject(responseBody);
        return gson.fromJson(json.getJSONObject("payload").toString(), StockInfo.class);

    }

}
