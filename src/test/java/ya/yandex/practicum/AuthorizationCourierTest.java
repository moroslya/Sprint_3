package ya.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

public class AuthorizationCourierTest extends CourierSteps {

    private Integer idCourier;

    @Test
    @DisplayName("Авторизация существующим курьером")
    public void authorizationExistingCourier() {

        //Создаем нового курьера
        Courier courier = new Courier(getRandomCourierLogin(), "qwerty12345", null);

        checkSuccessCreateNewCourier(courier);

        //Пытаемся авторизоваться существующим курьером
        idCourier = checkSuccessAuthorizationCourier(courier);

    }

    @Test
    @DisplayName("Авторизация с незаполненным логином")
    public void authorizationWithoutLogin() {

        Courier courier = new Courier(null, "qwerty12345", "Иванов Иван");

        checkErrorWhenAuthorizationNoRequiredFields(courier);

    }

    @Test
    @DisplayName("Авторизация с незаполненным паролем")
    public void authorizationWithoutPassword() {

        Courier courier = new Courier(getRandomCourierLogin(), null, "Иванов Иван");

        //Согласно описанию в Swagger должен вернуться код 400
        //но фактически возвращается 504
        checkErrorWhenAuthorizationNoRequiredFields(courier);

    }

    @Test
    @DisplayName("Авторизоваться под несуществующим курьером")
    public void authorizationWithNonExistentCourier() {

        Courier courier = new Courier(getRandomCourierLogin(), "qwerty12345", "Иванов Иван");

        checkErrorWhenAuthorizationNonExistentCourier(courier);

    }

    @After
    public void cleanUp() {

        if (idCourier != null) {
            sendRequestDeleteCourier(idCourier);
            idCourier = null;
        }

    }

}
