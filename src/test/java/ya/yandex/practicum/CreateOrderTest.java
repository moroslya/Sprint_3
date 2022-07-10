package ya.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ya.yandex.practicum.steps.OrderSteps;

import java.util.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends OrderSteps {

    private final List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
    }

    @Test
    @DisplayName("Создание нового заказа")
    public void createNewOrder() {

        //Создаем заказ с выбранным цветом
        trackOrder = sendRequestCreateOrder(color);

        //Проверяем, что созданный заказ существует
        sendRequestCheckOrderByTrackExists(trackOrder);

    }

    @After
    public void cleanUp() {

        if (trackOrder != null) {
            sendRequestCancelOrder(trackOrder);
            trackOrder = null;
        }

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
