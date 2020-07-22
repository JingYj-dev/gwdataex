package test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/7/2 11:26
 */
public class Gbk2UTF8 {

    public static void main(String[] args) {
        String srcDirPath = "E:\\workspace-dev\\myeclipse\\dataexsys";
        // 转为UTF-8编码格式源码路径
        String utf8DirPath = "E:\\workspace-dev\\myeclipse\\dataexsys-u8";
        System.out.println(srcDirPath);
        System.out.println("开始gbk-utf8转换");
        System.out.println(utf8DirPath);
        try {
            // 获取所有java文件
            Collection<File> javaGbkFileCol = FileUtils.listFiles(new File(srcDirPath), new String[]{"java","xml","properties","dtd","dat"}, true);
            for (File javaGbkFile : javaGbkFileCol) {
                // UTF8格式文件路径
                String utf8FilePath = utf8DirPath + javaGbkFile.getAbsolutePath().substring(srcDirPath.length());

                String fileEcoding=FileEcodingUtils.charset(javaGbkFile.getPath());
                System.out.println(javaGbkFile.getPath()+"字符集为: "+fileEcoding);
                if("gbk".equalsIgnoreCase(fileEcoding)){
                    // 使用GBK读取数据，然后用UTF-8写入数据
                    FileUtils.writeLines(new File(utf8FilePath), "UTF-8", FileUtils.readLines(javaGbkFile, "GBK"));
                }else{
                    FileUtils.copyFile(javaGbkFile,new File(utf8FilePath));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("转换完成。");
    }

}
