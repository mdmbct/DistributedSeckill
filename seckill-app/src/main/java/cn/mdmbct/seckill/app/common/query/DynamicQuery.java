package cn.mdmbct.seckill.app.common.query;

import java.util.List;

/**
 * JPA动态查询
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 9:42
 * @modified mdmbct
 * @since 0.1
 */
public interface DynamicQuery {

    void save(Object entity);

    void update(Object entity);

    <T> void delete(Class<T> entityClass, Object entityId);

    <T> void delete(Class<T> entityClass, Object[] entityIds);


    /**
     * 查询对象列表，返回List
     *
     * @param nativeSql sql
     * @param params    sql参数
     * @return List<T> 查询结果
     */
    <T> List<T> nativeQueryList(String nativeSql, Object... params);

    /**
     * 查询对象列表，返回List<Map<key,value>>
     *
     * @param nativeSql sql
     * @param params    sql参数
     * @return List<T>
     */
    <T> List<T> nativeQueryListMap(String nativeSql, Object... params);

    /**
     * 查询对象列表，返回List<组合对象>
     *
     * @param resultClass 返回类型
     * @param nativeSql   sql
     * @param params      sql参数
     * @return List<T>
     */
    <T> List<T> nativeQueryListModel(Class<T> resultClass, String nativeSql, Object... params);

    /**
     * 执行nativeSql统计查询
     *
     * @param nativeSql sql
     * @param params    sql参数    占位符参数(例如?1)绑定的参数值
     * @return 统计条数
     */
    Object nativeQueryObject(String nativeSql, Object... params);

    /**
     * 执行nativeSql统计查询
     *
     * @param nativeSql sql
     * @param params    sql参数    占位符参数(例如?1)绑定的参数值
     * @return 统计条数
     */
    Object[] nativeQueryArray(String nativeSql, Object... params);

    /**
     * 执行nativeSql的update,delete操作
     *
     * @param nativeSql sql
     * @param params    sql参数
     * @return 更新数量
     */
    int nativeExecuteUpdate(String nativeSql, Object... params);
}
