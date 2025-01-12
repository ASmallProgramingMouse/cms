package com.xzjie.cms.service;

import com.xzjie.cms.core.service.BaseService;
import com.xzjie.cms.dto.MenuResponse;
import com.xzjie.cms.dto.MenuRouter;
import com.xzjie.cms.dto.MenuTree;
import com.xzjie.cms.model.Menu;

import java.util.List;
import java.util.Set;


public interface MenuService extends BaseService<Menu> {
//
//    /**
//     * 查询全部数据
//     * @param criteria 条件
//     * @return /
//     */
//    List<MenuDTO> queryAll(MenuQueryCriteria criteria);
//
//    /**
//     * 根据ID查询
//     * @param id /
//     * @return /
//     */
//    MenuDTO findById(long id);
//
//    /**
//     * 创建
//     * @param resources /
//     * @return /
//     */
//    MenuDTO create(Menu resources);
//
//    /**
//     * 编辑
//     * @param resources /
//     */
//    void update(Menu resources);
//
//    /**
//     * 获取待删除的菜单
//     * @param menuList /
//     * @param menuSet /
//     * @return /
//     */
//    Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet);
//
    /**
     * 获取菜单树
     * @param pid /
     * @return /
     */
    List<MenuTree> getMenuTree(Long pid);

    List<MenuRouter> getMenuRouter(Set<String> roles);

    List<MenuResponse> getMenus();

    void delete(Set<Long> ids);
//
//    /**
//     * 根据pid查询
//     * @param pid /
//     * @return /
//     */
//    List<Menu> findByPid(long pid);
//
//    /**
//     * 构建菜单树
//     * @param menuDtos 原始数据
//     * @return /
//     */
//    Map<String,Object> buildTree(List<MenuDTO> menuDtos);
//
//    /**
//     * 根据角色查询
//     * @param roles /
//     * @return /
//     */
//    List<MenuDTO> findByRoles(List<RoleSmallDTO> roles);
//
//    /**
//     * 构建菜单树
//     * @param menuDtos /
//     * @return /
//     */
//    Object buildMenus(List<MenuDTO> menuDtos);
//
//    /**
//     * 根据ID查询
//     * @param id /
//     * @return /
//     */
//    Menu findOne(Long id);
//
//    /**
//     * 删除
//     * @param menuSet /
//     */
//    void delete(Set<Menu> menuSet);
//
//    /**
//     * 导出
//     * @param queryAll 待导出的数据
//     * @param response /
//     * @throws IOException /
//     */
//    void download(List<MenuDTO> queryAll, HttpServletResponse response) throws IOException;
}
