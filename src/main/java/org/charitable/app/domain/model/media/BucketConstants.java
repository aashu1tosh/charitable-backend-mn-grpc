package org.charitable.app.domain.model.media;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BucketConstants {

    public static final List<String> PUBLIC_BUCKETS = List.of(
            "donation",
            "donations"
    );

    public static final List<String> PRIVATE_BUCKETS = List.of(
    );

    public static final List<String> ALL_BUCKETS;

    static {
        List<String> combinedList = new ArrayList<>();
        combinedList.addAll(PUBLIC_BUCKETS);
        combinedList.addAll(PRIVATE_BUCKETS);
        ALL_BUCKETS = Collections.unmodifiableList(combinedList);
    }

}
