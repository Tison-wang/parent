package com.spring.boot.model;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PageVO<T> extends page {

    private final static Logger logger = LoggerFactory.getLogger(PageVO.class);

    private T Re;

    public T getRe() {
        return Re;
    }

    public void setRe(T re) {
        Re = re;
    }

    public static PageVO getPage(Page page, List list) {
        PageVO pv = new PageVO();
        pv.setRe(list);
        pv.setPageIndex(page.getPageNum());
        pv.setPageSize(page.getPageSize());
        pv.setTotal(page.getTotal());
        logger.info("页索引值：" + page.getPageNum() + "，每页条数：" + page.getPageSize() + "，总记录数：" + page.getTotal());
        return pv;
    }
}
