package org.charitable.app.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class Page<T> {
    private final List<T> items;
    private final Pagination pagination;
}
