package test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/7/2 11:25
 */
public class FileEcodingUtils {
    /**
     * 根据文件路径返回文件编码
     * @param path
     * @return
     * @throws IOException
     */
    public static String charset(String path) throws IOException {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0); // 读者注： bis.mark(0);修改为 bis.mark(100);我用过这段代码，需要修改上面标出的地方。
            // Wagsn注：不过暂时使用正常，遂不改之
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; // 文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(bis!=null){
                bis.close();
            }
        }
        System.out.println("--文件-> [" + path + "] 采用的字符集为: [" + charset + "]");
        return charset;
    }

    /**
     * 以指定编码方式写文本文件，存在会覆盖
     *
     * @param file
     *            要写入的文件
     * @param toCharsetName
     *            要转换的编码
     * @param content
     *            文件内容
     * @throws IOException
     * @throws Exception
     */
    public static void saveFile2Charset(File file, String toCharsetName,
                                        String content) throws IOException {
        if (!Charset.isSupported(toCharsetName)) {
            throw new UnsupportedCharsetException(toCharsetName);
        }
        try (OutputStream outputStream = new FileOutputStream(file)){
            //增加头文件标识
            outputStream.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
            try(OutputStreamWriter outWrite = new OutputStreamWriter(outputStream, toCharsetName);){
                outWrite.write(content);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取文件内容返回string
     * @throws IOException
     *
     */
    public static String file2String(File file,String inputFileEncode) throws IOException{
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try{
            //br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), inputFileEncode));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(br!=null){
                br.close();
            }
        }
        return result.toString();
    }


}
