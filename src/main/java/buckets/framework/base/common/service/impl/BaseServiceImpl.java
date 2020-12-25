package buckets.framework.base.common.service.impl;

import buckets.framework.base.common.entity.BaseEntity;
import buckets.framework.base.common.service.BaseService;
import buckets.framework.base.common.utils.PageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public PageUtil<T> page(Map<String, Object> params) {
        int pageCur = Integer.parseInt((String) params.get("page"));
        int size = Integer.parseInt((String) params.get("size"));
        Example example = new Example(getTClass());
        PageHelper.startPage(pageCur, size);
        List<T> list = dao.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new PageUtil<>(pageInfo);
    }


    private Class<T> getTClass() {
        Class<T> clazz = null;
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType;
        if (superclass instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length > 0) {
                clazz = (Class<T>) typeArray[1];
            }
        }
        return clazz;
    }

}
