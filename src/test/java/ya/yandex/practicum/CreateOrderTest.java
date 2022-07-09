package ya.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends OrderSteps {

    private final List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Test
    @DisplayName("Создание нового заказа")
    public void createNewOrder() {

        //Создаем заказ с выбранным цветом
        trackOrder = sendRequestCreateOrder(color);

        //Проверяем, что созданный заказ существует
        sendRequestGetOrder(trackOrder);

    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        };
    }

}
