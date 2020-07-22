package com.erichary.blog.utils.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * 通用响应数据对象
 *
 * @author erichary
 */
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericDataResponse<T> extends GenericResponse {

    @ApiModelProperty(name = "data", value = "数据对象", position = 3)
    @JsonProperty("data")
    private T data;

    public static <T> ResponseEntity<GenericDataResponse<T>> okWithData(T data) {
        GenericDataResponse<T> resp = new GenericDataResponse<T>();
        resp.setCode(GenericResponse.CODE_OK);
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }

    /**
     * 重载方法
     *
     * @param noDataMsg 当data为空时提示信息
     * @return ResponseEntity
     * @author lipengchao
     * @date 2019/6/13 16:46
     **/
    public static <T> ResponseEntity<GenericDataResponse<T>> okWithData(T data, String noDataMsg) {
        GenericDataResponse<T> resp = new GenericDataResponse<T>();
        resp.setCode(GenericResponse.CODE_OK);
        resp.setData(data);
        if (data == null) {
            resp.setMsg(noDataMsg);
        }
        return ResponseEntity.ok(resp);
    }

    public static <T> ResponseEntity<GenericDataResponse<T>> errorWithMsg(String msg) {
        GenericDataResponse<T> resp = new GenericDataResponse<T>();
        resp.setCode(GenericResponse.CODE_NG);
        resp.setMsg(msg);
        return ResponseEntity.ok(resp);
    }

    public static <T> ResponseEntity<T> okWithData(T data, HttpHeaders headers) {
        GenericDataResponse<T> resp = new GenericDataResponse<T>();
        resp.setCode(GenericResponse.CODE_OK);
        resp.setData(data);
        return new ResponseEntity(resp, headers, HttpStatus.OK);
    }

    public static <T> ResponseEntity<GenericDataResponse<T>> rcWithData(String msg, T data) {
        GenericDataResponse<T> resp = new GenericDataResponse<T>();
        resp.setCode(GenericResponse.CODE_OK);
        resp.setMsg(msg);
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }
}