package org.charitable.app.domain.common.pagination;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Page<T> {
    private final List<T> items;
    private final Pagination pagination;
}
