package buckets.framework.base.common.service.impl;

import buckets.framework.base.common.entity.BaseEntity;
import buckets.framework.base.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

/**
 * @author buckets
 * @date 2020/11/25
 */
public class BaseServiceImpl<D extends Mapper<T>, T extends BaseEntity, ID> implements BaseService<T, ID> {

    @Autowired
    public D dao;

    @Override
    public T get(ID id) {
        return dao.selectByPrimaryKey(id);
    }

    @Override
    public T add(T entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        dao.insert(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        entity.setUpdateTime(new Date());
        dao.updateByPrimaryKeySelective(entity);
        return entity;
    }

    @Override
    public int del(ID[] ids) {
        int temp = 0;
        for (ID id : ids) {
            temp += dao.deleteByPrimaryKey(id);
        }
        return temp;
    }
}
