package com.kawa.strplugin

import com.kawa.strplugin.strproguard.GlobalConfig
import com.kawa.strplugin.strproguard.StrProguard
import com.kawa.strplugin.strproguard.SettingParams
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

/****
 * <pre>
 *  Project_Name:    Bookkeeping
 *  Created:         Kawa on 2019/1/8 14:07.
 *  E-mail:          958129971@qq.com
 *  Desc:            
 * </pre> 
 ****/
public class StringEncryPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        System.out.println("========================");
        System.out.println("开始进入字符串加密Plugin");
        System.out.println("========================");

        project.extensions.create('stringEncry', SettingParams)
        GlobalConfig.setProject(project)
        def android = project.extensions.getByType(AppExtension)
        def classTransform = new StrProguard(project)
        android.registerTransform(classTransform)
    }
}
