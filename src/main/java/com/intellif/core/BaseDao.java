package com.intellif.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *create by yinchong
 * @param <T> 实体类
 */
public interface BaseDao<T> {

    /**
     * 保存和更新数据
     * @param bean
     */
    void save(T bean);

    /**
     * 更新数据
     * @param bean
     */
    void update(T bean);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    T findOne(Serializable id);

    /**
     * 根据id删除制定数据
     * @param id
     * @return
     */
    int delete(Serializable id);

    /**
     * 查询所有数据
     * @return
     */
    List<T> findAll();

    /**
     * 分页显示数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> pageList(int pageNum, int pageSize);

    /**
     * 查询所有并按主键降序
     * @return
     */
    List<T> findAllDesc();

    /**
     * 分页显示并按主键降序
     * @return
     */
    List<T> pageListDesc(int pageNum, int pageSize);

    /**
     * 对指定字段降序排序
     * @param fieldName
     * @return
     */
    List<T> findAllDesc(String fieldName);

    /**
     * 安某个字段并查找指定数据个数
     * @param fielName
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> pageListDesc(String fielName, int pageNum, int pageSize);

    /**
     * 查询所有并按主键升序
     * @return
     */
    List<T> findAllAsc();

    /**
     * 查找指定数量并按主键升序
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> pageListAsc(int pageNum, int pageSize);

    /**
     * 对指定字段升序排序
     * @param fieldName
     * @return
     */
    List<T> findAllAsc(String fieldName);

    /**
     * 对指定字段降序并查询指定数量
     * @param fieldName
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> pageListAsc(String fieldName, int pageNum, int pageSize);

    /**
     * 批量保存
     * @param beans
     */
    void batchSave(List<T> beans);

    /**
     * 批量更新所有字段
     * @param beans
     */
    void batchUpdate(List<T> beans);

    /**
     * 批量删除
     * @param ids
     * @return
     */
     int batchDelete(List<? extends Serializable> ids);

    /**
     * 批量更新非空字段
     * @param beans
     */
    void batchUpdateNotNull(List<T> beans);

    /**
     * 这个用于执行删除和更新的sql语句
     *
     * @param sql
     * @param params
     */
    int executeSql(String sql, Object... params);

    /**
     * 这个用于执行删除和更新的hql语句
     * @param hql
     * @param params
     */
    int executeHql(String hql, Object... params);

    /**
     * 根据原始sql语句执行sql
     *
     * @param sql    原始sql语句
     * @param params 要传递的参数
     * @return map对象
     */
    List<Map<String, Object>> findSql(String sql, Object... params);

    /**
     * @param sql    原始sql语句
     * @param clazz  要反射的Class对象
     * @param params 要传递的参数
     * @param <T>
     * @return
     */
    <T> List<T> findSql(String sql, Class<T> clazz, Object... params);

    /**
     * 执行原始sql并返回Object[]集合
     * @param sql
     * @param params
     * @return
     */
    List<Object[]> findObjectSql(String sql, Object... params);

    /**
     * 分页显示数据
     *
     * @param sql
     * @param pageNum  第几页
     * @param pageSize 每页显示多少个
     * @param params   需要传递的参数
     * @return
     */
    PageBean<Map<String,Object>> pageSql(String sql, Integer pageNum, Integer pageSize, Object... params);

    /**
     * 分页显示数据
     * @param sql
     * @param clazz
     * @param pageNum
     * @param pageSize
     * @param params
     * @param <T2>
     * @return
     */
    <T2> PageBean<T2> pageSql(String sql,Class<T2> clazz,int pageNum,int pageSize,Object... params);

    /**
     * 分页查询
     * @param sql
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageBean<Object[]> pageObjectSql(String sql, int pageNum, int pageSize, Object... params);

    /**
     * 执行hql查询语句
     * @param hql
     * @param params
     * @return
     */
    <T2> List<T2> findHql(String hql,Class<T2> clazz, Object... params);

    /**
     * hql语句返回Object[]对象
     * @param hql
     * @param params
     * @return
     */
    List<Object[]> findObjectHql(String hql, Object... params);

    /**
     * hql的分页操作
     * @param hql
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageBean<T> pageHql(String hql, Integer pageNum, Integer pageSize, Object... params);


    /**
     *hql执行统计总数
     * @param hql
     * @param params
     * @return
     */
    Long countHql(String hql, Object... params);

    /**
     * 执行统计总数
     * @param sql
     * @param params
     * @return
     */
    Long countSql(String sql, Object... params);

    /**
     * 更新非空字段
     * @param bean
     * @return
     */
    int updateNotNull(T bean);

    /**
     * 根据字段删除数据
     * @param field
     * @param value
     * @return
     */
    int deleteEqualField(String field, Object value);

    /**
     * like匹配删除
     * @param field
     * @param value
     * @return
     */
    int deleteLikeField(String field, String value);

    /**
     * 相等查找
     * @param field 字段名
     * @param value 字段对应的值
     * @return
     */
    List<T> findEqualField(String field, Object value);

    /**
     * like匹配查找
     * @param field 字段名
     * @param value 字段对应的值
     * @return
     */
    List<T> findLikeField(String field, String value);

    /**
     * 根据字段进行between查询
     * @param field
     * @param start
     * @param end
     * @return
     */
    List<T> findBetweenField(String field, Object start, Object end);

    /**
     * 删除所有
     */
    int deleteAll();

    /**
     * 对bean对象的多个字段and的equal匹配
     * @param data
     * @return
     */
    List<T> findAndFields(Map<String, Object> data);

    /**
     * 对bean对象多个字段or的equal匹配
     * @param data
     * @return
     */
    List<T> findOrFields(Map<String, Object> data);

    /**
     * 获取一条记录方法
     * @param hql
     * @param clazz
     * @param <T2>
     * @return
     */
    <T2> T2 findOneHql(String hql, Class<T2> clazz, Object... params);

    /**
     * 返回小于等于一条结果时
     * @param sql
     * @param clazz
     * @param <T2>
     * @return
     */
    <T2> T2 findOneSql(String sql, Class<T2> clazz, Object... params);
}
