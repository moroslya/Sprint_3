package ya.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import ya.yandex.practicum.data.Order;

import java.util.List;

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

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post("/api/v1/orders");

        response.then().statusCode(201)
                .and()
                .assertThat().body("track", Matchers.notNullValue());

        return response.jsonPath().getInt("track");

    }

    @Step("Проверка существования заказа c track = {track}")
    public void sendRequestCheckOrderByTrackExists(Integer track) {

        Response response = RestAssured.given()
                .get("/api/v1/orders/track?t={track}", track);

        response.then().statusCode(200)
                .and()
                .assertThat().body("order", Matchers.notNullValue())
                .and()
                .assertThat().body("order.track", Matchers.equalTo(track));

    }

    @Step("Проверка, что возвращается не пустой список заказов")
    public void sendRequestCheckingNonEmptyListOfOrdersReturned() {

        Response response = RestAssured.given()
                .get("/api/v1/orders");

        response.then().statusCode(200)
                .and().body("orders", Matchers.notNullValue());

        MatcherAssert.assertThat(response.jsonPath().getList("orders").size(), Matchers.greaterThan(0));

    }

    public void sendRequestCancelOrder(Integer track) {

        RestAssured.given()
                .header("Content-type", "application/json")
                .queryParam("track", track)
                .put("/api/v1/orders/cancel")
                .then()
                .statusCode(200);

    }

}
