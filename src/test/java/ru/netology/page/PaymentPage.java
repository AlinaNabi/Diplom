package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PaymentPage {
//    public void payDebitCard() {
//        buttonBuy.click();
//        buttonDebit.shouldBe(visible);
//    }
//
//    public void buyCreditCard() {
//        buttonBuyWithCredit.click();
//        buttonCredit.shouldBe(visible);
//    }

    protected static final SelenideElement formHeader = $x("//form//preceding-sibling::h3");
    private final SelenideElement cardNumberField = $x("//input[@placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $x("//input[@placeholder='08']");
    private final SelenideElement yearField = $x("//input[@placeholder='22']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("input");
    private final SelenideElement cvcField = $x("//input[@placeholder='999']");
    private SelenideElement buttonContinue = $x("//span[text()='Продолжить']//ancestor::button");


    //    ошибки полей
    private SelenideElement fieldCardNumberError = $x("//*[text()='Номер карты']/..//*[@class='input__sub']");
    private SelenideElement fieldMonthError = $x("//*[text()='Месяц']/..//*[@class='input__sub']");
    private SelenideElement fieldYearError = $x("//*[text()='Год']/..//*[@class='input__sub']");
    private SelenideElement fieldOwnerError = $x("//*[text()='Владелец']/..//*[@class='input__sub']");
    private SelenideElement fieldCvcError = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");

    private SelenideElement notificationApproved = $x("//div[contains(@class, 'notification_status_ok')]");
    private SelenideElement notificationError = $x("//div[contains(@class, 'notification_status_error')]");

    //    продолжить
    public void pressButtonForContinue() {
        buttonContinue.click();
    }

    public void sendingValidData (DataHelper.CardInfo info) {
        cardNumberField.setValue(info.getNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getHolder());
        cvcField.setValue(info.getCvc());
        buttonContinue.click();
    }


    public void sendingValidDataWithFieldCardNumberError (String error) {
        fieldCardNumberError.shouldHave(text(error)).shouldBe(visible);
    }

    public void sendingValidDataWithFakerCardNumber (String error) {
        notificationError.shouldHave(text(error)).shouldBe(visible);
    }

    public void sendingValidDataWithFieldMonthError (String error) {
        fieldMonthError.shouldHave(text(error)).shouldBe(visible);

    }

    public void sendingValidDataWithFieldYearError (String error) {

        fieldYearError.shouldHave(text(error)).shouldBe(visible);
    }

    public void sendingEmptyNameValidData (DataHelper.CardInfo info) {
        cardNumberField.setValue(info.getNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        cvcField.setValue(info.getCvc());
        pressButtonForContinue();
        fieldOwnerError.shouldBe(visible);
    }

    public void sendingValidDataWithFieldNameError (String error) {
        fieldOwnerError.shouldHave(text(error)).shouldBe(visible);

    }

    public void sendingValidDataWithFieldCVVError (String error) {
        fieldCvcError.shouldHave(text(error)).shouldBe(visible);
    }

    public void emptyForm() {
        pressButtonForContinue();
        fieldCardNumberError.shouldBe(visible);
        fieldMonthError.shouldBe(visible);
        fieldYearError.shouldBe(visible);
        fieldOwnerError.shouldBe(visible);
        fieldCvcError.shouldBe(visible);
    }

    public void bankApproved() {

        notificationApproved.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void bankDeclined() {

        notificationError.shouldBe(visible, Duration.ofSeconds(15));
    }
}
