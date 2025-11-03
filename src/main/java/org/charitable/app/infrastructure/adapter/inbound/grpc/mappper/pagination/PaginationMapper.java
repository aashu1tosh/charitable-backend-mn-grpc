package org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.pagination;

import org.charitable.app.domain.common.pagination.Pagination;

public class PaginationMapper {

    public static org.charitable.app.proto.Pagination toProtoPagination (Pagination pagination) {
        return org.charitable.app.proto.Pagination.newBuilder()
                .setPage(pagination.getPage())
                .setSize(pagination.getLimit())
                .setTotal(pagination.getTotal())
                .setTotalPages(pagination.getTotalPages())
                .build();
    }
}
