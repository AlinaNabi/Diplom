package ru.netology.page;

import com.codeborne.selenide.Condition;

public class CreditPaymentPage extends PaymentPage{
    public CreditPaymentPage() {
        formHeader.should(Condition.visible, Condition.text("Кредит по данным карты"));
    }
}
