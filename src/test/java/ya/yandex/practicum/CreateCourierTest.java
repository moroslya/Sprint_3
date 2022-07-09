package ya.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

public class CreateCourierTest extends CourierSteps {

    private Courier courier;

    @Test
    @DisplayName("Создание нового курьера")
    public void createAndAuthorizationOneNewCourier() {

        courier = new Courier(getRandomCourierLogin(), "qwerty12345", "Иванов Иван");

        checkSuccessCreateNewCourier(courier);

    }

    @Test
    @DisplayName("Создание двух курьеров с одинаковым логином")
    public void createTwoCouriersWithSameLogin() {

        //Создаем нового курьера
        courier = new Courier(getRandomCourierLogin(), "qwerty12345", "Иванов Иван");

        checkSuccessCreateNewCourier(courier);

        //Пытаемся создать второго курьера с таким же логином
        Courier secondCourier = new Courier(courier.getLogin(), "qwerty54321", "Петров Петр");

        //Согласно описанию в Swagger текст сообщения об ошибке должен быть "message": "Этот логин уже используется"
        //но фактически возвращается : "Этот логин уже используется. Попробуйте другой."
        checkErrorWhenCreatingCourierWithExistingLogin(secondCourier);

    }

    @Test
    @DisplayName("Создание нового курьера с незаполненным логином")
    public void createNewCourierWithoutLogin() {

        checkErrorWhenCreatingCourierNoRequiredFields(null, "qwerty12345", "Иванов Иван");

    }

    @Test
    @DisplayName("Создание нового курьера с незаполненным паролем")
    public void createNewCourierWithoutPassword() {

        checkErrorWhenCreatingCourierNoRequiredFields(getRandomCourierLogin(), null, "Иванов Иван");

    }

    @Test
    @DisplayName("Создание нового курьера с незаполненным именем")
    public void createNewCourierWithoutFirstName() {

        courier = new Courier(getRandomCourierLogin(), "qwerty12345", null);

        checkSuccessCreateNewCourier(courier);

    }

    @After
    public void cleanUp() {

        if (courier != null) {
            Integer idCourier = sendRequestAuthorizationCourier(courier).jsonPath().getInt("id");
            sendRequestDeleteCourier(idCourier);
            courier = null;
        }

    }

}