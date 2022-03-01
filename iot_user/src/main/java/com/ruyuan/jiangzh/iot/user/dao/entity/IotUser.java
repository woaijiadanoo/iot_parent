package com.ruyuan.jiangzh.iot.user.dao.entity;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
/**
 * <p>
 * 
 * </p>
 *
 * @author jiangzh
 * @since 2022-02-28
 */
public class IotUser extends Model<IotUser> {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private String userName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "IotUser{" +
        ", uuid=" + uuid +
        ", userName=" + userName +
        "}";
    }
}
