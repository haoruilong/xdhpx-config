package com.xdhpx.config.jpa.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @ClassName: BaseEntity
 * @Description: 抽离公共属性
 * @author 郝瑞龙
*/
@MappedSuperclass
public class BaseEntity {
	
	@Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", strategy = "uuid")
	private String id;
	
	/**
	 * Hibernate 支持 /Java 8 中的 java.time 类包
	 * @CreationTimestamp 插入时，是由数据库自动生成值，然后 Hibernate 自动刷新
	 * @UpdateTimestamp  与插入注解功能一样只不过它是检测数据更新时的变化
	 */
	@CreationTimestamp
	private Date createTime;
	
	@UpdateTimestamp
	private Date updateTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
