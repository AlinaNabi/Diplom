package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPaymentPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;


public class PayCreditCard {
    CreditPaymentPage creditPaymentPage;

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
        MainPage mainPage = new MainPage();
        creditPaymentPage = mainPage.pressPayCreditCardButton();
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.databaseCleanUp();
    }

    @AfterAll
    public static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка кредитной картой")
    void shouldApproveCreditCard() {
        var info = getApprovedCard();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
        var creditRequest = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, getCreditRequestInfo().getStatus());
        assertEquals(orderInfo.getPayment_id(), creditRequest.getBank_id());
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка кредитной невалидной картой")
    void shouldPayCreditDeclinedCard() {
        var info = DataHelper.getDeclinedCard();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.bankDeclined();
        var expected = getStatusSecondCard();
        var paymentInfo = getPaymentInfo().getStatus();
        assertEquals(expected, paymentInfo);
    }


    @Test
    @DisplayName("Покупка кредитной картой: пустое поле")
    void shouldEmptyFormWithCredit() {
        creditPaymentPage.emptyForm();

    }

    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля карты, остальные поля - валидные данные")
    public void shouldEmptyFieldCardWithCredit()  {
        var info = getEmptyCardNumber();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldCardNumberError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля карты одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldCardNumberWithCredit() {
        var info = getOneNumberCardNumber();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldCardNumberError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля карты 15 цифрами, остальные поля - валидные данные")
    public void shouldFifteenNumberInFieldCardNumberWithCredit() {
        var info = getFifteenNumberCardNumber();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldCardNumberError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой не из БД, остальные поля - валидные данные")
    public void shouldFakerCardInFieldCardNumberWithCredit() {
        var info = getFakerNumberCardNumber();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFakerCardNumber("Ошибка! Банк отказал в проведении операции.");
    }

    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля месяц, остальные поля - валидные данные")
    public void shouldEmptyFieldMonthWithCredit() {
        var info = getEmptyMonth();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldMonthError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле месяц одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldMonthWithCredit() {
        var info = getOneNumberMonth();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldMonthError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц предыдущий от текущего, остальные поля -валидные данные")
    public void shouldFieldWithPreviousMonthWithCredit() {
        var info = getPreviousMonthInField();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldMonthError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц нулевой (не существующий) месяц" +
            " остальные поля -валидные данные")
    public void shouldFieldWithZeroMonthWithCredit() {
        var info = getZeroMonthInField();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldMonthError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка кредитной картой:  в поле месяц в верном формате тринадцатый (не существующий) месяц" +
            " остальные поля -валидные данные")
    public void shouldFieldWithThirteenMonthWithCredit() {
        var info = getThirteenMonthInField();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldMonthError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля год, остальные поля -валидные данные")
    public void shouldEmptyFieldYearWithCredit() {
        var info = getEmptyYear();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldYearError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, предыдущим годом от текущего" +
            " остальные поля -валидные данные")
    public void shouldPreviousYearFieldYearWithCredit() {
        var info = getPreviousYearInField();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldYearError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля -валидные данные")
    public void shouldPlusSixYearFieldYearWithCredit() {
        var info = getPlusSixYearInField();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldYearError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле владелец пустое, остальные поля -валидные данные")
    public void shouldEmptyFieldNameWithCredit() {
        var info = getApprovedCard();
        creditPaymentPage.sendingEmptyNameValidData(info);
        creditPaymentPage.sendingValidDataWithFieldNameError("Поле обязательно для заполнения");
    }


    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец спец. символами" +
            " остальные поля -валидные данные")
    public void shouldSpecialSymbolInFieldNameWithCredit() {
        var info = getSpecialSymbolInFieldName();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец цифрами" +
            " остальные поля -валидные данные")
    public void shouldNumberInFieldNameWithCredit() {
        var info = getNumberInFieldName();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец русскими буквами" +
            " остальные поля -валидные данные")
    public void shouldRussianNameInFieldNameWithCredit() {
        var info = getRusName();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец только фамилией" +
            " остальные поля -валидные данные")
    public void shouldOnlySurnameInFieldNameWithCredit() {
        var info = getOnlySurnameInFieldName();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }


    @Test
    @DisplayName("Покупка кредитной картой: поле CVV пустое" +
            " остальные поля -валидные данные")
    public void shouldEmptyCVVInFieldCVVWithCredit() {
        var info = getEmptyCVVInFieldCVV();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldCVVError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле CVV одним числом" +
            " остальные поля -валидные данные")
    public void shouldOneNumberInFieldCVVWithCredit() {
        var info = getOneNumberInFieldCVV();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldCVVError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле CVV двумя числами" +
            " остальные поля -валидные данные")
    public void shouldTwoNumberInFieldCVVWithCredit() {
        var info = getOTwoNumberInFieldCVV();
        creditPaymentPage.sendingValidData(info);
        creditPaymentPage.sendingValidDataWithFieldCVVError("Неверный формат");
    }
}
