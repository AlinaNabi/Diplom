package ru.netology.page;

import com.codeborne.selenide.Condition;

public class DebitPaymentPage extends PaymentPage {

    public DebitPaymentPage() {
        formHeader.should(Condition.visible, Condition.text("Оплата по карте"));
    }
}
