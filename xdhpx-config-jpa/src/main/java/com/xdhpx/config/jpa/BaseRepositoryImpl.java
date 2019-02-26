package com.xdhpx.config.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.xdhpx.config.jpa.utils.Page;


@SuppressWarnings("unchecked")
public class BaseRepositoryImpl<T, ID extends Serializable> 
																			 extends SimpleJpaRepository<T,ID>
 																			 implements BaseRepository<T,ID> {
	
	/**
 		首先我们需要继承SimpleJpaRepository，SimpleJpaRepository帮助我们实现了JpaRepository中的方法。
 		然后实现我们自己写的BaseRepository接口
	 **/

	private static  Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);

	
	private EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    /********************自定义方法*******************/
    
	@Override
	@Transactional
	public boolean update(T entity) {
	    boolean result = false;
	    try {
	        entityManager.merge(entity);
	        result = true;
	    } catch (Exception e) {
	    	logger.info(entityManager.getClass().getName()+"---------------更新出错---------------");
	    	throw e;
	    }
	    return result;
	}

    
	@Override
	@Transactional
	public int updateByHQL(String hql, Object... params) {
	    Query query = entityManager.createQuery(hql);
	    if(params != null){
	        for(int i = 0; i < params.length; i++) {
	            query.setParameter(i+1, params[i]);
	        }
	    }
	    entityManager.close();
	    return query.executeUpdate();
	}

	@Override
	public T getByHQL(String hql, Object... params) {
	    Query query = entityManager.createQuery(hql);
	    if (params != null){
	        for (int i = 0; i < params.length; i++){
	            query.setParameter(i+1, params[i]);
	        }
	    }
	    entityManager.close();
	    return (T) query.getSingleResult();
	}


@Override
public List<T> getListByHQL(String hql, Object... params) {  
    Query query = entityManager.createQuery(hql);  
    if (params != null){
        for (int i = 0;i < params.length; i++) {  
            query.setParameter(i+1, params[i]);  
        }  
    }
    return query.getResultList();  
}  

@Override
public List<T> getListBySQL(String sql, Object... params ) {
    Query query = entityManager.createNativeQuery(sql,entityManager.getClass());
    if (params != null){
        for (int i = 0;params != null && i < params.length; i++){
            query.setParameter(i+1, params[i]);
        }
    }
    return query.getResultList();
}

@Override
public Integer countByHql(String hql, Object... params) {
    Query query = entityManager.createQuery(hql);
    if(params != null){
        for(int i = 0; i < params.length; i++) {
            query.setParameter(i+1, params[i]);
        }
    }
    return (Integer) query.getSingleResult();
}


	@Override
	public Page<T> getPageByHQL(String hql,int pageNo,int pageSize){
		Page<T> page = new Page<T>(); 
		
	    /**总记录数**/
	    int allRow = entityManager.createQuery(hql).getResultList().size();
		//当前页开始记录
	    int offset = page.countOffset(pageNo,pageSize); 
	  //分页查询结果集
	    Query query = entityManager.createQuery(hql);
	    query.setFirstResult(offset);
	    query.setMaxResults(pageSize);
	    List<T> list = query.getResultList(); 
	    
	    page.setPageNo(pageNo);
	    page.setPageSize(pageSize);
	    page.setTotalRecords(allRow);
	    page.getTotalPages();
	    page.setList(list);
		
		return page;
	}


}