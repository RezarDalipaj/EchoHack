package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.enumeration.PaymentMethod;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperHelper {

    String mapRoleToString(Role role){
        return role == null ? null : role.toString();
    }
    String paymentMethodToString(PaymentMethod paymentMethod){
        return paymentMethod == null ? null : paymentMethod.toString();
    }

    Role mapStringToRole(String role){
        for (var roleEnum : Role.values()) {
            if (roleEnum.toString().equals(role))
                return roleEnum;
        }
        return null;
    }

    PaymentMethod mapStringToPaymentMethod(String paymentMethod){
        for (var payment : PaymentMethod.values()) {
            if (payment.toString().equals(paymentMethod))
                return payment;
        }
        return null;
    }
}
