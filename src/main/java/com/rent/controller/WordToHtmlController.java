package com.rent.controller;

import com.rent.service.WordToHtmlService;
import com.rent.util.Response;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * word 转换成html
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/WordToHtml")
public class WordToHtmlController {
    @Autowired
    WordToHtmlService wordToHtmlService;

    /**
     * 2007版本word转换成html
     *
     * @throws IOException
     */
    @RequestMapping("/test1")
    public void Word2007ToHtml() throws IOException {
        String filepath = "F:/test/";
        String fileName = "test.docx";
        String htmlName = "test.html";
        final String file = filepath + fileName;
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("Sorry File does not Exists!");
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {

                // 1) 加载word文档生成 XWPFDocument对象
                InputStream in = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(in);

                // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
                File imageFolderFile = new File(filepath);
                XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
                options.setExtractor(new FileImageExtractor(imageFolderFile));
                options.setIgnoreStylesIfUnused(false);
                options.setFragment(true);

                // 3) 将 XWPFDocument转换成XHTML
                OutputStream out = new FileOutputStream(new File(filepath + htmlName));
                XHTMLConverter.getInstance().convert(document, out, options);

                //也可以使用字符数组流获取解析的内容
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                XHTMLConverter.getInstance().convert(document, baos, options);
                String content = baos.toString();
                System.out.println(content);
                baos.close();
                Response.json(content);
            } else {
                System.out.println("Enter only MS Office 2007+ files");
            }
        }
    }

    /**
     * /**
     * 2003版本word转换成html
     *
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    @RequestMapping("/test2")
    public void Word2003ToHtml() throws IOException, TransformerException, ParserConfigurationException {
        String filepath = "F:/test/";
        final String imagepath = "image.files/";
        String fileName = "test.doc";
        String htmlName = "test1.html";
        final String file = filepath + fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //设置图片存放的位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                File imgPath = new File(filepath + imagepath);
                if (!imgPath.exists()) {//图片目录不存在则创建
                    imgPath.mkdirs();
                }
                File file = new File(imagepath + suggestedName);
                try {
                    OutputStream os = new FileOutputStream(file);
                    os.write(content);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imagepath + suggestedName;
            }
        });

        //解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();

        File htmlFile = new File(filepath + htmlName);
        OutputStream outStream = new FileOutputStream(htmlFile);

        //也可以使用字符数组流获取解析的内容
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream outStream1 = new BufferedOutputStream(baos);
        String str = outStream1.toString();

        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        serializer.transform(domSource, streamResult);

        //也可以使用字符数组流获取解析的内容
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        OutputStream outStream1 = new BufferedOutputStream(baos);

        InputStream is = new FileInputStream(file);
        WordExtractor wr = new WordExtractor(is);
        String resullt = wr.getText();
        //Response.json(resullt);

        Map<String, Object> map = new HashMap<>();
        Response.json(str);
        wr.close();
        String content = baos.toString();
        System.out.println(content);
        baos.close();
        outStream.close();
    }

    @RequestMapping("/importWord")
    public void importWord(@RequestParam(value = "file", required = false) MultipartFile file) {
        File file1 = null;
        try {
            String originalFilename = file.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file1 = File.createTempFile(filename[0], ".doc");
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = wordToHtmlService.readHtml(file1);
        String htmlContent = (String) map.get("body");
        File wordFile = (File) map.get("file");
        if (map.size() > 0) {
            Response.json("result", map);
        } else {
            Response.json("html文件读取失败！");
        }
    }

    @RequestMapping("/importtest")
    public void changeHtml() throws IOException {
        File file = new File("C:/test/test1.html");
        InputStream in = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        String s = null;
        StringBuffer buff = new StringBuffer();
        while ((s = br.readLine()) != null) {
            buff.append(s);
        }
        br.close();
        reader.close();
        in.close();
        System.out.println(buff.toString());

        String htmlContent = buff.toString();
        String htmlContent1 = changeStyle(buff);
        Map<String, Object> map = new HashMap<>();
        map.put("html1", htmlContent);
        map.put("html2", htmlContent1);
        Response.json("result", map);
    }

    public String changeStyle(StringBuffer buff) {
        String htmlContent = "";
        StringBuffer buffStyle = new StringBuffer();
        //截取样式代码
        buffStyle.append(buff.substring(buff.indexOf("<style type=\"text/css\">") + 23, buff.indexOf("style", buff.indexOf("<style type=\"text/css\">") + 23) - 2));
        System.out.println(buffStyle);
        //截取body代码
        String body = buff.substring(buff.indexOf("<body"), buff.indexOf("</body") + 7);
        body = body.replaceAll("body", "div");
        StringBuffer bodyBuffer = new StringBuffer(body);
        System.out.println(bodyBuffer);
        String[] split = buffStyle.toString().split("}");
        Map<String, String> styleMap = new HashMap<>();
        for (String s1 : split) {
            System.out.println(s1);
            String[] split1 = s1.split("\\{");
            styleMap.put(split1[0].substring(1), split1[1]);
        }
        Set<String> strings = styleMap.keySet();
        for (String key : strings) {
            System.out.print("key : " + key);
            System.out.println("   value : " + styleMap.get(key));
            //将嵌入样式转换为行内样式
            if (bodyBuffer.toString().contains(key)) {
                int length = bodyBuffer.toString().split(key).length - 1;
                int temp = 0;
                for (int i = 0; i < length; i++) {
                    //首先判断是否完全匹配这个样式的class标识
                    //由于word转换为html的时候他会自动生成class的标识  比如 p1,p2,p3,p4,p10,p11这样的话使用contains方法
                    //p1就会被p11匹配到，这样样式就会乱掉，所以在添加行内样式之前必须要进行完全匹配
                    temp = bodyBuffer.indexOf(key, temp);
                    String isComplete = bodyBuffer.substring(temp, temp + key.length() + 1);
                    //这个地方key+" "意思是代表可能一个标签里面有多个class标识 比如 class = "p2 p3 p4"
                    if (!isComplete.equals(key + "\"") && !isComplete.equals(key + " ")) {
                        //这种就代表不是完全匹配
                        continue;
                    }
                    //这个是每次查询到的位置，判断此标签中是否添加了style标签
                    String isContaionStyle = bodyBuffer.substring(temp, bodyBuffer.indexOf(">", temp));
                    if (isContaionStyle.contains("style")) {
                        //代表已经存在此style，那么直接加进去就好了
                        //首先找到style的位置
                        int styleTemp = bodyBuffer.indexOf("style", temp);
                        bodyBuffer.insert(styleTemp + 7, styleMap.get(key));
                    } else {
                        //代表没有style，那么直接插入style
                        int styleIndex = bodyBuffer.indexOf("\"", temp);
                        bodyBuffer.insert(styleIndex + 1, " style=\"" + styleMap.get(key) + "\"");
                    }
                    temp += key.length() + 1;
                }
            }
        }
        System.out.println("------------------------------------------");
        System.out.println(bodyBuffer.toString());
        changePicture(bodyBuffer);
        htmlContent = bodyBuffer.toString();
        return bodyBuffer.toString();
    }

    //更换图片的路径
    public void changePicture(StringBuffer buffer) {
        //查询一个有多少个图片
        int length = buffer.toString().split("<img src=\"").length - 1;
        int temp = 0;
        for (int i = 0; i < length; i++) {
            temp = buffer.indexOf("<img src=\"", temp);
            String srcContent = buffer.substring(temp + 10, buffer.indexOf("style", temp + 10));
            //获取第三方文件服务器的路径,比如如下realSrc
            String realSrc = "";
            //将路径进行替换
            buffer.replace(temp + 10, buffer.indexOf("style", temp + 10), realSrc + "\"");
            temp++;
        }
    }

    //------------------------
    public static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos,"GB2312"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }

    public static void convert2Html(String fileName, String outPutFile)
            throws TransformerException, IOException,
            ParserConfigurationException {
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager( new PicturesManager()
        {
            public String savePicture( byte[] content,
                                       PictureType pictureType, String suggestedName,
                                       float widthInches, float heightInches )
            {
                return "test/"+suggestedName;
            }
        } );
        wordToHtmlConverter.processDocument(wordDocument);
        //save pictures
        List pics=wordDocument.getPicturesTable().getAllPictures();
        if(pics!=null){
            for(int i=0;i<pics.size();i++){
                Picture pic = (Picture)pics.get(i);
                System.out.println();
                try {
                    pic.writeImageContent(new FileOutputStream("D:/test/"
                            + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        writeFile(new String(out.toByteArray()), outPutFile);
    }
}