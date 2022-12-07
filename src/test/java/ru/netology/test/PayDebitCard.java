package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.DebitPaymentPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.getPaymentInfo;


public class PayDebitCard {
    DebitPaymentPage debitPaymentPage;

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
        MainPage mainPage = new MainPage();
        debitPaymentPage = mainPage.pressPayDebitCardButton();
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
    @DisplayName("Покупка валидной картой")
    public void shouldPayDebitValidCard() {
        var info = getApprovedCard();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
        var paymentInfo = SQLHelper.getPaymentInfo();
        var orderInfo = SQLHelper.getOrderInfo();
        var expectedAmount = "45000";
        assertEquals(expected, getPaymentInfo().getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        assertEquals(expectedAmount, paymentInfo.getAmount());
    }


    @Test
    @SneakyThrows
    @DisplayName("Покупка дебетовой невалидной картой")
    void shouldPayDebitDeclinedCard() {
        var info = getDeclinedCard();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.bankDeclined();
        var paymentStatus = getPaymentInfo();
        assertEquals("DECLINED", paymentStatus);
    }

    @Test
    @DisplayName("Покупка дебетовой картой без заполнения полей")
    void shouldEmptyFormDebitCard() {
        debitPaymentPage.emptyForm();
    }

    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля карты, остальные поля - валидные данные")
    void shouldEmptyFieldCardFormDebit() {
        var info = getEmptyCardNumber();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldCardNumberError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой при заполнения поля карты одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldCardFormDebit() {
        var info = getOneNumberCardNumber();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldCardNumberError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой при заполнения поля карты 15 цифрами, остальные поля - валидные данные")
    public void shouldFifteenNumberInFieldCardNumberFormDebit() {
        var info = getFifteenNumberCardNumber();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldCardNumberError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка картой не из БД, остальные поля - валидные данные")
    public void shouldFakerCardNumberFormDebit() {
        var info = getFakerNumberCardNumber();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFakerCardNumber("Ошибка! Банк отказал в проведении операции");
    }


    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля месяц, остальные поля - валидные данные")
    public void shouldEmptyFieldMonthFormDebit() {
        var info = getEmptyMonth();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldMonthError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой c заполнением поля месяц одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldMonthFormDebit() {
        var info = getOneNumberMonth();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldMonthError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц предыдущий от текущего, остальные поля -валидные данные")
    public void shouldFieldWithPreviousMonthFormDebit() {
        var info = getPreviousMonthInField();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldMonthError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц нулевой (не существующий) месяц" +
            " остальные поля - валидные данные")
    public void shouldFieldWithZeroMonthFormDebit() {
        var info = getZeroMonthInField();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldMonthError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц тринадцатый (не существующий) месяц" +
            " остальные поля - валидные данные")
    public void shouldFieldWithThirteenMonthFormDebit() {
        var info = getThirteenMonthInField();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldMonthError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля год, остальные поля - валидные данные")
    public void shouldEmptyFieldYearFormDebit() {
        var info = getEmptyYear();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldYearError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля год, предыдущим годом от текущего" +
            " остальные поля - валидные данные")
    public void shouldPreviousYearFieldYearFormDebit() {
        var info = getPreviousYearInField();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldYearError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля - валидные данные")
    public void shouldPlusSixYearFieldYearFormDebit() {
        var info = getPlusSixYearInField();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldYearError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец пустое, остальные - валидные данные")
    public void shouldEmptyFieldNameFormDebit() {
        var info = getApprovedCard();
        debitPaymentPage.sendingEmptyNameValidData(info);
        debitPaymentPage.sendingValidDataWithFieldNameError("Поле обязательно для заполнения");
    }


    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец спец. символами" +
            " остальные поля - валидные данные")
    public void shouldSpecialSymbolInFieldNameFormDebit() {
        var info = getSpecialSymbolInFieldName();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение  поля владелец цифрами" +
            " остальные поля - валидные данные")
    public void shouldNumberInFieldNameFormDebit() {
        var info = getNumberInFieldName();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец рус буквами" +
            " остальные поля - валидные данные")
    public void shouldEnglishNameInFieldNameFormDebit() {
        var info = getRusName();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец только фамилия, остальные поля - валидные данные")
    public void shouldOnlySurnameFormDebit() {
        var info = getOnlySurnameInFieldName();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldNameError("Ошибка! Банк отказал в проведении операции");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV пустое" +
            " остальные поля - валидные данные")
    public void shouldEmptyCVVInFieldCVVFormDebit() {
        var info = getEmptyCVVInFieldCVV();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldCVVError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV одно число" +
            " остальные поля - валидные данные")
    public void shouldOneNumberInFieldCVVFormDebit() {
        var info = getOneNumberInFieldCVV();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldCVVError("Неверный формат");
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV двумя числами" +
            " остальные поля - валидные данные")
    public void shouldTwoNumberInFieldCVVАFormDebit() {
        var info = getOTwoNumberInFieldCVV();
        debitPaymentPage.sendingValidData(info);
        debitPaymentPage.sendingValidDataWithFieldCVVError("Неверный формат");
    }
}
