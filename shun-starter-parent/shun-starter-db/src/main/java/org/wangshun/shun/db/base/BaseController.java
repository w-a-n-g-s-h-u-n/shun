package org.wangshun.shun.db.base;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.wangshun.shun.core.http.R;
import org.wangshun.shun.db.base.entity.BaseDatabaseEntity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.Map;

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
    protected R save(T entity) {
        return R.of(baseService.save(entity));
    }

    /**
     * 插入（批量）
     *
     * @param entityList
     *            实体对象集合
     */
    protected R saveBatch(Collection<T> entityList) {
        return R.of(baseService.saveBatch(entityList));
    }

    /**
     * 批量修改插入
     *
     * @param entityList
     *            实体对象集合
     */
    protected R saveOrUpdateBatch(Collection<T> entityList) {
        return R.of(baseService.saveOrUpdateBatch(entityList));
    }

    /**
     * 根据 ID 删除
     *
     * @param id
     *            主键ID
     */
    protected R removeById(Serializable id) {
        return R.of(baseService.removeById(id));
    }

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap
     *            表字段 map 对象
     */
    protected R removeByMap(Map<String, Object> columnMap) {
        return R.of(baseService.removeByMap(columnMap));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param entity
     *            实体对象
     */
    protected R remove(T entity) {
        return R.of(baseService.remove(Wrappers.query(entity)));
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList
     *            主键ID列表
     */
    protected R removeByIds(Collection<? extends Serializable> idList) {
        return R.of(baseService.removeByIds(idList));
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity
     *            实体对象
     */
    protected R updateById(T entity) {
        return R.of(baseService.updateById(entity));
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList
     *            实体对象集合
     */
    protected R updateBatchById(Collection<T> entityList) {
        return R.of(baseService.updateBatchById(entityList));
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity
     *            实体对象
     */
    protected R saveOrUpdate(T entity) {
        return R.of(baseService.saveOrUpdate(entity));
    }

    /**
     * 根据 ID 查询
     *
     * @param id
     *            主键ID
     */
    protected R getById(Serializable id) {
        return R.success(baseService.getById(id));
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList
     *            主键ID列表
     */
    protected R listByIds(Collection<? extends Serializable> idList) {
        return R.success(baseService.listByIds(idList));
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap
     *            表字段 map 对象
     */
    protected R listByMap(Map<String, Object> columnMap) {
        return R.success(baseService.listByMap(columnMap));
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>
     * 结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
     * </p>
     * 
     * @param entity
     *            实体对象
     */
    protected R getOne(T entity) {
        return R.success(baseService.getOne(Wrappers.query(entity)));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param entity
     *            实体对象
     * @param throwEx
     *            有多个 result 是否抛出异常
     */
    protected R getOne(T entity, boolean throwEx) {
        return R.success(baseService.getOne(Wrappers.query(entity), throwEx));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param entity
     *            实体对象
     */
    protected R getMap(T entity) {
        return R.success(baseService.getMap(Wrappers.query(entity)));
    }

    /**
     * 查询总记录数
     *
     */
    protected R count() {
        return R.success(baseService.count());
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param entity
     *            实体对象
     */
    protected R count(T entity) {
        return R.success(baseService.count(Wrappers.query(entity)));
    }

    /**
     * 查询列表
     *
     * @param entity
     *            实体对象
     */
    protected R list(T entity) {
        return R.success(baseService.list(Wrappers.query(entity)));
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    protected R list() {
        return R.success(baseService.list());
    }

    /**
     * 翻页查询
     *
     * @param page
     *            翻页对象
     * @param condition
     *            实体对象
     */
    protected R page(PageCondition page,T condition) {
        return R.success(baseService.page(page.toPage(), Wrappers.query(condition)));
    }

    /**
     * 无条件翻页查询
     *
     * @param page
     *            翻页对象
     * @see Wrappers#emptyWrapper()
     */
    protected R page(Page<T> page) {
        return R.success(baseService.page(page));
    }

    /**
     * 查询列表
     *
     * @param entity
     *            实体对象
     */
    protected R listMaps(T entity) {
        return R.success(baseService.listMaps(Wrappers.query(entity)));
    }

    /**
     * 查询所有列表
     *
     * @see Wrappers#emptyWrapper()
     */
    protected R listMaps() {
        return R.success(baseService.listMaps());
    }

    /**
     * 查询全部记录
     */
    protected R listObjs() {
        return R.success(baseService.listObjs());
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param entity
     *            实体对象
     */
    protected R listObjs(T entity) {
        return R.success(baseService.listObjs(Wrappers.query(entity)));
    }

    /**
     * 翻页查询
     *
     * @param page
     *            翻页对象
     * @param entity
     *            实体对象
     */
    protected R pageMaps(Page<Map<String, Object>> page, T entity) {
        return R.success(baseService.pageMaps(page, Wrappers.query(entity)));
    }

    /**
     * 无条件翻页查询
     *
     * @param page
     *            翻页对象
     * @see Wrappers#emptyWrapper()
     */
    protected R pageMaps(Page<Map<String, Object>> page) {
        return R.success(baseService.pageMaps(page));
    }

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    protected BaseMapper<T> getBaseMapper() {
        return baseService.getBaseMapper();
    }

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return (Class<T>)ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }
}
