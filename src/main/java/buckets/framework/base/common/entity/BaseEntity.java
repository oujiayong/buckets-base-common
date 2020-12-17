package buckets.framework.base.common.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author buckets
 * @date 2020/12/16
 */
public class BaseEntity implements Serializable {

    public Date getCreateTime() {
        return null;
    }

    public void setCreateTime(Date createTime) {
    }

    public Date getUpdateTime() {
        return null;
    }

    public void setUpdateTime(Date updateTime) {
    }

}
