package rays.telegram.services;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import rays.telegram.components.StockPrice;

import java.io.IOException;

public class StockService {

    private static final String TINKOFF_API_SANDBOX = "https://api-invest.tinkoff.ru/openapi/sandbox/market/";
    private static final String TICKER_URL = TINKOFF_API_SANDBOX + "search/by-ticker?ticker=";
    private static final String PRICE_URL = TINKOFF_API_SANDBOX +"orderbook?depth=1&figi=";

    private final OkHttpClient client = new OkHttpClient();


    public StockPrice getCurrentAndClosePriceForTinkoff(String ticket) {

        try {
            return getStockPrice(getFigi(ticket));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private String getFigi(String ticker) throws IOException {

        Request request = new Request.Builder()
                .url(TICKER_URL + ticker)
                .addHeader("Authorization", "Bearer " + System.getenv("TINKOFF_TOKEN"))
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if(response.body()!=null && response.isSuccessful()) {
            return getFigiFromResponse(response.body().string());
        }
        else {
            return "Tикет не найден";
        }

    }

    private String getFigiFromResponse(String responseBody) throws IOException {

        var json = new JSONObject(responseBody);
        var payload = json.getJSONObject("payload");
        var instruments = payload.getJSONArray("instruments");
        if(instruments.isEmpty()) {
            throw new IOException("Такого тикета нет");
        }
        var ticker = instruments.getJSONObject(0);
        return ticker.getString("figi");

    }

    private StockPrice getStockPrice(String figi) throws IOException {

        Request request = new Request.Builder()
                .url(PRICE_URL + figi)
                .addHeader("Authorization", "Bearer " + System.getenv("TINKOFF_TOKEN"))
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if(response.body()!=null) {
            return getStockPriceFromResponse(response.body().string());
        }
        else {
            return new StockPrice();
        }
    }

    private StockPrice getStockPriceFromResponse(String responseBody){
        var json = new JSONObject(responseBody);
        Gson gson=new Gson();
        return gson.fromJson(json.getJSONObject("payload").toString(), StockPrice.class);

    }

}
