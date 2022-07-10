package ya.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import ya.yandex.practicum.data.Order;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderSteps {

    public Integer trackOrder;

    @Step("Проверка успешного создания заказа с цветом: {color}")
    public Integer sendRequestCreateOrder(List<String> color) {

        Order order = new Order(
                "Иван",
                "Иванов",
                "Тестовый адрес",
                "5",
                "+7 910 000 00 00",
                5,
                "2022-06-26",
                "Тестовый комментарий",
                color
        );

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post("/api/v1/orders");

        response.then().statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());

        return response.jsonPath().getInt("track");

    }

    @Step("Проверка существования заказа c track = {track}")
    public void sendRequestCheckOrderByTrackExists(Integer track) {

        Response response = given()
                .get("/api/v1/orders/track?t={track}", track);

        response.then().statusCode(200)
                .and()
                .assertThat().body("order", notNullValue())
                .and()
                .assertThat().body("order.track", equalTo(track));

    }

    @Step("Проверка, что возвращается не пустой список заказов")
    public void sendRequestCheckingNonEmptyListOfOrdersReturned() {

        Response response = given()
                .get("/api/v1/orders");

        response.then().statusCode(200)
                .and().body("orders", notNullValue());

        MatcherAssert.assertThat(response.jsonPath().getList("orders").size(), greaterThan(0));

    }

    public void sendRequestCancelOrder(Integer track) {

        given()
                .header("Content-type", "application/json")
                .queryParam("track", track)
                .put("/api/v1/orders/cancel")
                .then()
                .statusCode(200);

    }

}
