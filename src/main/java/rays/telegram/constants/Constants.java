package rays.telegram.constants;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final Map<String, String> BOT_COMMANDS;

    static {
        BOT_COMMANDS = new HashMap<>();
        BOT_COMMANDS.put("/help", "Привет! Я биржевой помощник, если пропишешь мне тикет - выдам тебе информацию по текущей цене этой компании\nНапример /TSLA\nПосмотреть другие примеры /examples");
        BOT_COMMANDS.put("/examples", "Вот мои самые любимые тикеты: \nГазпром - /GAZP\nСбербанк - /SBER\nAlibaba - /BABA\nMail(VK) - /MAIL\nВТБ - /VTBR \nМечел - /MTLR \nРаскадская - /RASP");

    }

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

}
