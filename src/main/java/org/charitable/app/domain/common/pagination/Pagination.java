package org.charitable.app.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Pagination {
    private final int page;
    private final int limit;
    private final int total;
    private final int totalPages;
}
