package buckets.framework.base.common.service;

import buckets.framework.base.common.entity.BaseEntity;

/**
 * @author buckets
 * @date 2020/11/25
 */
public interface BaseService<T extends BaseEntity,ID> {

    T get(ID id);

    T add(T entity);

    T update(T entity);

    int del(ID[] ids);
}
