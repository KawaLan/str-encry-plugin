package com.kawa.strplugin.strproguard

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import org.gradle.api.Project
import com.android.build.gradle.internal.pipeline.TransformManager

/****
 * <pre>
 *  Project_Name:    Bookkeeping
 *  Created:         Kawa on 2019/1/8 14:07.
 *  E-mail:          958129971@qq.com
 *  Desc:            
 * </pre> 
 ****/
public class StrProguard extends Transform {

    Project project;

    StrProguard(Project project) {
        this.project = project;
    }

    @Override
    String getName() {
        return "StrProguard"
    }

    /**
     * CLASSES - 表示处理java的class文件
     * RESOURCES - 表示处理java的资源
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        System.out.println("________开始Transform_______________")
        // Transform 的 inputs 分为两种类型，一直是目录，一种是 jar 包。需要分开遍历

        inputs.each { TransformInput input ->
            // 1) 对类型为"目录"的 input 进行遍历
            input.directoryInputs.each { DirectoryInput dirInput ->
                ClassHandler.injectUpdateClass(dirInput.file.absolutePath, project)
                // 获取 output 目录
                def dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes,
                        dirInput.scopes, Format.DIRECTORY)
                // 将 input 的目录复制到 output 指定目录
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            // 2) 对类型为 jar 文件的 input 进行遍历
            input.jarInputs.each { JarInput jarInput ->
                // jar 文件一般是第三方依赖库jar包

                // 重命名输出文件（同目录 copyFile 会冲突）
                def jarName = jarInput.name
                def md5Name = EncDecUtils.md5(jarInput.file.getAbsolutePath())

                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                // 生成输出路径
                def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes,
                        jarInput.scopes, Format.JAR)
                // 将输入内容复制到输出
//                FileUtils.copyFile(jarInput.file, dest)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }

        System.out.println("________结束了Transform_______________")
    }
}
