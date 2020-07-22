package com.erichary.blog.utils.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 通用响应列表对象
 *
 * @author erichary
 */
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericListResponse<T> extends GenericResponse {

    @JsonProperty("total")
    private long total;

    @JsonProperty("list")
    private List<T> list;

    public static <T> ResponseEntity<GenericListResponse<T>> listNoCount(List<T> arr) {
        GenericListResponse<T> resp = new GenericListResponse<T>();
        resp.setCode(CODE_OK);
        if (arr == null) {
            resp.setTotal(0);
        } else {
            resp.setTotal(arr.size());
        }
        resp.setList(arr);
        return ResponseEntity.ok(resp);
    }

    public static <T> ResponseEntity<GenericListResponse<T>> listNoCount(String msg, List<T> arr) {
        GenericListResponse<T> resp = new GenericListResponse<T>();
        resp.setCode(CODE_OK);
        resp.setMsg(msg);
        if (arr == null || arr.size() < 1) {
            resp.setTotal(0);
        } else {
            resp.setTotal(arr.size());
        }
        resp.setList(arr);
        return ResponseEntity.ok(resp);
    }

    public static ResponseEntity<?> listAndCount(List<?> arr, Integer tc) {
        return listAndCount(arr, new Long(tc));
    }

    public static <T> ResponseEntity<GenericListResponse<T>> listAndCount(List<T> arr, Long tc) {
        GenericListResponse<T> resp = new GenericListResponse<T>();
        resp.setCode(CODE_OK);
        if (arr == null) {
            resp.setTotal(0);
        } else {
            resp.setList(arr);
            resp.setTotal(tc);
        }
        return ResponseEntity.ok(resp);
    }

    public static <T> ResponseEntity<GenericListResponse<T>> listAndCount(String msg, List<T> arr, Long tc) {
        if (arr == null || arr.size() < 1) {
            return listNoCount(msg, arr);
        } else {
            return listAndCount(arr, tc);
        }
    }
}
