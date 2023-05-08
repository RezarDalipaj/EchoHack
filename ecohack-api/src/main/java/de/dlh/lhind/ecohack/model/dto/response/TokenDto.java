package de.dlh.lhind.ecohack.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private static final long serialVersionUID = -8091879091924046844L;
    private String accessToken;
    private String refreshToken;
}
