package de.dlh.lhind.ecohack.util.mapper;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.enumeration.PaymentMethod;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@UtilityClass
public final class MappingUtil {

    public static String mapRoleToString(Role role){
        return role == null ? null : role.toString();
    }
    public static String paymentMethodToString(PaymentMethod paymentMethod){
        return paymentMethod == null ? null : paymentMethod.toString();
    }

    public static Role mapStringToRole(String role){
        for (var roleEnum : Role.values()) {
            if (roleEnum.toString().equals(role))
                return roleEnum;
        }
        return Role.CLIENT;
    }

    public static PaymentMethod mapStringToPaymentMethod(String paymentMethod){
        for (var payment : PaymentMethod.values()) {
            if (payment.toString().equals(paymentMethod))
                return payment;
        }
        return PaymentMethod.CASH;
    }

    public static String mapAuthoritiesToRole(Collection<? extends GrantedAuthority> authorities)
            throws UnAuthorizedException {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(UnAuthorizedException::new);
    }
}
