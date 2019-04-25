package com.xdhpx.config.redis.utils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisTemplateUtil {
	
	
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplateUtil.class);
    
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    
    /** Key前缀，方便管理**/
    public static final String KEY_PREFIX_STRING = "xdhpx_string:";
    public static final String KEY_PREFIX_SET = "xdhpx_set:";
    public static final String KEY_PREFIX_LIST = "xdhpx_list:";
    public static final String KEY_PREFIX_MAP = "xdhpx_map:";

    /**--------------------------------公共操作--------------------------------**/
    
    /**
     * 判断缓存是否存在
     * @param k
     * @return
     */
    public boolean containsStringKey(String k) {
        return containsKey(KEY_PREFIX_STRING + k);
    }
    public boolean containsSetKey(String k) {
        return containsKey(KEY_PREFIX_SET + k);
    }
    public boolean containsListKey(String k) {
        return containsKey(KEY_PREFIX_LIST + k);
    }
    public boolean containsMapKey(String k) {
        return containsKey(KEY_PREFIX_MAP + k);
    }
    
    /**实际判断方法**/
    private boolean containsKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable t) {
            logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
    
    /**
     * 移除缓存
     * @param k
     * @return
     */
    public boolean removeString(String k) {
        return remove(KEY_PREFIX_STRING + k);
    }
    public boolean removeSet(String k) {
        return remove(KEY_PREFIX_SET + k);
    }
    public boolean removeList(String k) {
        return remove(KEY_PREFIX_LIST + k);
    }
    public boolean removeMap(String k) {
        return remove(KEY_PREFIX_MAP + k);
    }
    /**实际移除缓存方法**/
    private boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
    
    
    /**--------------------------------ValueOperations操作--------------------------------**/
    
    /**
     * 缓存value操作
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean setCacheString(String k, String v, long time) {
        String key = KEY_PREFIX_STRING + k;
        try {
            ValueOperations<String, Object> valueOps =  redisTemplate.opsForValue();
            valueOps.set(key, v);
            /**设置有效期**/
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存value操作
     * @param k
     * @param v
     * @return
     */
    public boolean setCacheString(String k, String v) {
        return setCacheString(k, v, -1);
    }

    /**
     * 获取value缓存
     * @param k
     * @return
     */
    public String getCacheString(String k) {
        try {
            ValueOperations<String, Object> valueOps =  redisTemplate.opsForValue();
            return valueOps.get(KEY_PREFIX_STRING + k).toString();
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + k + ", error[" + t + "]");
        }
        return null;
    }

    /**--------------------------------SetOperations操作--------------------------------**/
    
    /**
     * 缓存set
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean setCacheSet(String k, Set<Object> set, long time) {
        String key = KEY_PREFIX_SET + k;
        try {
            SetOperations<String, Object> setOps =  redisTemplate.opsForSet();
            setOps.add(key, set.toArray(new Object[set.size()]));
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + set + "]", t);
        }
        return false;
    }

    /**
     * 缓存set
     * @param k
     * @param v
     * @return
     */
    public boolean setCacheSet(String k, Set<Object> set) {
        return setCacheSet(k, set, -1);
    }

    /**
     * 获取缓存set数据
     * @param k
     * @return
     */
    public Set<Object> getCacheSet(String k) {
        try {
            SetOperations<String, Object> setOps = redisTemplate.opsForSet();
            return setOps.members(KEY_PREFIX_SET + k);
        } catch (Throwable t) {
            logger.error("获取set缓存失败key[" + KEY_PREFIX_SET + k + ", error[" + t + "]");
        }
        return null;
    }

    /**--------------------------------ListOperations操作--------------------------------**/
    
    /**
     * 缓存list
     * @param k
     * @param list
     * @param time
     * @return
     */
    public boolean setCacheList(String k, List<Object> list, long time) {
        String key = KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, Object> listOps =  redisTemplate.opsForList();
            listOps.rightPushAll(key, list.toArray());
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + list + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     * @param k
     * @param v
     * @return
     */
    public boolean setCacheList(String k, List<Object> list) {
       return setCacheList(k, list, -1);
    }

    /**
     * 获取list缓存
     * @param k
     * @param start
     * @param end
     * @return
     */
    public List<Object> getCacheList(String k, long start, long end) {
        try {
            ListOperations<String, Object> listOps =  redisTemplate.opsForList();
            return listOps.range(KEY_PREFIX_LIST + k, start, end);
        } catch (Throwable t) {
            logger.error("获取list缓存失败key[" + KEY_PREFIX_LIST + k + ", error[" + t + "]");
        }
        return null;
    }
    /**
      * 获取list集合
      * @param k  
      * @return
     */
    public List<Object> getCacheList(String k) {
    	/**0 ，-1 代表所有值**/
    	return getCacheList(k,0,-1);
    }

    /**
     * 获取总条数, 可用于分页
     * @param k
     * @return
     */
    public long getCacheListSize(String k) {
        try {
            ListOperations<String, Object> listOps =  redisTemplate.opsForList();
            return listOps.size(KEY_PREFIX_LIST + k);
        } catch (Throwable t) {
            logger.error("获取list长度失败key[" + KEY_PREFIX_LIST + k + "], error[" + t + "]");
        }
        return 0;
    }
    
    /**--------------------------------HashOperations操作--------------------------------**/

    /**
     * 缓存map
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean setCacheMap(String k, Map<String, Object> v, long time) {
        String key = KEY_PREFIX_MAP + k;
        try {
            HashOperations<String, Object, Object> mapOps = redisTemplate.opsForHash();
            for (Map.Entry<String, Object> entry : v.entrySet()) {
            	mapOps.put(key,entry.getKey(),entry.getValue());
			}
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存list
     * @param k
     * @param v
     * @return
     */
    public boolean setCacheMap(String k, Map<String, Object> v) {
       return setCacheMap(k, v, -1);
    }

    /**
     * 获取map缓存
     * @param k
     * @param start
     * @param end
     * @return
     */
    public Map<Object, Object> getCacheMap(String k) {
        try {
        	HashOperations<String, Object, Object> mapOps = redisTemplate.opsForHash();
            return mapOps.entries(KEY_PREFIX_MAP + k);
        } catch (Throwable t) {
            logger.error("获取map缓存失败key[" + KEY_PREFIX_MAP + k + ", error[" + t + "]");
        }
        return null;
    }
    
}
