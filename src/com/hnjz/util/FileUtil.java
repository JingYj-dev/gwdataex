package com.hnjz.util;

import com.hnjz.util.Md5Util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:22
 */
public class FileUtil {
    public FileUtil() {
    }

    public static void mkdir(File file) {
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

    }

    public static void mkdir(String file) {
        mkdir(new File(file));
    }

    public static void mkfile(File file) throws IOException {
        if (file != null && !file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                mkdir(parentFile);
            }

            file.createNewFile();
        }

    }

    public static void mkfile(String file) throws IOException {
        mkfile(new File(file));
    }

    public static void del(String filename) throws Exception {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }

    }

    public static void delFile(File path) throws Exception {
        if (!path.exists()) {
            throw new RuntimeException("文件" + path + "不存在!");
        } else {
            if (path.isFile()) {
                del(path.getAbsolutePath());
            } else {
                File[] files = path.listFiles();

                for(int i = 0; i < files.length; ++i) {
                    delFile(files[i]);
                    del(files[i].getAbsolutePath());
                }
            }

        }
    }

    public static void delFile(String path) throws Exception {
        delFile(new File(path));
    }

    public static String getExtension(File f) {
        return f != null ? getExtension(f.getName()) : "";
    }

    public static String getExtension(String filename) {
        return getExtension(filename, "");
    }

    public static String getExtension(String filename, String defExt) {
        if (filename != null && filename.length() > 0) {
            int i = filename.lastIndexOf(46);
            if (i > -1 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }

        return defExt;
    }

    public static String trimExtension(String filename) {
        if (filename != null && filename.length() > 0) {
            int i = filename.lastIndexOf(46);
            if (i > -1 && i < filename.length()) {
                return filename.substring(0, i);
            }
        }

        return filename;
    }

    public static void list(File path, String[] extArr, HashMap hm) {
        if (!path.exists()) {
            System.out.println("文件名称不存在!");
        } else if (path.isFile()) {
            for(int i = 0; i < extArr.length; ++i) {
                if (path.getName().toLowerCase().endsWith(extArr[i])) {
                    hm.put(path.getName(), path.getAbsolutePath());
                }
            }
        } else {
            File[] files = path.listFiles();

            for(int i = 0; i < files.length; ++i) {
                list(files[i], extArr, hm);
            }
        }

    }

    public static void list(File path, Map hm) {
        if (!path.exists()) {
            System.out.println("文件名称不存在!");
        } else if (path.isFile()) {
            hm.put(Md5Util.MD5Encode(path.getAbsolutePath()), path.getAbsolutePath());
        } else {
            File[] files = path.listFiles();

            for(int i = 0; i < files.length; ++i) {
                list(files[i], hm);
            }
        }

    }

    public static void list(String path, Map hm) {
        list(new File(path), hm);
    }

    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;

        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return file;
    }

    public static byte[] getBytesFromFile(String path) {
        BufferedInputStream stream = null;
        File file = null;
        byte[] b = (byte[])null;

        try {
            file = new File(path);
            b = new byte[(int)file.length()];
            FileInputStream fstream = new FileInputStream(file);
            stream = new BufferedInputStream(fstream);
            stream.read(b);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return b;
    }

    public static void copyFile(String oldPath, String newPath, Integer arrSize) throws Exception {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inputStream = new FileInputStream(oldPath);
                outputStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[arrSize];

                while((byteread = inputStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    outputStream.write(buffer, 0, byteread);
                }

                outputStream.flush();
            }
        } catch (Exception var13) {
            System.out.println("复制单个文件操作出错");
            throw var13;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

        }

    }
}
