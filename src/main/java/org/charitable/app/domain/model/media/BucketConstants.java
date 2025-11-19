package org.charitable.app.domain.model.media;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BucketConstants {

    public static final List<String> PUBLIC_BUCKETS = List.of(
            "donation"
    );

    public static final List<String> PRIVATE_BUCKETS = List.of(
    );

    public static final List<String> ALL_BUCKETS;

    // Static initialization block to safely combine the lists once
    static {
        List<String> combinedList = new ArrayList<>();
        combinedList.addAll(PUBLIC_BUCKETS);
        combinedList.addAll(PRIVATE_BUCKETS);
        // Make the combined list immutable before assigning it
        ALL_BUCKETS = Collections.unmodifiableList(combinedList);
    }

}
