package com.traderdiary.filter;

import java.util.List;

public class SearchResultData {

    private Long total;

    private List<?> resultData;

    public SearchResultData(Long total, List<?> resultData) {
        super();
        this.total = total;
        this.resultData = resultData;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getResultData() {
        return resultData;
    }

    public void setResultData(List<?> resultData) {
        this.resultData = resultData;
    }

}
