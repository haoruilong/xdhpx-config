package com.xdhpx.config.jpa.utils;
import java.io.Serializable;
import java.util.List;

/***
  * @ClassName: Page
  * @Description: basicService用到的分页类
  * @author 郝瑞龙
  * @param <T>
 */
public class Page<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**每页多少条记录**/
    private int pageSize;
    /**第几页**/
    private int pageNo;
    /**结果集**/
    private List<T> list;
    /**查询记录总数**/
    private int totalRecords;
    /**总页数**/
    private int pageCount;

    
    /**
     * @return 总页数
     * */
    public void getTotalPages() {
        pageCount = totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;
    }
    
    /**
     * 计算当前页开始记录
     * @param currentPage 当前第几页
     * @param pageSize 每页记录数
     * @return 当前页开始记录号
     */
    public int countOffset(int currentPage,int pageSize){
        int offset = pageSize*(currentPage-1);
        return offset;
    }
    

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}