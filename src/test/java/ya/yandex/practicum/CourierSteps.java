package ya.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;

import java.sql.Timestamp;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps {


    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
    }

    @Step("Проверка успешного создания нового курьера")
    public void checkSuccessCreateNewCourier(Courier courier) {

        Response response = sendRequestCreateCourier(courier);

        response.then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

    }

    @Step("Проверка авторизации существующим курьером")
    public Integer checkSuccessAuthorizationCourier(Courier courier) {

        Response response = sendRequestAuthorizationCourier(courier);

        response.then().statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());

        return response.jsonPath().getInt("id");

    }

    @Step("Проверка ошибки при создании курьера с существующим логином")
    public void checkErrorWhenCreatingCourierWithExistingLogin(Courier courier) {

        Response response = sendRequestCreateCourier(courier);

        response.then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется"));

    }

    @Step("Проверка ошибки при создании нового курьера с незаполненными обязательными полями")
    public void checkErrorWhenCreatingCourierNoRequiredFields(String login, String password,String firstName) {

        Courier courier = new Courier(login, password, firstName);

        Response responseCreateCourier = sendRequestCreateCourier(courier);

        responseCreateCourier.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Step("Проверка ошибки при авторизации с незаполненными обязательными полями")
    public void checkErrorWhenAuthorizationNoRequiredFields(Courier courier) {

        Response responseAuthorizationCourier = sendRequestAuthorizationCourier(courier);

        responseAuthorizationCourier.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Step("Проверка ошибки при авторизации несуществующим курьером")
    public void checkErrorWhenAuthorizationNonExistentCourier(Courier courier) {

        Response responseAuthorizationCourier = sendRequestAuthorizationCourier(courier);

        responseAuthorizationCourier.then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

    }

    public String getRandomCourierLogin() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return "courier_" + timestamp.getTime();

    }

    public Response sendRequestCreateCourier(Courier courier){

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier");

    }

    public Response sendRequestAuthorizationCourier(Courier courier){

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier/login");

    }

    public void sendRequestDeleteCourier(Integer id) {

        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200);

    }

}
