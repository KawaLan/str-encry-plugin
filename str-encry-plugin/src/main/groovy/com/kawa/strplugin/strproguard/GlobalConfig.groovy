package com.kawa.strplugin.strproguard


import org.gradle.api.Project

/**
 * 全局配置类
 */
class GlobalConfig {

    private static Project project

    static void setProject(Project project) {
        GlobalConfig.@project = project
    }

    static SettingParams getParams() {
        return project.stringEncry
    }
}