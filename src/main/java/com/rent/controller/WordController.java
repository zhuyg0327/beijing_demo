//package com.rent.controller;
//
//import com.rent.util.Response;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.MultipartResolver;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.util.Calendar;
//import java.util.UUID;
//
//@RestController
//@CrossOrigin(allowCredentials = "true")
//@RequestMapping("/api/word")
//public class WordController {
//
//    @PostMapping("/wordimport4editor")
//    public void wordimport4editor(HttpServletRequest request, HttpServletResponse response){
//        MultipartResolver resolver = new CommonsMultipartResolver();
//        MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart(request);
//        MultipartFile wordimportfile = multipartHttpServletRequest.getFile("wordimportfile");
//
//        String wordHtml = getWordHtml(wordimportfile);
//        //this.outJson(response,wordHtml);
//        Response.json(wordHtml);
//    }
//
//    public String getWordHtml(MultipartFile wordimportfile) {
//        File saveFile = null;
//        File htmlFile = null;
//        try {
//            //保存文件
//            String originalFilename = wordimportfile.getOriginalFilename();
//
////            String webRootPath = servletContext.getRealPath("/");
//            String webRootPath="";
//
//            Calendar calendar = Calendar.getInstance();
//            int month = calendar.get(Calendar.MONTH);
//
//            String releativePath = "upload" + File.separator + "wordArticle" + File.separator + (month + 1);
//
//            String dirPath = webRootPath + File.separator + releativePath;
//
//            String saveFilePath = dirPath + File.separator + UUID.randomUUID().toString() + "_" + originalFilename;
//
//            saveFile = new File(saveFilePath);
//
//            if (!saveFile.getParentFile().exists()) saveFile.getParentFile().mkdirs();
//
//            wordimportfile.transferTo(saveFile);
//
//            String htmlFileName = UUID.randomUUID().toString();
//
//            //保存为html文件
//            String htmlFilePath = dirPath + File.separator + htmlFileName + ".html";
////            Document document = new Document(saveFilePath);
////            document.save(htmlFilePath, SaveFormat.HTML);
//
//            //解析html文件
//            htmlFile = new File(htmlFilePath);
//            org.jsoup.nodes.Document doc = Jsoup.parse(htmlFile, "UTF-8");
//
//            Elements imgs = doc.getElementsByTag("img");
//
//            for (Element img : imgs) {
//                String imgSrc = img.attr("src");
//                img.removeAttr("src");
//                img.attr("src", "/upload/wordArticle/" + (month + 1) + "/" + imgSrc);
//            }
//
//            String html = doc.html();
////            log.debug(html);
//            return html;
//        } catch (Exception e) {
////            log.error("error:", e);
//            if (saveFile != null && saveFile.exists()) saveFile.delete();
//            if (htmlFile != null && htmlFile.exists()) htmlFile.delete();
//            return "";
//        }
//    }
//
//}
