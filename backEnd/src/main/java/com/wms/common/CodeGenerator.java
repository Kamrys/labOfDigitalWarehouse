package com.wms.common;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {
    /**
     * 读取控制台内容
     *
     * @param tip 提示信息，告诉用户需要输入什么内容
     * @return 用户输入的字符串
     * @throws MybatisPlusException 如果用户输入为空或未输入任何内容，则抛出异常
     */
    public static String scanner(String tip){
        Scanner scanner = new Scanner(System.in);
//        用于拼接提示信息
        StringBuffer help = new StringBuffer();
//        help.append("请输入" + tip + ": ");
        // 拼接提示信息 ↕
        help.append("请输入").append(tip).append(": ");
        System.out.println(help.toString());

        if (scanner.hasNext()){     // 检查是否有可用的输入
            // 读取用户输入的下一个字符串
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)){   // 检查输入的字符串是否非空
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "! ");
    }

     /**
     * 操作步骤：
     * 1.修改数据源包括地址密码信息，对应代码标记：⼀、 下同
     * 2.模块配置，可以修改包名
     * 3.修改模板（这步可忽略）
     * @param args
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir")+"/wms";     // 获取项目的根路径，并拼接目标输出目录
        gc.setOutputDir(projectPath + "/src/main/java");                // 设置全局配置的输出目录
        gc.setAuthor("小林ikun");            // 设置作者信息
        gc.setOpen(false);                  // 生成完成后是否打开输出目录
        gc.setSwagger2(true);               // 实体属性 Swagger2 注解
        gc.setBaseResultMap(true);          // 是否生成 XML 的 ResultMap
        gc.setBaseColumnList(true);         // 是否生成 XML 的 columnList
        // 去掉service接口首字母的I，如DO为User则叫UserService
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);            // 将全局配置应用到代码生成器


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // TODO 一、修改数据源，连接到 MySQL 数据库
        dsc.setUrl("jdbc:mysql://192.168.1.11:3306/wms?serverTimezone=UTC");
//        dsc.setSchemaName("public");      // 设置数据库的 Schema 名称（可选，适用于某些数据库）
        dsc.setDriverName("com.mysql.jdbc.Driver");     // 设置数据库驱动名称
        dsc.setUsername("root");    // 数据库用户名
        dsc.setPassword("admin");   // 数据库密码
        // 将数据源配置应用到代码生成器
        mpg.setDataSource(dsc);


        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));     // 从控制台输入模块名（可选）
        // TODO 二、模块配置
        pc.setParent("com.wms")                 // 设置父包名
                .setEntity("entity")            // 设置实体类包名
                .setMapper("mapper")            // 设置 Mapper 接口包名
                .setService("service")          // 设置 Service 接口包名
                .setServiceImpl("service.impl") // 设置 Service 实现类包名
                .setController("controller");   // 设置 Controller 类包名
        mpg.setPackageInfo(pc);         // 将包配置应用到代码生成器
//        System.out.println(mpg);      // 打印


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override public void initMap(){
                // 初始化自定义配置的 map，这里可以添加一些自定义的数据
                // 示例：map.put("abc", this.getConfig().getGlobalConfig().getAuthor());
                // to do nothing
            }
        };

        // 如果模版引擎是 freemarker
        String templatePath = "templates/mapper.xml.ftl";
        // 如果模版引擎是 velocity
//        String templatePath = "templates/mapper.xml.vm";

        // ⾃定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // ⾃定义输出⽂件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发⽣变化！！
                /*
                    projectPath 是项目的根路径
                    pc.getModuleName: 配置生成代码的包结构
                    tableInfo.getEntityName: TableInfo 对象，包含表的相关信息
                    常量类，DOT_XML 表示 .xml
                 */
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" +
                        StringPool.DOT_XML;
            }
        });

        /*
        自定义文件创建策略
            注释掉 cfg.setFileCreate：使用默认的文件创建策略，每次运行代码生成器时都会重新生成所有文件，可能会覆盖已有的文件。
            保留 cfg.setFileCreate：使用自定义的文件创建策略，可以根据条件决定是否生成文件，避免不必要的文件覆盖。
         */
        /*cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType,
                                    String filePath) {
                // 判断⾃定义⽂件夹是否需要创建
                checkDir("调⽤默认⽅法创建的⽬录，⾃定义⽬录⽤");
                if (fileType == FileType.MAPPER) {
                    // 已经⽣成 mapper ⽂件判断存在，不想重新⽣成返回 false
                    return !new File(filePath).exists();
                }
                // 允许⽣成模板⽂件
                return true;
            }
        });*/

        // 将自定义的文件输出配置列表应用到 InjectionConfig 对象中
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);        // 将自定义配置应用到代码生成器


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置⾃定义输出模板
        //指定⾃定义模板路径，注意不要带上.ftl/.vm, 会根据使⽤的模板引擎⾃动识别
        // TODO 三、修改模板
        /*
        templateConfig.setEntity("templates/entity.java");           // 设置实体类模板路径
        templateConfig.setService("templates/service.java");         // 设置 Service 接口模板路径
        templateConfig.setController("templates/controller.java");   // 设置 Controller 类模板路径
        templateConfig.setMapper("templates/mapper.java");           // 设置 Mapper 接口模板路径
        templateConfig.setServiceImpl("templates/serviceimpl.java"); // 设置 Service 实现类模板路径
        */

        templateConfig.setXml(null);        // 不生成 XML 文件
        mpg.setTemplate(templateConfig);    // 将模板配置应用到代码生成器


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);          // 设置数据库表名转换为实体类名的策略，这里使用下划线转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);    // 设置数据库列名转换为实体类属性名的策略，这里也使用下划线转驼峰命名
        // strategy.setSuperEntityClass("你⾃⼰的⽗类实体,没有就不⽤设置!");
        //strategy.setSuperEntityClass("BaseEntity");
        strategy.setEntityLombokModel(true);    // 设置实体类是否使用 Lombok 注解
        strategy.setRestControllerStyle(true);  // 设置控制器是否使用 REST 风格
        // 公共⽗类
        // strategy.setSuperControllerClass("你⾃⼰的⽗类控制器,没有就不⽤设置!");
        //strategy.setSuperControllerClass("BaseController");
        // 写于⽗类中的公共字段
        // strategy.setSuperEntityColumns("id");        // 设置父类实体中的公共字段
        strategy.setInclude(scanner("表名，多个英⽂逗号分割").split(","));     // 设置需要生成的表名，多个表名用英文逗号分隔
        strategy.setControllerMappingHyphenStyle(true);         // 设置控制器映射路径是否使用连字符风格
        //strategy.setTablePrefix(pc.getModuleName() + "_");    // 设置表前缀，例如 module1_
        // 忽略表前缀tb_,⽐如说tb_user,直接映射成user对象
        // 四、注意是否要去掉表前缀
        //strategy.setTablePrefix("tb_");
        mpg.setStrategy(strategy);      // 将策略配置应用到代码生成器
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());  // 设置模板引擎为 Freemarker
        mpg.execute();      // 执行代码生成

    }
}
