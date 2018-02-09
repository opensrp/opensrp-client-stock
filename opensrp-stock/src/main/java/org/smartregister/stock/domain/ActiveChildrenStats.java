package org.smartregister.stock.domain;

/**
 * Created by samuelgithengi on 2/8/18.
 */

public class ActiveChildrenStats {
    private Long childrenLastMonthZeroToEleven = 0l;
    private Long childrenLastMonthtwelveTofiftyNine = 0l;
    private Long childrenThisMonthZeroToEleven = 0l;
    private Long childrenThisMonthtwelveTofiftyNine = 0l;

    public Long getChildrenLastMonthZeroToEleven() {
        return childrenLastMonthZeroToEleven;
    }

    public void setChildrenLastMonthZeroToEleven(Long childrenLastMonthZeroToEleven) {
        this.childrenLastMonthZeroToEleven = childrenLastMonthZeroToEleven;
    }

    public Long getChildrenLastMonthtwelveTofiftyNine() {
        return childrenLastMonthtwelveTofiftyNine;
    }

    public void setChildrenLastMonthtwelveTofiftyNine(Long childrenLastMonthtwelveTofiftyNine) {
        this.childrenLastMonthtwelveTofiftyNine = childrenLastMonthtwelveTofiftyNine;
    }

    public Long getChildrenThisMonthZeroToEleven() {
        return childrenThisMonthZeroToEleven;
    }

    public void setChildrenThisMonthZeroToEleven(Long childrenThisMonthZeroToEleven) {
        this.childrenThisMonthZeroToEleven = childrenThisMonthZeroToEleven;
    }

    public Long getChildrenThisMonthtwelveTofiftyNine() {
        return childrenThisMonthtwelveTofiftyNine;
    }

    public void setChildrenThisMonthtwelveTofiftyNine(Long childrenThisMonthtwelveTofiftyNine) {
        this.childrenThisMonthtwelveTofiftyNine = childrenThisMonthtwelveTofiftyNine;
    }
}
