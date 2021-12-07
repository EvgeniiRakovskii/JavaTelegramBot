package rays.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.json.JSONException;
import rays.telegram.components.StockInfo;
import rays.telegram.components.StockPrice;
import rays.telegram.constants.Constants;
import rays.telegram.exceptions.AppException;
import rays.telegram.services.StockService;

/**
 * Подправить кратность оругления, у всех акций есть свой параметр minPriceIncrement
 * Добавить последнюю новость по компании
 * Подправить код бота, слишком много проверок, надо их как-то разнести
 * Проверить многопоточность, как работает при многих запросах
 * Эффективность
 */

public class Bot {
    // Create  bot passing the token received from @BotFather
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));

    private final StockService stockService = new StockService();

    public Bot() {
    }

    public void serve() {

        // Register for updates
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            // ... process updates
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });


    }

    private void process(Update update) {
        Message message = update.message() == null ? update.channelPost() : update.message();
        BaseRequest request = null;

        if (message != null && message.text() != null) {
            String messageToBot = message.text().replace("@RaysJavaBot","");
            long chatId = message.chat().id();
            if (Constants.BOT_COMMANDS.containsKey(messageToBot.toLowerCase())){
                request = new SendMessage(chatId, Constants.BOT_COMMANDS.get(messageToBot.toLowerCase()));
            }
            else
            {
                StockInfo stockInfo;
                try {
                    stockInfo = stockService.getInfoAboutStock(messageToBot.replace("/", ""));
                    StockPrice stockPrice = stockInfo.getStockPrice();
                    request = new SendMessage(chatId, stockInfo.getStockPrice().getTradeStatus().equals("NotAvailableForTrading") ?
                            String.format("Торги этим тикетом сейчас не ведутся %nЦена закрытия %s %.2f", stockInfo.getName(), stockPrice.getClosePrice()) :
                            String.format("Текущая цена %s %.2f %nПроцент изменения за день %.2f", stockInfo.getName(), stockPrice.getLastPrice(), stockPrice.getPercentageChangeForDay()));
                } catch (JSONException | AppException e) {
                    request = new SendMessage(chatId, e.getLocalizedMessage());
                }
            }
        }
        if (request != null) {
            bot.execute(request);
        }
    }
}
