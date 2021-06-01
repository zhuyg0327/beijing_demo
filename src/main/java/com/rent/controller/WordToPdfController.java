package com.rent.controller;

import com.rent.util.Response;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;



@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/pdf")
public class WordToPdfController {
    public static void wordConverterToPdf(InputStream source,
                                          OutputStream target, Map<String, String> params) throws Exception {
        wordConverterToPdf(source, target, null, params);
    }

    /**
     * 将word文档， 转换成pdf, 中间替换掉变量
     *
     * @param source  源为word文档， 必须为docx文档
     * @param target  目标输出
     * @param params  需要替换的变量
     * @param options PdfOptions.create().fontEncoding( "windows-1250" ) 或者其他
     * @throws Exception
     */
    public static void wordConverterToPdf(InputStream source, OutputStream target, PdfOptions options,
                                          Map<String, String> params) throws Exception {
        XWPFDocument docx = new XWPFDocument(source);
        PdfConverter.getInstance().convert(docx, target, options);
    }

    @RequestMapping("/test1")
    public void wordToPdf(@RequestParam(value = "file", required = false) MultipartFile file) {
        //String filepath = "F:\\test\\test.docx";
        String outpath = "F:\\test\\test.pdf";

        File file1 = null;
        try {
            String originalFilename = file.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file1 = File.createTempFile(filename[0], ".docx");
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream source;
        OutputStream target;
        try {
            source = new FileInputStream(file1);
            target = new FileOutputStream(outpath);
            Map<String, String> params = new HashMap<String, String>();

            PdfOptions options = PdfOptions.create();
            wordConverterToPdf(source, target, options, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response.json("result", "success");
    }

//    @RequestMapping("/test2")
//    public static void savaPdf(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        File files = null;
//        try {
//            String originalFilename = file.getOriginalFilename();
//            String[] filename = originalFilename.split("\\.");
//            files = File.createTempFile(filename[0], ".docx");
//            file.transferTo(files);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String wordPath=files.getPath();
//        String p1=files.getAbsolutePath();
//        String p2=files.getName();
//        //加载测试文档
//        Document doc = new Document(wordPath);
////       Document doc = new Document();
////       doc.loadFromFile(files);
//        //将文档指定页保存为Png格式的图片
//     //   BufferedImage image = doc.saveToImages(0, ImageType.Bitmap);
////        File file1 = new File("ToPNG.png");
////        ImageIO.write(image, "PNG", file1);
//
//        //将Word转为PDF
//        doc.saveToFile("F:\\test\\test3.pdf", FileFormat.PDF);
//        Response.json("result", "success");
//    }



}
