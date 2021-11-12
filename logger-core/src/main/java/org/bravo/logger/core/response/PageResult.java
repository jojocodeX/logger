package org.bravo.logger.core.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页查询结果
 */
@Setter
@Getter
public class PageResult<T> implements Serializable {

    /** 分页数据 */
    private List<T>              list              = Collections.emptyList();

    /** 总条数 */
    private Long                 totalCount        = 0L;

    /** 当前页 */
    private Long                 currentPage;

    /** 分页条数 */
    private Long                 pageSize;

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    public PageResult(Long currentPage, Long pageSize) {
        this.pageSize = pageSize == null || pageSize == 0 ? DEFAULT_PAGE_SIZE : pageSize;
        this.currentPage = currentPage <= 0 ? 1 : currentPage;
    }

    public Long offset() {
        return getCurrentPage() > 0 ? (getCurrentPage() - 1) * getPageSize() : 0;
    }

    public Long getPages() {
        if (getPageSize() == 0) {
            return 0L;
        }
        Long pages = getTotalCount() / getPageSize();
        if (getTotalCount() % getPageSize() != 0) {
            pages++;
        }
        return pages;
    }

    public Long getTotals() {
        if (getTotalCount() == null || getTotalCount() == 0) {
            return 0L;
        }
        return getTotalCount();
    }

}
