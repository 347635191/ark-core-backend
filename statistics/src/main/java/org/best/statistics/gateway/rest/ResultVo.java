package org.best.statistics.gateway.rest;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ResultVo<T> {
    private static final String FAIL = "-1";
    private static final String SUCCESS = "0";

    /**
     * 业务状态码
     */
    private String state;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 错误原因
     */
    private String toast;

    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<>(SUCCESS, data, null);
    }

    public static ResultVo<?> fail(String toast) {
        return new ResultVo<>(FAIL, null, toast);
    }
}
