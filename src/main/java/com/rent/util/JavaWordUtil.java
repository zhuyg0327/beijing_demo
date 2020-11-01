package com.rent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class JavaWordUtil {
    private JavaWordUtil(){}

    private static String CHARSET = "gbk";//编码格式

    private static String PATH = "E:uploadword";//本地测试路径

//private static String PATH = "/home/upload/base/word/";//服务器文件存放路径

    /**
     * @param fileName
     * @param content
     * @return
     * @方法说明 生成word文档，如果返回null，则表示生成失败
     * @date 2012-7-5
     * @author 孙伟
     */
    public static String createWordFile(String fileName,String content){
        OutputStreamWriter os = null;
        FileOutputStream fos = null;
        try{
            if(fileName.indexOf(".doc")>-1){
                fileName = fileName.substring(0, fileName.length()-4);
            }

            File file = new File(PATH);

//如果目录不存在就创建
            if (!(file.exists() && file.isDirectory())) {
                file.mkdirs();
            }

            fileName = PATH + "" + fileName + "-" +System.currentTimeMillis() + ".doc";

//创建文件
            File targetFile = new File(fileName);
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            fos = new FileOutputStream(fileName);
            os = new OutputStreamWriter(fos,CHARSET);
            os.append(content.toString());
            os.flush();
            return fileName;
        }catch(Exception e){
            return null;
        }finally{
            try{
                os.close();
                fos.close();
            }catch(Exception e){
                return null;
            }
        }
    }

}
