package ya.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ya.yandex.practicum.steps.OrderSteps;

import java.util.Collections;

public class ListOfOrdersTest extends OrderSteps {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrders() {

        //Создаем заказ, чтобы в списке заказов точно был хотябы один заказ
        trackOrder = sendRequestCreateOrder(Collections.emptyList());

        //Проверяем, что возвращается не пустой список заказов
        sendRequestCheckingNonEmptyListOfOrdersReturned();

    }

    @After
    public void cleanUp() {

        if (trackOrder != null) {
            sendRequestCancelOrder(trackOrder);
            trackOrder = null;
        }

    }

}
