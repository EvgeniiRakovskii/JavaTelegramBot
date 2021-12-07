package rays.telegram.services;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import rays.telegram.components.StockInfo;
import rays.telegram.components.StockPrice;
import rays.telegram.exceptions.AppException;
import rays.telegram.exceptions.StockServerNotAvailableException;
import rays.telegram.exceptions.TicketNotFoundException;

import java.io.IOException;

public class TinkoffApiService {

    private static final String TINKOFF_API_SANDBOX = "https://api-invest.tinkoff.ru/openapi/sandbox/market/";
    private static final String TICKER_URL = TINKOFF_API_SANDBOX + "search/by-ticker?ticker=";
    private static final String PRICE_URL = TINKOFF_API_SANDBOX + "orderbook?depth=1&figi=";

    private final Gson gson = new Gson();

    private final OkHttpClient client = new OkHttpClient();


    protected StockInfo getCompanyNameAboutFigi(String ticker) throws IOException, AppException {

        Request request = new Request.Builder()
                .url(TICKER_URL + ticker)
                .addHeader("Authorization", "Bearer " + System.getenv("TINKOFF_TOKEN"))
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() != null && response.isSuccessful()) {
            StockInfo stockInfo = getStockInfoFromResponse(response.body().string());
            if (stockInfo.getFigi() == null) {
                throw new TicketNotFoundException(String.format("Тикет %s не найден", ticker));
            }
            return stockInfo;
        } else {
            throw new StockServerNotAvailableException("Сервер недоступен, статус ответа " + response.code());
        }

    }

    private StockInfo getStockInfoFromResponse(String responseBody) {
        var json = new JSONObject(responseBody);
        var instruments = json.getJSONObject("payload").getJSONArray("instruments");

        if (instruments.isEmpty()) {
            return new StockInfo();
        } else {
            return gson.fromJson(instruments.getJSONObject(0).toString(), StockInfo.class);
        }
    }

    protected StockPrice getStockPrice(String figi) throws IOException, StockServerNotAvailableException {

        Request request = new Request.Builder()
                .url(PRICE_URL + figi)
                .addHeader("Authorization", "Bearer " + System.getenv("TINKOFF_TOKEN"))
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() != null) {
            return getStockPriceFromResponse(response.body().string());

        } else {
            throw new StockServerNotAvailableException("Response from server " + response.code());
        }
    }

    private StockPrice getStockPriceFromResponse(String responseBody) {
        var json = new JSONObject(responseBody);
        return gson.fromJson(json.getJSONObject("payload").toString(), StockPrice.class);

    }

}
