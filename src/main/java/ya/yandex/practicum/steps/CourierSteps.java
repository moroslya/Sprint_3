package ya.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import ya.yandex.practicum.data.Courier;

import java.sql.Timestamp;

public class CourierSteps {

    @Step("Проверка успешного создания нового курьера")
    public void checkSuccessCreateNewCourier(Courier courier) {

        Response response = sendRequestCreateCourier(courier);

        response.then().statusCode(201)
                .and()
                .assertThat().body("ok", Matchers.equalTo(true));

    }

    @Step("Проверка авторизации существующим курьером")
    public Integer checkSuccessAuthorizationCourier(Courier courier) {

        Response response = sendRequestAuthorizationCourier(courier);

        response.then().statusCode(200)
                .and()
                .assertThat().body("id", Matchers.notNullValue());

        return response.jsonPath().getInt("id");

    }

    @Step("Проверка ошибки при создании курьера с существующим логином")
    public void checkErrorWhenCreatingCourierWithExistingLogin(Courier courier) {

        Response response = sendRequestCreateCourier(courier);

        response.then().statusCode(409)
                .and()
                .assertThat().body("message", Matchers.equalTo("Этот логин уже используется"));

    }

    @Step("Проверка ошибки при создании нового курьера с незаполненными обязательными полями")
    public void checkErrorWhenCreatingCourierNoRequiredFields(String login, String password,String firstName) {

        Courier courier = new Courier(login, password, firstName);

        Response responseCreateCourier = sendRequestCreateCourier(courier);

        responseCreateCourier.then().statusCode(400)
                .and()
                .assertThat().body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Step("Проверка ошибки при авторизации с незаполненными обязательными полями")
    public void checkErrorWhenAuthorizationNoRequiredFields(Courier courier) {

        Response responseAuthorizationCourier = sendRequestAuthorizationCourier(courier);

        responseAuthorizationCourier.then().statusCode(400)
                .and()
                .assertThat().body("message", Matchers.equalTo("Недостаточно данных для входа"));

    }

    @Step("Проверка ошибки при авторизации несуществующим курьером")
    public void checkErrorWhenAuthorizationNonExistentCourier(Courier courier) {

        Response responseAuthorizationCourier = sendRequestAuthorizationCourier(courier);

        responseAuthorizationCourier.then().statusCode(404)
                .and()
                .assertThat().body("message", Matchers.equalTo("Учетная запись не найдена"));

    }

    public String getRandomCourierLogin() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return "courier_" + timestamp.getTime();

    }

    public Response sendRequestCreateCourier(Courier courier){

        return RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier");

    }

    public Response sendRequestAuthorizationCourier(Courier courier){

        return RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier/login");

    }

    public void sendRequestDeleteCourier(Integer id) {

        RestAssured.given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200);

    }

}
