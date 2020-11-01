package com.rent.service.impl;

import com.rent.service.WordToHtmlService;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordToHtmlServiceImpl implements WordToHtmlService {
    public String wordToHtml(File file) throws IOException, TransformerException,
            ParserConfigurationException {
        String filePath[] = file.getPath().split("\\.");
        String htmlPath = filePath[0] + File.separator + "html" + File.separator;
        String filename = file.getName();
        String names[] = filename.split("\\.");
        String htmlName = names[0] + ".html";
        final String imagePath = htmlPath + "image" + File.separator;

        // 判断html文件是否存在，每次重新生成
        File htmlFile = new File(htmlPath + htmlName);
//      if (htmlFile.exists()) {
//          return htmlFile.getAbsolutePath();
//      }

        // 原word文档
//        final String file = wordPath + File.separator + wordName + suffix;
//        InputStream input = new FileInputStream((File)file);

        //final String file = wordPath + File.separator + wordName + suffix;
        InputStream input = new FileInputStream(file);
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());

        // 设置图片存放的位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType,
                                      String suggestedName, float widthInches, float heightInches) {
                File imgPath = new File(imagePath);
                if (!imgPath.exists()) {// 图片目录不存在则创建
                    imgPath.mkdirs();
                }
                File file = new File(imagePath + suggestedName);
                try {
                    OutputStream os = new FileOutputStream(file);
                    os.write(content);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 图片在html文件上的路径 相对路径
                return "image/" + suggestedName;
            }
        });

        // 解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();

        // 生成html文件上级文件夹
        File folder = new File(htmlPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 生成html文件地址
        OutputStream outStream = new FileOutputStream(htmlFile);

        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        serializer.transform(domSource, streamResult);

        outStream.close();

        return htmlFile.getAbsolutePath();
    }

    public Map<String, Object> readHtml(File multipartFile) {
        Map<String, Object> map = new HashMap<>();
        map.put("file", multipartFile);
        File file = null;
        try {
            file = new File(wordToHtml(multipartFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        //File file=new File(filepath);
        String body = "";
        try {
            FileInputStream iStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(iStream);
            BufferedReader htmlReader = new BufferedReader(reader);

            String line;
            boolean found = false;
            while (!found && (line = htmlReader.readLine()) != null) {
                if (line.toLowerCase().indexOf("<body") != -1) { // 在<body>的前面可能存在空格
                    found = true;
                }
            }

            found = false;
            while (!found && (line = htmlReader.readLine()) != null) {
                if (line.toLowerCase().indexOf("</body") != -1) {
                    found = true;
                } else {
                    // 如果存在图片，则将相对路径转换为绝对路径
                    String lowerCaseLine = line.toLowerCase();
                    if (lowerCaseLine.contains("src")) {

                        //这里是定义图片的访问路径
                        String directory = "D:/test";
                        // 如果路径名不以反斜杠结尾，则手动添加反斜杠
                        /*if (!directory.endsWith("\\")) {
                            directory = directory + "\\";
                        }*/
                        //    line = line.substring(0,  lowerCaseLine.indexOf("src") + 5) + directory + line.substring(lowerCaseLine.indexOf("src") + 5);
                        /*String filename = extractFilename(line);
                        line = line.substring(0,  lowerCaseLine.indexOf("src") + 5) + directory + filename + line.substring(line.indexOf(filename) + filename.length());
                    */
                        // 如果该行存在多个<img>元素，则分行进行替代
                        String[] splitLines = line.split("<img\\s+"); // <img后带一个或多个空格
                        // 因为java中引用的问题不能使用for each
                        for (int i = 0; i < splitLines.length; i++) {
                            if (splitLines[i].toLowerCase().startsWith("src")) {
                                splitLines[i] = splitLines[i].substring(0, splitLines[i].toLowerCase().indexOf("src") + 5)
                                        + directory
                                        + splitLines[i].substring(splitLines[i].toLowerCase().indexOf("src") + 5);
                            }
                        }

                        // 最后进行拼接
                        line = "";
                        for (int i = 0; i < splitLines.length - 1; i++) { // 循环次数要-1，因为最后一个字符串后不需要添加<img
                            line = line + splitLines[i] + "<img ";
                        }
                        line = line + splitLines[splitLines.length - 1];
                    }

                    body = body + line + "\n";
                }
            }
            htmlReader.close();
            //        System.out.println(body);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String filePath[] = multipartFile.getPath().split("\\.");
        String path = filePath[0];
        //删除HTML文件以及文件夹
        deleteFile(path);
        //删除word文件
        deleteFile(multipartFile.getPath());
        map.put("body", body);
        return map;
    }

    //删除文件或者文件夹
    public static void deleteFile(String path) {
        int flag = 1;
        //判断文件不为null或文件目录存在
        File file = new File(path);
        if (file == null || !file.exists()) {
            flag = 0;
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        if (files != null && files.length > 0) {
            for (File f : files) {
                //打印文件名
                String name = file.getName();
                System.out.println(name);
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()) {
                    deleteFile(f.getPath());
                } else {
                    f.delete();
                }
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
        System.out.println("目录删除成功");
    }
}
