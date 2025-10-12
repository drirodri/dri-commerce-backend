package dri.commerce.user.domain.entity;

import java.util.List;

public record Page<T>(
        List<T> content,
        long total,
        int page,
        int pageSize,
        int totalPages
) {

    public Page {
        content = content != null ? List.copyOf(content) : List.of();
    }

    public static <T> Page<T> of(List<T> content, long total, int page, int pageSize) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        return new Page<>(content, total, page, pageSize, totalPages);
    }

    public boolean hasNext() {
        return page < totalPages;
    }

    public boolean hasPrevious() {
        return page > 1;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }
}