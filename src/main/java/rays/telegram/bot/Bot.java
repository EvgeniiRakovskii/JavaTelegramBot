package rays.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import rays.telegram.components.StockPrice;
import rays.telegram.services.StockService;

public class Bot {
    //    "t.8vh3RRubYA1BMG9iy_W4emU2GRdauSzF_Qtxw6reY8FfwnHb5oiazDqVFG6Thxm12dzjEv-MQ07mp7qfjhwKog"
    // Create  bot passing the token received from @BotFather
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));

    private final StockService stockService = new StockService();

    public Bot(){
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
        Message message = update.message()==null? update.channelPost() : update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        InlineQuery inlineQuery = update.inlineQuery();
        BaseRequest request = null;

        if (inlineQuery!=null){
            if("groolexx".equals(inlineQuery.from().username())){
                request = new AnswerInlineQuery(inlineQuery.id(), new InlineQueryResultArticle("1", "Я пока ещё в разработке", "С тобой, Леха, я не разговариваю!"));
            }
            else {
                request = new AnswerInlineQuery(inlineQuery.id(), new InlineQueryResultArticle("2", "Введи какой-нибудь тикет", "Вбей в чате @RaysJavaBot и интересующий тикет"));

            }
        }

        if(message != null && message.text() != null){
            long chatId = message.chat().id();
            StockPrice stockPrice = stockService.getCurrentAndClosePriceForTinkoff(message.text().replace("/",""));
            if(stockPrice!=null){
                request = new SendMessage(chatId, stockPrice.getTradeStatus().equals("NotAvailableForTrading")?  String.format("Торги сейчас не ведутся, цена закрытия - %.2f",stockPrice.getClosePrice()):String.format("Текущая цена - %f, процент изменения за день - %f",stockPrice.getLastPrice(), stockPrice.getPercentageChangeForDay()));
            }
            else
            request = new SendMessage(chatId, "Что-то пошло не так! Тикет не найден или Тинькофф не отвечает");
        }

        if(request!=null) {
            bot.execute(request);
        }
    }
}
