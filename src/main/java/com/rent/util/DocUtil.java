package com.rent.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocUtil {
    private static HWPFDocument doc = null;

    private static Range range = null;

    private static List<Picture> pictsList = null;

    // 用来标记是否存在图片
    boolean hasPic = false;

    /**
     * 构造器，注意到所传入的参数必须是微软word文档的名字
     *
     * @param msDocName
     * @throws IOException
     * @throws FileNotFoundException
     */
    public DocUtil(File msDocName) {
        try {
            doc = new HWPFDocument(new FileInputStream(msDocName));
            range = doc.getRange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 默认构造器，为私有函数
     */
    private DocUtil() {

    }

    /**
     * 从word文档中获取所有文字
     *
     * @return
     */
    public static String getAllText() {
        int numP = range.numParagraphs();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < numP; ++i) {
            //从每一段落中获取文字
            Paragraph p = range.getParagraph(i);
            ret.append(p.text());
        }
        return ret.toString();
    }

    /**
     * 从word里面提取图片
     *
     * @return
     */
    private boolean extractPictures() {
        pictsList = new ArrayList();
        // 得到文档的数据流
        byte[] dataStream = doc.getDataStream();
        int numChar = range.numCharacterRuns();

        PicturesTable pTable = new PicturesTable(doc, dataStream, new byte[1024]);
        for (int j = 0; j < numChar; ++j) {
            CharacterRun cRun = range.getCharacterRun(j);
            // 是否有图片
            boolean has = pTable.hasPicture(cRun);
            if (has) {
                Picture picture = pTable.extractPicture(cRun, true);
                // 大于300bites的图片我们才弄下来，消除word中莫名的小图片的影响
                if (picture.getSize() > 300) {
                    pictsList.add(picture);
                    hasPic = true;
                }
            }
        }
        return hasPic;
    }

    /**
     * word文档里有几张图片，使用这个函数之前，
     * 必须先使用函数 extractPictures()
     *
     * @return
     */
    public int numPictures() {
        if (!hasPic)
            return 0;
        return pictsList.size();
    }

    /**
     * 把提取的图片保存到用户指定的位置
     * @param picNames， 图片要保存的路径,最好完整地写上图片类型
     * @return
     */
    private boolean writePictures(String[] picNames, String savePath) {
        int size = pictsList.size();
        if (size == 0)
            return false;

        for (int i = 0; i < size; ++i) {
            Picture p = pictsList.get(i);
            try {
                p.writeImageContent(new FileOutputStream(savePath + "/" + picNames[i]));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // 读取File源word文件docx文字
    public static Map<String,Object> readDocxImage(File file) {
//        String path = srcFile;
//        File file = new File(path);
        Map<String,Object> map=new HashMap<>();
        try {
            // 用XWPFWordExtractor来获取文字
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
            String text = xwpfWordExtractor.getText();
            map.put("content",text);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("picture",pictsList);
        return map;
    }

    public static Map<String,Object> readDocImage(File file) {
        DocUtil extr = new DocUtil(file);
        String str = extr.getAllText().trim();
        extr.extractPictures();
        int num = extr.numPictures();
        String names[] = new String[num];
        for (int i = 0; i < num; ++i) {
            String imageType = pictsList.get(i).getMimeType().split("/")[1];
            names[i] = "image" + i + "." + imageType;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("content",str);
        map.put("picture",pictsList);
        return map;
    }

    public static Map<String,Object> readWordImage(File file) {
        String[] split = file.getName().split("\\.");
        Map<String,Object> map=new HashMap<>();
        String str=file.getAbsolutePath();
        String str1=file.getPath();
        if ("doc".equals(split[1])) {
            map=readDocImage(file);
        } else if ("docx".equals(split[1])) {
            map=readDocxImage(file);
        }
        return map;
    }
}
