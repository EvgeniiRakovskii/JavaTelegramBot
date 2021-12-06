package rays.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import rays.telegram.components.StockInfo;
import rays.telegram.exceptions.AppException;
import rays.telegram.services.StockService;

/**
 * Осталось добавить название компании
 * Исправить отображение процента изменения + добавить знак
 * Доработать ошибки, под каждую - свой Exception
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
            long chatId = message.chat().id();
       /*     if(message.from()!=null && ("groolexx".equals(message.from().username()) || "Alexey".equals(message.from().firstName()))){
                request = new SendMessage(chatId, "Тебе ничего не скажу, вначале придумай чем на новый год заниматься будем, потом буду тебе тикеты отображать");
            }else */
            {
                StockInfo stockInfo = null;
                try {
                    stockInfo = stockService.getInfoAboutStock(message.text().replace("/", ""));
                    request = new SendMessage(chatId, stockInfo.getTradeStatus().equals("NotAvailableForTrading") ?
                            String.format("Торги сейчас не ведутся %nЦена закрытия %s %.2f", stockInfo.getName(), stockInfo.getClosePrice()) :
                            String.format("Текущая цена %s %.2f %nПроцент изменения за день %.2f", stockInfo.getName(), stockInfo.getLastPrice(),stockInfo.getPercentageChangeForDay()));

                } catch (AppException e) {
                    request = new SendMessage(chatId, e.getLocalizedMessage());

                }
            }


        }
        if (request != null) {
            bot.execute(request);
        }
    }
}
