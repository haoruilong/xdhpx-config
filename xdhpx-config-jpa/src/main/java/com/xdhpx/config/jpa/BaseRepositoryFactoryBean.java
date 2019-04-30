package com.xdhpx.config.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;


@SuppressWarnings("rawtypes")
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T,I extends Serializable> 
														extends JpaRepositoryFactoryBean<R, T, I> {
	
	/**
	 	要想把自定义的方法全局应用，需要仿照参照Spring Data的JpaRepositoryFactoryBean重写一个工厂类，
	 	然后在这个工厂中注册我们自己定义的BaseRepositoryImpl的实现。
	 	这个类上面一堆的泛型，我们不用考虑，只要按照相同的方式来写即可
	 **/
	
	
	/**构造函数**/
    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	/**
	 	创建JpaRepositoryFactoryBean需要调用如下方法，
	 	通过这个方法来返回一个工厂，这里返回的是JpaRepositoryFactory
	 **/
	
	@Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new BaseRepositoryFactory(em);
    }

	 /********************内部类开始*******************/
	
    /**创建一个内部类，该类不用在外部访问**/
	@SuppressWarnings("unchecked")
    private static class BaseRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        public BaseRepositoryFactory(EntityManager em) {
            super(em);
        }

        /**设置具体的实现类是BaseRepositoryImpl**/
		@Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,EntityManager em) {
            return new BaseRepositoryImpl<T, I>((Class<T>) information.getDomainType(), em);
        }

        /**设置具体的实现类的class**/
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
        
    }
	
	 /********************内部类结束*******************/
    
}


