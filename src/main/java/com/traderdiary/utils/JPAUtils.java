package com.traderdiary.utils;

import javax.persistence.TypedQuery;
import java.util.List;

public class JPAUtils {

    public static <T> T uniqueResultOrNull(final TypedQuery<T> query) {
        return uniqueResultOrElse(query, null);
    }

    public static <T> T uniqueResultOrElse(final TypedQuery<T> query, final T defaultValue) {
        final List<T> resultList = query.setMaxResults(1).getResultList();
        return resultList.isEmpty() ? defaultValue : resultList.get(0);
    }
}
