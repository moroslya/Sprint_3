package ya.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.util.Collections;

public class ListOfOrdersTest extends OrderSteps {

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrders() {

        //Создаем заказ, чтобы в списке заказов точно был хотябы один заказ
        trackOrder = sendRequestCreateOrder(Collections.emptyList());

        //Проверяем, что возвращается не пустой список заказов
        sendRequestGetListOfOrders();

    }

}
