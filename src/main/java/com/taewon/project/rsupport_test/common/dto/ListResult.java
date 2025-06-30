package com.taewon.project.rsupport_test.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "total", "offset", "count", "list" })
@Schema(description="리스트 형태의 응답 결과")
public class ListResult<T> {

    private static class EmptyHolder {
        private static final ListResult<?> empty = new ListResult<>(0, 0, Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    public static <T> ListResult<T> empty() {
        return (ListResult<T>)EmptyHolder.empty;
    }

    @Schema(description="전체 개수")
    int total;

    @Schema(description="오프셋")
    int offset;

    List<T> list;

    @Schema(description="리스트 사이즈")
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    public static <T> ListResult<T> of(List<T> result) {
        return new ListResult<>(result.size(), 0, result);
    }
}
