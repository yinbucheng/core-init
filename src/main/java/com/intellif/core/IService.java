package com.intellif.core;

import java.io.Serializable;
import java.util.List;

/**
 * 基础service
 */
public interface IService<T> {
    /**
     * 保存
     * @param bean
     * @return
     */
    int save(T bean);

    /**
     * 更新
     * @param bean
     * @return
     */
    int update(T bean);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Serializable id);

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(List<? extends Serializable> ids);

    /**
     * 批量添加
     * @param datas
     */
    void batchAdd(List<T> datas);

    /**
     * 批量更新
     * @param datas
     */
    void batchUpdate(List<T> datas);

    /**
     * 显示所有
     * @return
     */
    List<T> listAll();

    /**
     * 根据字段查找
     * @param fieldName
     * @param value
     * @return
     */
    List<T> listEqualField(String fieldName, Object value);

    /**
     * 根据字段模糊查找
     * @param fieldName
     * @param value
     * @return
     */
    List<T> listLikeField(String fieldName, String value);

    /**
     * 根据字段范围查找
     * @param fieldName
     * @param start
     * @param end
     * @return
     */
    List<T> listBetweenField(String fieldName, Object start, Object end);

}
