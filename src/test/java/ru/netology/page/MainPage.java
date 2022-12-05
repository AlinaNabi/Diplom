package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final SelenideElement buttonBuy = $x("//span[text()='Купить']//ancestor::button");
    private final SelenideElement buttonBuyWithCredit = $x("//span[text()='Купить в кредит']//ancestor::button");

//   private final SelenideElement buttonDebit = $(withText("Оплата по карте"));
//   private final SelenideElement buttonCredit = $(withText("Кредит по данным карты"));
    public MainPage() {
        buttonBuy.shouldBe(Condition.visible);
        buttonBuyWithCredit.shouldBe(Condition.visible);
    }
    public DebitPaymentPage pressPayDebitCardButton() {
        buttonBuy.click();
        return new DebitPaymentPage();
    }
    public CreditPaymentPage pressPayCreditCardButton() {
        buttonBuyWithCredit.click();
        return new CreditPaymentPage();
    }

}
