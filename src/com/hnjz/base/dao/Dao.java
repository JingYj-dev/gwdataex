package com.hnjz.base.dao;

import java.util.List;
import java.util.Map;

/**
 * @author ：JingYj
 * @date ：2020/7/2
 * @version: V1.0.0
 */
public interface Dao {

    /**
     * 根据Id和Class获取一个对象
     *
     * @param clazz 要获取对象的class
     * @param id    要获取对象的主键
     * @return 对象
     */
    Object get(Class<?> clazz, String id);

    /**
     * 保存一个对象
     *
     * @param object 要保存的对象
     * @return
     */
    Object save(Object object);

    /**
     * 批量保存或更新对象
     *
     * @param object 保存或更新对象
     */
    void save(List<? extends Object> object);

    /**
     * 修改或添加对象
     */
    Object merge(Object object);

    /**
     * 根据Id和class删除一个对象
     *
     * @param clazz 要删除对象的class
     * @param id    要删除对象的主键
     */
    void remove(Class<?> clazz, String id);

    /**
     * 删除传入的对象
     *
     * @param object 要删除的对象
     */
    void remove(Object object);

    /**
     * 批量删除传入的对象
     *
     * @param objs 要删除的对象列表
     */
    void remove(List<? extends Object> objs);

    /**
     * 删除根据hsql查询出来的东西出来的东西，例如：<br>
     * String hsql = "from User";
     * <br>
     * dao.find(hsql);
     *
     * @param hsql hsql语句
     * @return 结果列表
     */
    void removeFindObjs(String hsql, Object... canshu);

    /**
     * 使用hsql查询，例如：<br>
     * String hsql = "from User";
     * <br>
     * dao.find(hsql);
     *
     * @param hsql hsql语句
     * @return 结果列表
     */
    <T> List<T> find(String hsql);

    /**
     * 使用hsql查询，例如：<br>
     * String hsql = "from User where name = ?";
     * <br>
     * dao.find(hsql,"张三");
     * <br>
     * String hsql = "from User where name = ? and sex = ?";
     * <br>
     * dao.find(hsql,"张三","男");
     * <br>
     * hsql 为 "from XX ..." 返回XX对象的类表，List<XX>
     * <br>
     * "select X,Y from Z ..",返回 List<Object[]>
     * <br>
     * "select X from Z ..",返回 List<Object>
     *
     * @param hsql   hsql语句
     * @param canshu hsql查询条件的参数，多个用逗号隔开，可以为null
     * @return 结果列表
     */
    <T> List<T> find(String hsql, Object... canshu);

    /**
     * 函数介绍：使用sql查询
     * <p>
     * 输入参数：sql语句
     * <p>
     * 返回值：List<Object[]>
     */
    public <T> List<T> findBySql(final String sql);

    /**
     * 使用sql查询，例如：<br>
     * String sql = "select * from T_DATA_FILE";
     * <br>
     * dao.findBySql(sql);
     *
     * @param sql sql语句
     * @return 结果列表  List<Object[]>
     */
    public <T> List<T> findBySql(String sql, Object... canshu);


    /**
     * 执行原生sql，保存或更新、删除等操作
     *
     * @param sql
     * @return
     */
    public boolean execute(String sql);

    /**
     * 根据表名封装数据
     * @throws SQLException
     */

    /**
     * 执行update、insert、delete 语句
     *
     * @param sql
     * @param condition
     * @return
     */
    boolean execute(String sql, List<Object> condition);

    /**
     * 修改一个对象
     *
     * @param object 要保存的对象
     * @return
     */
    Object update(Object object);

}
