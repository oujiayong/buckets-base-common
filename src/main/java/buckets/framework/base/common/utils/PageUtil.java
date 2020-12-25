package buckets.framework.base.common.utils;

import buckets.framework.base.common.entity.BaseEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author buckets
 * @date 2020/12/18
 */
public class PageUtil<T extends BaseEntity> {

    /**
     * 当前页码
     */
    private int page;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 总条数
     */
    private int total;

    /**
     * 每页条数
     */
    private int size;

    /**
     * 分页数据
     */
    private List<T> list;

    public PageUtil() {
    }

    public PageUtil(Map<String, Object> params) {
        this.page = params.get("page") == null ? 0 : Integer.parseInt((String) params.get("page"));
        this.size = params.get("size") == null ? 0 : Integer.parseInt((String) params.get("size"));
        this.total = params.get("total") == null ? 0 : Integer.parseInt((String) params.get("total"));
        this.pageCount = params.get("pageCount") == null ? 0 : Integer.parseInt((String) params.get("pageCount"));
    }

    public PageUtil(int page, int pageCount, int total, int size) {
        this.page = page;
        this.pageCount = pageCount;
        this.total = total;
        this.size = size;
    }

    public PageUtil(PageInfo<T> pageInfo) {
        this(pageInfo.getPageNum(), pageInfo.getSize(), (int) pageInfo.getTotal());
        this.list = pageInfo.getList();
    }

    public PageUtil(int page, int size) {
        this(page,size,0);
    }

    public PageUtil(int page, int size, int total) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.pageCount = total % size == 0 ? total / size : total / size + 1;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                ", total=" + total +
                ", size=" + size +
                ", list=" + list +
                '}';
    }
}
