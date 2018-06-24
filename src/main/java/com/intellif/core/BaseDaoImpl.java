package com.intellif.core;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/***
**create by yinchong
*/
public class BaseDaoImpl<T> implements BaseDao<T> {

    private Class<T> clazz;

    public BaseDaoImpl() {
        ParameterizedType t = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获取泛型参数的实际类型
        this.clazz = (Class<T>) t.getActualTypeArguments()[0];
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    @Transactional(propagation = Propagation.REQUIRED)
    public void save(T bean) {
       Field id =  ReflectUtils.getIdField(bean.getClass());
       Object value = null;
        try {
            value = id.get(bean);
        } catch (IllegalAccessException e) {
           throw new RuntimeException(e);
        }
        if(value==null) {
           entityManager.persist(bean);
       }else{
           update(bean);
       }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(T bean) {
        entityManager.merge(bean);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public T findOne(Serializable id) {
        return entityManager.find(clazz, id);
    }


    /**
     * @param id
     * @return 小于0 表示删除失败 大与0表示删除成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int delete(Serializable id) {
        String hql = "delete from " + clazz.getSimpleName() + " p where p."+getIdProperties(clazz)+" = ?1";
        Query query = entityManager.createQuery(hql);
        query.setParameter(1, id);
        return query.executeUpdate();
    }

    private String getIdProperties(Class clazz){
       Field field =  ReflectUtils.getIdField(clazz);
       return field.getName();
    }


    /**
     * 显示所有数据
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAll() {
        String hql = "select t from " + clazz.getSimpleName() + " t";
        Query query = entityManager.createQuery(hql,clazz);
        List<T> beans = query.getResultList();
        return beans;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> pageList(int pageNum, int pageSize) {
        String hql = "select t from " + clazz.getSimpleName() + " t";
        Query query = entityManager.createQuery(hql,clazz);
        limitDatas(pageNum,pageSize,query);
        List<T> beans = query.getResultList();
        return beans;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAllDesc() {
        String id = getIdProperties(clazz);
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+id+" desc";
        return findHql(hql,clazz);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> pageListDesc(int pageNum, int pageSize) {
        String id = getIdProperties(clazz);
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+id+" desc";
        Query query = entityManager.createQuery(hql,clazz);
        limitDatas(pageNum,pageSize,query);
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAllDesc(String fieldName) {
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+fieldName+" desc";
        return findHql(hql,clazz);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> pageListDesc(String fieldName, int pageNum, int pageSize) {
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+fieldName+" desc";
        Query query = entityManager.createQuery(hql,clazz);
       limitDatas(pageNum,pageSize,query);
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAllAsc() {
        String id = getIdProperties(clazz);
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+id+" asc";
        return findHql(hql,clazz);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> pageListAsc(int pageNum, int pageSize) {
        if(pageNum<=0)
            pageNum=1;
        String id = getIdProperties(clazz);
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+id+" asc";
        Query query = entityManager.createQuery(hql,clazz);
        query.setFirstResult((pageNum-1)*pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAllAsc(String fieldName) {
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+fieldName+" asc";
        return findHql(hql,clazz);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> pageListAsc(String fieldName, int pageNum, int pageSize) {
        if(pageNum<=0)
            pageNum=1;
        String hql = "select t from " + clazz.getSimpleName() + " t order by t."+fieldName+" asc";
        Query query = entityManager.createQuery(hql,clazz);
        query.setFirstResult((pageNum-1)*pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }


    /**
     * 批量插入
     * @param beans
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchSave(List<T> beans) {
        int count =0;
        if (beans != null) {
            for (T bean : beans) {
                count++;
                entityManager.persist(bean);
                if(count%1000==0){
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();
        }

    }


    /**
     * 批量更新
     * @param beans
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchUpdate(List<T> beans) {
        if (beans != null) {
            for (T bean : beans)
                entityManager.merge(bean);
            entityManager.flush();
            entityManager.clear();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public int batchDelete(List<? extends Serializable> ids) {
        StringBuffer hql = new StringBuffer("delete from " + clazz.getSimpleName() + " p where p."+getIdProperties(clazz)+"  in(:ids)");
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("ids", ids);
        return query.executeUpdate();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchUpdateNotNull(List<T> beans) {
        if (beans == null || beans.size() == 0)
            return;
            List<Map<String, Object>> datas = new LinkedList<>();
            for (T bean : beans) {
                Map<String, Object> data = ReflectUtils.createMapForNotNull(bean);
                datas.add(data);
            }
            String id = getIdProperties(clazz);

            for (Map<String, Object> map : datas) {
                //拼接Hql语句
                StringBuffer hql = new StringBuffer("update " + clazz.getSimpleName() + "");
                Set<String> keys = map.keySet();
                boolean fist = true;
                for (String key : keys) {
                    if (key.equals(id))
                        continue;
                    if (fist) {
                        hql.append(" set ").append(key + "=:" + key);
                        fist = false;
                    } else {
                        hql.append("," + key + "=:" + key);
                    }
                }
                hql.append(" where "+id+"=:"+id);
                Query query = entityManager.createQuery(hql.toString());

                //设置参数
                for (String key : keys) {
                    Object value = map.get(key);
                    query.setParameter(key, value);
                }
                query.executeUpdate();
            }
            entityManager.flush();
            entityManager.clear();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int executeSql(String sql, Object... params) {
        Query query = createSqlQuery(sql, params);
        return query.executeUpdate();
    }

    /**
     * 生成Query对象
     * @param sql
     * @param params
     * @return
     */
    private Query createSqlQuery(String sql, Object[] params) {
        Map<String, Object> map = getSql(sql);
        int count = (int) map.get("count");
        Query query = entityManager.createNativeQuery(sql);
        setQueryParameters(count,query,params);
        return query;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int executeHql(String hql, Object... params) {
        Query query = createHqlQuery(hql, params);
        return query.executeUpdate();
    }

    /**
     * 生成Hql可执行的query对象
     * @param hql
     * @param params
     * @return
     */
    private Query createHqlQuery(String hql, Object[] params) {
        Map<String, Object> map = getHSql(hql);
        String jpaHql = (String) map.get("sql");
        int count = (int) map.get("count");
        Query query = entityManager.createQuery(jpaHql);
        setQueryParameters(count,query,params);
        return query;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> findSql(String sql, Object... params) {
        List<Map<String,Object>> list = new LinkedList<>();
        List<String> labels = new LinkedList<>();
        jdbcTemplate.query(sql, params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                ResultSetMetaData rsd =  resultSet.getMetaData();
                int count = rsd.getColumnCount();
               if(labels.size()==0){
                  for(int i=1;i<=count;i++){
                     labels.add(rsd.getColumnLabel(i));
                  }
               }
               Map<String,Object> map = new HashMap<>();
               for(int i=1;i<=count;i++){
                   map.put(labels.get(i-1),resultSet.getObject(i));
               }
               list.add(map);
            }
        });
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T1> List<T1> findSql(String sql, Class<T1> clazz, Object... params) {
        if(ReflectUtils.isSimpleType(clazz)){
            return jdbcTemplate.queryForList(sql,params,clazz);
        }
        List<String> lables = new LinkedList<>();
        Map<String,String> propertiesName = listPropertiesName(clazz);
         List<T1> result = jdbcTemplate.query(sql, params, new RowMapper<T1>() {
           @Override
           public T1 mapRow(ResultSet resultSet, int j) throws SQLException {
              ResultSetMetaData rsd =  resultSet.getMetaData();
              int count = rsd.getColumnCount();
              if(lables.size()==0){
                  for(int i=1;i<=count;i++){
                     String name = rsd.getColumnLabel(i);
                     lables.add(propertiesName.get(name));
                  }
              }
              Map<String,Object> temp = new HashMap<>();
              for(int i=1;i<=count;i++){
                  temp.put(lables.get(i-1),resultSet.getObject(i));
              }
               return ReflectUtils.changeMapToBean(temp,clazz);
           }
       });
      return result;
    }



    private Map<String,String> listPropertiesName(Class clazz){
        List<Field> fields = ReflectUtils.listAllFields(clazz);
        Map<String,String> result = new HashMap<>();
        for(Field field:fields){
            String value = field.getName();
            String key = value;
           Column column =  field.getAnnotation(Column.class);
           if(column!=null){
               key = column.name();
           }
           result.put(key,value);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Object[]> findObjectSql(String sql, Object... params) {
        Map<String, Object> map = getSql(sql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        Query query = entityManager.createNativeQuery(jpaSql);
        setQueryParameters(count,query,params);
        return query.getResultList();
    }

    /**
     * 获取数据库类型
     * @return
     */
    private String getDatabaseProductName(){
        try {
          return   jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     *
     * @param sql
     * @param pageNum  第几页 从1开始
     * @param pageSize 每页显示多少个
     * @param params   需要传递的参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageBean<Map<String, Object>> pageSql(String sql, Integer pageNum, Integer pageSize, Object... params) {
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        Map<String, Object> map = getSql(sql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        Long totalCount = getCount(sql, count, params);
        if(totalCount==null||totalCount==0){
            pageBean.setTotalCount(0L);
            return pageBean;
        }
        pageBean.setTotalCount(totalCount);
        int start = (pageNum-1)*pageSize;
        int end = (pageNum)*pageSize;
        String dataName = getDatabaseProductName();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>dataName:"+dataName);
        switch (dataName){
            case "mysql":
                sql+=" limit "+start+", "+end;
                break;
            case "oracle":
                break;
        }
        List<Map<String,Object>> data = findSql(sql,params);
        pageBean.setDatas(data);
        return pageBean;
    }

    /**
     * 分页查找
     * @param sql
     * @param clazz
     * @param pageNum
     * @param pageSize
     * @param params
     * @param <T2>
     * @return
     */
    public<T2> PageBean<T2> pageSql(String sql,Class<T2> clazz,int pageNum,int pageSize,Object... params){
        PageBean<T2> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        Map<String, Object> map = getSql(sql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        Long totalCount = getCount(sql, count, params);
        if(totalCount==null||totalCount==0){
            pageBean.setTotalCount(0L);
            return pageBean;
        }
        pageBean.setTotalCount(totalCount);
        sql = getLimitSql(sql, pageNum, pageSize);
        List<T2> data = findSql(sql,clazz,params);
        pageBean.setDatas(data);
        return pageBean;
    }

    /**
     * 获取不同环境下分页语句
     * @param sql
     * @param pageNum
     * @param pageSize
     * @return
     */
    private String getLimitSql(String sql, int pageNum, int pageSize) {
        int start = (pageNum-1)*pageSize;
        int end = (pageNum)*pageSize;
        String dataName = getDatabaseProductName();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>dataName:"+dataName);
        switch (dataName){
            case "mysql":
                sql+=" limit "+start+", "+end;
                break;
            case "oracle":
                sql+=" and rn between "+start+" and "+end;
                break;
            case "sqlserver":
                sql+=" offset "+start+" rows fetch next "+end+" rows only";
                break;
            case "postgresql":
                sql+=" limit "+start+" offset "+end;
                break;
        }
        return sql;
    }

    private Long getCount(String sql, Integer count, Object... params) {
        if (params != null) {
            if (count != params.length)
                throw new RuntimeException("参数数量和？不一致");
        }
        int end = sql.lastIndexOf("order by");
        if(end<=0){
            end = sql.lastIndexOf("ORDER BY");
        }
        String tempSql = sql;
        if(end>0){
            tempSql = tempSql.substring(0,end);
        }
        tempSql ="select count(1) from (" +tempSql+") as temp";
        return countSql(tempSql,params);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageBean<Object[]> pageObjectSql(String sql, int pageNum, int pageSize, Object... params) {
        PageBean<Object[]> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        Map<String, Object> map = getSql(sql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        Long totalCount = getCount(sql, count, params);
        if(totalCount==null||totalCount==0){
            pageBean.setTotalCount(0L);
            return pageBean;
        }
        pageBean.setTotalCount(totalCount);
        Query query = entityManager.createNativeQuery(jpaSql);
        limitDatas(pageNum,pageSize,query);
        if (params != null && params.length > 0) {
            int length = params.length;
            for (int i = 0; i < length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        List<Object[]> datas =query.getResultList();
        pageBean.setDatas(datas);
        return pageBean;
    }

    private Map<String, Object> getSql(String sql) {
        sql += " ";
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sql", sql);
        map.put("count", count);
        String[] datas = sql.split("\\?");
        int length = datas.length;
        if (length == 1)
            return map;
        for (int i = 0; i < length - 1; i++) {
            count++;
        }
        map.put("count", count);
        return map;
    }

    private Map<String, Object> getHSql(String sql) {
        sql += " ";
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("sql", sql);
        String[] datas = sql.split("\\?");
        int length = datas.length;
        if (length == 1)
            return map;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - 1; i++) {
            count++;
            sb.append(datas[i]).append("?" + count).append(" ");
        }
        sb.append(datas[length-1]);
        map.put("count", count);
        map.put("sql", sb.toString());
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public<T2> List<T2> findHql(String hql,Class<T2> clazz, Object... params) {
        Map<String, Object> map = getHSql(hql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        //获取数据
        Query query = entityManager.createQuery(jpaSql,clazz);
        setQueryParameters(count,query,params);
        List<T2> records =  query.getResultList();
        return records;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Object[]> findObjectHql(String hql, Object... params) {
        Map<String, Object> map = getHSql(hql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        //获取数据
        Query query = entityManager.createQuery(jpaSql);
        setQueryParameters(count,query,params);
        List<Object[]> records =  query.getResultList();
        return records;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public<T2> PageBean<T2> pageHql(String hql,Class<T2> clazz, Integer pageNum, Integer pageSize, Object... params) {

        PageBean<T2> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        Map<String, Object> map = getHSql(hql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        Long totalCount = getHCount(jpaSql, count, params);
        if(totalCount==null||totalCount==0){
            pageBean.setTotalCount(0L);
            return pageBean;
        }
        pageBean.setTotalCount(totalCount);

        //获取数据
        Query query = entityManager.createQuery(jpaSql,clazz);
        if (params != null && params.length > 0) {
            int length = params.length;
            for (int i = 0; i < length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        limitDatas(pageNum, pageSize, query);
        List<T2> records =  query.getResultList();
        pageBean.setDatas(records);
        return pageBean;
    }

    /**
     * 执行count操作
     * @param jpaSql
     * @param count
     * @param params
     * @return
     */
    private Long getHCount(String jpaSql, Integer count, Object... params) {
        if (params != null) {
            if (params.length != count) {
                throw new RuntimeException("sql 语句和参数数量不一致");
            }
        }
        //执行count操作
        boolean flag = jpaSql.startsWith("select");
        int end = jpaSql.lastIndexOf("order by");
        if(end<=0){
            end = jpaSql.lastIndexOf("ORDER BY");
        }
        String tempHql =jpaSql;
        if(flag) {
            if (end > 0) {
                tempHql = tempHql.substring(0, end);
            }
           //找到第一from
            int index = tempHql.indexOf("from");
            tempHql=tempHql.substring(index);
            tempHql = "select count(id) "+tempHql;
        }else{
            tempHql="select count(id) "+tempHql;
        }

        return countHql(tempHql,params);
    }

    /**
     * 数据分页
     * @param pageNum
     * @param pageSize
     * @param query
     */
    private void limitDatas(Integer pageNum, Integer pageSize, Query query) {
        if(pageNum!=null&&pageSize!=null){
            if(pageNum<=0)
                pageNum=1;
            query.setFirstResult((pageNum-1)*pageSize);
            query.setMaxResults(pageSize);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countHql(String hql, Object... params) {
        return findOneHql(hql,Long.class,params);
    }

    /**
     * 为Query设置参数
     * @param count
     * @param query
     * @param params
     */
    private void setQueryParameters(Integer count, Query query, Object[] params) {
        if (params != null) {
            if (params.length != count) {
                throw new RuntimeException("sql 语句和参数数量不一致");
            }
            int length = params.length;
            for (int i = 0; i < length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countSql(String sql, Object... params) {
        return  jdbcTemplate.queryForObject(sql,params,Long.class);
    }

    @Override
    public int updateNotNull(T bean) {
        try {
            batchUpdateNotNull(Arrays.asList(bean));
        }catch (Exception e){
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:error:"+e.getMessage()+""+e.getCause());
            return -1;
        }
         return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteEqualField(String field, Object value) {
        Query query = entityManager.createQuery("delete from " + clazz.getSimpleName() + " p where p."+field+" = ?1");
        query.setParameter(1, value);
        return query.executeUpdate();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteLikeField(String field,String value){
        Query query = entityManager.createQuery("delete from " + clazz.getSimpleName() + " p where p."+field+" like ?1");
        query.setParameter(1, value);
        return query.executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findEqualField(String field, Object value) {
        String hql = "select p from "+clazz.getSimpleName()+" p where p."+field+" = ?";
        return findHql(hql,clazz,value);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findLikeField(String field, String value) {
        String hql = "select p from "+clazz.getSimpleName()+" p where p."+field+" like ?";
        return findHql(hql,clazz,value);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findBetweenField(String field, Object start, Object end) {
        String hql = "select p from "+clazz.getSimpleName()+" p where p."+field+" between ? and ?";
        return findHql(hql,clazz,start,end);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteAll() {
        String hql = "delete from " + clazz.getSimpleName() + " p ";
        return executeHql(hql);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAndFields(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("select p from "+clazz.getSimpleName()+" p ");
        if(data!=null){
            boolean flag = true;
           for(String key:data.keySet()){
               if(flag){
                   sb.append(" where ").append(key).append(" =:").append(key);
                   flag = false;
               }else{
                   sb.append(" and ").append(key).append(" =:").append(key);
               }
           }
        }
      Query query =  entityManager.createQuery(sb.toString());
        setQueryParamters(data, query);
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findOrFields(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("select p from "+clazz.getSimpleName()+" p ");
        if(data!=null){
            boolean flag = true;
            for(String key:data.keySet()){
                if(flag){
                    sb.append(" where ").append(key).append(" =:").append(key);
                    flag = false;
                }else{
                    sb.append(" or ").append(key).append(" =:").append(key);
                }
            }
        }
        Query query =  entityManager.createQuery(sb.toString());
        setQueryParamters(data, query);
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T2> T2 findOneHql(String hql, Class<T2> clazz,Object... params) {
        Map<String, Object> map = getHSql(hql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        //获取数据
        Query query = entityManager.createQuery(jpaSql,clazz);
        setQueryParameters(count,query,params);
        return (T2) query.getSingleResult();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T2> T2 findOneSql(String sql, Class<T2> clazz,Object... params) {
        return jdbcTemplate.queryForObject(sql,params,clazz);
    }

    @Override
    public List<Map<String,Object>> findWrapper(Wrapper wrapper) {
        if(wrapper instanceof HQLWrapper){
            Map<String, Object> map = getHSql(wrapper.getQuery());
            String jpaSql = (String) map.get("sql");
            Integer count = (Integer) map.get("count");
            //获取数据
            Query query = entityManager.createQuery(jpaSql);
            setQueryParameters(count,query,wrapper.getParams().toArray());
            List<Object[]> datas =  query.getResultList();
            HQLWrapper hqlWrapper = (HQLWrapper) wrapper;
            List<Map<String,Object>> result = new LinkedList<>();
            if(datas!=null&&datas.size()>0){
                Map<String,Object> temp = new HashMap<>();
                int len = hqlWrapper.getArgs().size();
                for(Object[] data:datas) {
                    for (int i = 0; i < len; i++) {
                        temp.put((String) hqlWrapper.getArgs().get(i),data[i]);
                    }
                    result.add(temp);
                }
            }
            return result;
        }else if(wrapper instanceof SQLWrapper){
          return findSql(wrapper.getQuery(),wrapper.getParams().toArray());
        }
        return null;
    }

    @Override
    public <T2> List<T2> findWrapper(Wrapper wrapper, Class<T2> clazz) {
        if(wrapper instanceof HQLWrapper){
            return findHql(wrapper.getQuery(),clazz,wrapper.getParams().toArray());
        }else if(wrapper instanceof SQLWrapper){
             return findSql(wrapper.getQuery(),clazz,wrapper.getParams().toArray());
        }
        return null;
    }

    private void setQueryParamters(Map<String, Object> data, Query query) {
        if(data!=null){
            for(Map.Entry<String,Object> entry:data.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                query.setParameter(key,value);
            }
        }
    }


}

