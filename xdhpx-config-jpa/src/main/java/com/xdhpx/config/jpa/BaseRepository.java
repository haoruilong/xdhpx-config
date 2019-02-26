package com.xdhpx.config.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.xdhpx.config.jpa.utils.Page;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID> {

	/**
	 	继承JpaRepository，这样就保证拥有了Spring Data JPA中那些比较好用的方法，
	 	然后可以自定义自己需要的方法。
	 	需要注意的是@NoRepositoryBean这个表示该接口不会创建这个接口的实例
	**/
	
	 /********************自定义方法*******************/
	

	public boolean update(T entity);
	
	public int updateByHQL(String hql, Object... params);  
	
	public T getByHQL(String hql, Object... params);
	
	public List<T> getListByHQL(String hql, Object... params);
	
	public List<T> getListBySQL(String sql, Object... params);
	
	public Integer countByHql(String hql,Object... params);
	
	public Page<T> getPageByHQL(String hql,int pageNo,int pageSize);    


}