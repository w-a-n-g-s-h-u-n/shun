package org.wangshun.shun.db.base;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.wangshun.shun.core.http.R;
import org.wangshun.shun.db.base.entity.BaseDatabaseEntity;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import lombok.Getter;

public abstract class BaseController<S extends BaseService<M, T>, M extends BaseMapper<T>,
    T extends BaseDatabaseEntity<T>> {

    @Autowired
    @Getter
    protected S baseService;
    protected Class<?> entityClass = currentModelClass();

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity
     *            实体对象
     */
    R save(T entity) {
        return R.of(baseService.save(entity));
    }

    /**
     * 插入（批量）
     *
     * @param entityList
     *            实体对象集合
     */
    R saveBatch(Collection<T> entityList) {
        return R.of(baseService.saveBatch(entityList));
    }

    /**
     * 插入（批量）
     *
     * @param entityList
     *            实体对象集合
     * @param batchSize
     *            插入批次数量
     */
    R saveBatch(Collection<T> entityList, int batchSize) {
        return R.of(baseService.saveBatch(entityList, batchSize));
    }

    /**
     * 批量修改插入
     *
     * @param entityList
     *            实体对象集合
     */
    R saveOrUpdateBatch(Collection<T> entityList) {
        return R.of(baseService.saveOrUpdateBatch(entityList));
    }

    /**
     * 批量修改插入
     *
     * @param entityList
     *            实体对象集合
     * @param batchSize
     *            每次的数量
     */
    R saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return R.of(baseService.saveOrUpdateBatch(entityList, batchSize));
    }

    /**
     * 根据 ID 删除
     *
     * @param id
     *            主键ID
     */
    R removeById(Serializable id) {
        return R.of(baseService.removeById(id));
    }

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap
     *            表字段 map 对象
     */
    R removeByMap(Map<String, Object> columnMap) {
        return R.of(baseService.removeByMap(columnMap));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper
     *            实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R remove(Wrapper<T> queryWrapper) {
        return R.of(baseService.remove(queryWrapper));
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList
     *            主键ID列表
     */
    R removeByIds(Collection<? extends Serializable> idList) {
        return R.of(baseService.removeByIds(idList));
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity
     *            实体对象
     */
    R updateById(T entity) {
        return R.of(baseService.updateById(entity));
    }

    /**
     * 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
     *
     * @param updateWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     */
    R update(Wrapper<T> updateWrapper) {
        return R.of(baseService.update(updateWrapper));
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity
     *            实体对象
     * @param updateWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     */
    R update(T entity, Wrapper<T> updateWrapper) {
        return R.of(baseService.update(updateWrapper));
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList
     *            实体对象集合
     */
    R updateBatchById(Collection<T> entityList) {
        return R.of(baseService.updateBatchById(entityList));
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList
     *            实体对象集合
     * @param batchSize
     *            更新批次数量
     */
    R updateBatchById(Collection<T> entityList, int batchSize) {
        return R.of(baseService.updateBatchById(entityList, batchSize));
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity
     *            实体对象
     */
    R saveOrUpdate(T entity) {
        return R.of(baseService.saveOrUpdate(entity));
    }

    /**
     * <p>
     * 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法 此次修改主要是减少了此项业务代码的代码量（存在性验证之后的saveOrUpdate操作）
     * </p>
     *
     * @param entity
     *            实体对象
     */
    R saveOrUpdate(T entity, Wrapper<T> updateWrapper) {
        return R.of(baseService.saveOrUpdate(entity, updateWrapper));
    }

    /**
     * 根据 ID 查询
     *
     * @param id
     *            主键ID
     */
    R getById(Serializable id) {
        return R.success(baseService.getById(id));
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList
     *            主键ID列表
     */
    R listByIds(Collection<? extends Serializable> idList) {
        return R.success(baseService.listByIds(idList));
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap
     *            表字段 map 对象
     */
    R listByMap(Map<String, Object> columnMap) {
        return R.success(baseService.listByMap(columnMap));
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>
     * 结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
     * </p>
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R getOne(Wrapper<T> queryWrapper) {
        return R.success(baseService.getOne(queryWrapper));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx
     *            有多个 result 是否抛出异常
     */
    R getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        return R.success(baseService.getOne(queryWrapper, throwEx));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R getMap(Wrapper<T> queryWrapper) {
        return R.success(baseService.getMap(queryWrapper));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper
     *            转换函数
     */
    <V> R getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return R.success(baseService.getObj(queryWrapper, mapper));
    }

    /**
     * 查询总记录数
     *
     * @see Wrappers#emptyWrapper()
     */
    R count() {
        return R.success(baseService.count());
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R count(Wrapper<T> queryWrapper) {
        return R.success(baseService.count(queryWrapper));
    }

    /**
     * 查询列表
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R list(Wrapper<T> queryWrapper) {
        return R.success(baseService.list(queryWrapper));
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    R list() {
        return R.success(baseService.list());
    }

    /**
     * 翻页查询
     *
     * @param page
     *            翻页对象
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    <E extends IPage<T>> R page(E page, Wrapper<T> queryWrapper) {
        return R.success(baseService.page(page, queryWrapper));
    }

    /**
     * 无条件翻页查询
     *
     * @param page
     *            翻页对象
     * @see Wrappers#emptyWrapper()
     */
    <E extends IPage<T>> R page(E page) {
        return R.success(baseService.page(page));
    }

    /**
     * 查询列表
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R listMaps(Wrapper<T> queryWrapper) {
        return R.success(baseService.listMaps(queryWrapper));
    }

    /**
     * 查询所有列表
     *
     * @see Wrappers#emptyWrapper()
     */
    R listMaps() {
        return R.success(baseService.listMaps());
    }

    /**
     * 查询全部记录
     */
    R listObjs() {
        return R.success(baseService.listObjs());
    }

    /**
     * 查询全部记录
     *
     * @param mapper
     *            转换函数
     */
    <V> R listObjs(Function<? super Object, V> mapper) {
        return R.success(baseService.listObjs(mapper));
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    R listObjs(Wrapper<T> queryWrapper) {
        return R.success(baseService.listObjs(queryWrapper));
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper
     *            转换函数
     */
    <V> R listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return R.success(baseService.listObjs(queryWrapper, mapper));
    }

    /**
     * 翻页查询
     *
     * @param page
     *            翻页对象
     * @param queryWrapper
     *            实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    <E extends IPage<Map<String, Object>>> R pageMaps(E page, Wrapper<T> queryWrapper) {
        return R.success(baseService.pageMaps(page, queryWrapper));
    }

    /**
     * 无条件翻页查询
     *
     * @param page
     *            翻页对象
     * @see Wrappers#emptyWrapper()
     */
    <E extends IPage<Map<String, Object>>> R pageMaps(E page) {
        return R.success(baseService.pageMaps(page));
    }

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    BaseMapper<T> getBaseMapper(){
        return baseService.getBaseMapper();
    }

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return (Class<T>)ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }
}
