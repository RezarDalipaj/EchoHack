package de.dlh.lhind.ecohack.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    PAYPAL("Paypal"),
    CASH("Cash");

    private final String value;
}
