package com.abhisek.challenge_maker.challenge_maker;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer success;
    private Integer statusCode;
    private String msg;
    private T result;

}

