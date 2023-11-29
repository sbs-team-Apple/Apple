package com.sbs.apple.exchange;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyForm {

    @NotEmpty(message="이름은 필수항목입니다.")
    private String realname;

    @NotEmpty(message="이메일은 필수항목입니다.")
    private String email;

    @NotEmpty(message="이메일은 필수항목입니다.")
    private String address;

    @NotEmpty(message="핸드폰 번호는 필수항목입니다.")
    private String f_No;

    @NotEmpty(message="핸드폰 번호는 필수항목입니다.")
    private String phonNo_2;

    @NotEmpty(message="핸드폰 번호는 필수항목입니다.")
    private String phonNo_3;

    @NotEmpty(message="자택 주소를 적어주세요.")
    private String homeAdress;

    @NotEmpty(message="은행선택은 필수 항목입니다.")
    private String bank;

    @NotEmpty(message="예금주를 입력 해주세요.")
    private String accountHolder;

    @NotEmpty(message="계좌번호를 입력 해주세요.")
    private String accountNumber;

}
