package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小林ikun
 * @since 2024-11-08
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "*")         // 全局跨源资源共享
public class UserController {

    @Autowired
    private UserService userService;    // 服务层接口，定义了与用户数据相关的业务逻辑方法。实际的业务逻辑实现通常在另一个类中

    @Autowired
    private MenuService menuService;    // 服务层接口，定义了与用户数据相关的业务逻辑方法。实际的业务逻辑实现通常在另一个类中


    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    // TODO 登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List<User> list = userService.lambdaQuery()
                .eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword()).list();

        // 设置我们的动态路由，由角色编码决定每个角色有多少权限
        if (list.size()>0){
            User user1 = (User) list.get(0);
//            System.out.println(user1);
            List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user", user1);
            res.put("menuList", menuList);
            return Result.suc(res);
        }


        return Result.fail();
    }

    // TODO 注册
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNo, user.getNo());
        List<User> list = userService.list(queryWrapper);

        if (!list.isEmpty()) {
            return Result.fail("用户已存在");
        }

        // 保存新用户
        boolean saved = userService.save(user);
        return saved ? Result.suc("注册成功") : Result.fail();
    }

    /**
     *  @RequestParam 用于从 HTTP 请求中提取请求参数，并将其绑定到控制器方法的参数上
     *  用于从 HTTP 请求的查询参数（query parameters）或表单数据（form data）中提取数据。
     */
    // TODO 验证账号的唯一性
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        // lambdaQuery:查询条件
        List<User> list = userService.lambdaQuery().eq(User::getNo, no).list();
        return !list.isEmpty()?Result.suc(list):Result.fail();
    }

    // @RequestParam：从请求的查询参数或表单数据中提取数据。
    // @RequestBody：从请求的正文中提取数据。

    /**
     *  @RequestBody 用于将 HTTP 请求的正文（body）绑定到控制器方法的参数上。
     *  适用于 POST、PUT 或 PATCH 请求，这些请求通常包含 JSON、XML 或其他格式的请求体数据。
     */
    // TODO 新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user) ? Result.suc() : Result.fail();
    }

    // TODO 修改1wms
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateById(user) ? Result.suc() : Result.fail();
    }

    // 修改2
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user){
        return userService.updateById(user);
    }

    // 新增或修改
    @PostMapping("/saveOrupdate")
    public boolean saveOrupdate(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    // TODO 删除1
    @GetMapping("/delete")
    public Result delete(@RequestParam String id){
        return userService.removeById(id) ? Result.suc() : Result.fail();
    }

    // 删除2
    @GetMapping("/del")
    public boolean del(Integer id){
        return userService.removeById(id);
    }

    // TODO 多删除1
    @GetMapping("/deletes")
    public Result deletes(@RequestParam List<Integer> ids) {
        return userService.removeByIds(ids) ? Result.suc() : Result.fail();
    }

    // TODO 查询（模糊、匹配） 使用手机号或者姓名作为模糊查询
    @PostMapping("/selects")
    public Result selects(@RequestBody User user){
        // 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(user.getName()) || StringUtils.isNotBlank(user.getPhone())){
        /*
            添加模糊查询条件，查询用户名称包含 user.getName() 的用户
            调用 like 方法，用于添加模糊查询条件
            第一个参数：User 类的 getName 方法引用，表示要查询的字段
            第二个参数：从 user 对象中获取的名称，作为模糊查询的条件
         */
            lambdaQueryWrapper
                    .like(User::getName, user.getName())
                    .or()
                    .like(User::getPhone, user.getPhone());
        }

        return Result.suc(userService.list(lambdaQueryWrapper));
    }


    // 分页
    @PostMapping("/listPage")          // HashMap map
    public List<User> listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();   // 调用封装方法
        /*System.out.println("num===" + query.getPageNum());
        System.out.println("size===" + query.getPageSize());

        HashMap param = query.getParam();
        System.out.println("name===" + (String) param.get("name"));
        System.out.println("city===" + (String) param.get("city"));*/

        String name = (String) param.get("name");   // 转换成字符串类型

        Page<User> page = new Page<User>();     // 创建分页对象
        page.setCurrent(query.getPageNum());    // 设置当前页码
        page.setSize(query.getPageSize());      // 设置每页大小

        // 创建查询条件
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, name);
        // 执行分页查询
        IPage<User> result = userService.page(page, lambdaQueryWrapper);

        System.out.println("total===" + result.getTotal());

        return result.getRecords();
    }



    // 自定义分页
    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String) param.get("name");

        Page<User> page = new Page<User>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName, name);

        /**
         * 先创建到 UserService → UserServiceImpl → UserMapper → UserMapper.xml
         */
        //IPage<User> result = userService.pageC(page);
        IPage<User> result = userService.pageCc(page, lambdaQueryWrapper);

        System.out.println("total===" + result.getTotal());

        return result.getRecords();
    }


    // TODO 自定义分页 + 查询 + 响应码
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String) param.get("name");
        String phone = (String) param.get("phone");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");

        Page<User> page = new Page<User>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

//        检查 name 是否为空或仅包含空白字符
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }

        if (StringUtils.isNotBlank(sex)) {
            lambdaQueryWrapper.like(User::getSex, sex);
        }

        if (StringUtils.isNotBlank(phone)) {
            // 检查当前的查询条件是否已经包含了 "or" 关键字
            // 如果已经包含了 "or"，则需要再添加一个 "or" 条件
            if (lambdaQueryWrapper.getSqlSegment().contains("or")) {
                lambdaQueryWrapper.or();
            }
            lambdaQueryWrapper.like(User::getPhone, phone);
        }

        if (StringUtils.isNotBlank(roleId)) {
            lambdaQueryWrapper.like(User::getRoleId, roleId);
        }



        IPage<User> result = userService.pageCc(page, lambdaQueryWrapper);

//        System.out.println("total===" + result.getTotal());

        return Result.suc(result.getRecords(), result.getTotal());
    }
}
