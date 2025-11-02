package org.charitable.app.domain.common.pagination;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Pagination {
    private final int page;
    private final int limit;
    private final long total;
    private final int totalPages;
}
