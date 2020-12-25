package buckets.framework.base.common.service;

import buckets.framework.base.common.entity.BaseEntity;
import buckets.framework.base.common.utils.PageUtil;

import java.util.Map;

/**
 * @author buckets
 * @date 2020/11/25
 */
public interface BaseService<T extends BaseEntity,ID> {

    T get(ID id);

    T add(T entity);

    T update(T entity);

    int del(ID[] ids);

    PageUtil<T> page(Map<String, Object> params);
}
