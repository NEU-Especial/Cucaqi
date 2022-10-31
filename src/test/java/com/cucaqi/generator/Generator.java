package com.cucaqi.generator;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.ArrayList;
import java.util.Collections;

public class Generator {


    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("t_admin");
        strings.add("t_answerer");
        strings.add("t_survey");
        strings.add("t_group");
        strings.add("t_lessee");
        strings.add("t_survey");
        strings.add("t_user");


        FastAutoGenerator.create("jdbc:mysql://localhost:3306/cucaqi?useSSL=false&useUnicode=true&characterEncoding=UTF-8", "root", "xgs583719992")
                .globalConfig(builder -> {
                    builder.author("GaoSong Xu") // 设置作者
                            .fileOverride()
                            .outputDir("C://Users/LENOVO/Desktop/cucaqi/src/main/java/ccc"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com") // 设置父包名
                            .moduleName("cucaqi") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\LENOVO\\Desktop\\cucaqi\\src\\main\\resources\\mapper2")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(strings)
                            .addTablePrefix("t_"); // 设置过滤表前缀
                }).execute();

    }

}
