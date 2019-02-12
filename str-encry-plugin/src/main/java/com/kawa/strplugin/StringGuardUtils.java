package com.kawa.strplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class StringGuardUtils {

    public static boolean writeFileLine(List<String> lists, String fileName) {
        if (lists == null || lists.size() == 0) {
            return false;
        }
        if (isTextEmpty(fileName)) {
            return false;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            for (String str : lists) {
                if (!isTextEmpty(str)) {
                    bw.write(str);
                    bw.write(System.lineSeparator());
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            ioClose(fw, bw);
        }
    }

    @SuppressWarnings("finally")
    public static List<String> readFileLine(String file) {
        List<String> list = new ArrayList<>();
        if (isTextEmpty(file)) {
            return null;
        }
        if (!new File(file).exists()) {
            return null;
        }
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new FileReader(file);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            return null;
        } finally {
            return ioClose(isr, br) ? list : null;
        }
    }

    @SuppressWarnings("finally")
    public static boolean saveFileData(File file, byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
        } catch (Exception e) {
            return false;
        } finally {
            return ioClose(fos);
        }
    }

    public static boolean ioClose(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            int count = 0;
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                    count++;
                } catch (Exception e) {
                }
            }
            if (count == closeables.length) {
                return true;
            }
        }
        return false;
    }

    public static String getFileName(String path) {
        if (isTextEmpty(path)) {
            return null;
        }
        try {
            return path.substring(path.lastIndexOf(File.separator) + 1, path.length());
        } catch (Exception e) {
        }
        return null;
    }

    public static String getFilePath(String path) {
        if (isTextEmpty(path)) {
            return null;
        }
        try {
            return path.substring(0, path.lastIndexOf(File.separator));
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean isTextEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String printTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StackTraceElement[] elements = throwable.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement ele : elements) {
            sb.append(ele.getClassName() + "." + ele.getMethodName() + ":" + ele.getLineNumber());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * 输入：/app/build/intermediates/classes/debug/cn/wjdiankong/changeclassloader/MainActivity.class
     * @param dir ：/app/build/intermediates/classes/debug/
     * @param path ：/app/build/intermediates/classes/debug/cn/wjdiankong/changeclassloader/MainActivity.class
     * @return cn.wjdiankong.changeclassloader.MainActivity
     */
    public static String pathToClassName(String dir, String path) {
        if (isTextEmpty(path) || isTextEmpty(dir)) {
            return null;
        }
        try {
            // 补上路径符
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }
            String classPath = path.split(dir)[1].replace(File.separator, ".");
            return classPath.substring(0, classPath.lastIndexOf("."));
        } catch (Exception e) {
            return null;
        }
    }

}
