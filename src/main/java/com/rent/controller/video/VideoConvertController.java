package com.rent.controller.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@RestController
@RequestMapping("/api/videoConvert")
public class VideoConvertController {
    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     *
     * @throws Exception
     */
    @RequestMapping("/getImg")
    @ResponseBody
    public static void fetchFrame(@RequestParam(value = "file", required = false) MultipartFile file)
            throws Exception {

        File videoFile = null;
        String fileName = "";
        String originalFilename = file.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        fileName = filename[0];
        String suffix = "." + filename[1];
        videoFile = File.createTempFile(filename[0], suffix);
        file.transferTo(videoFile);

        long start = System.currentTimeMillis();
        //截取图片存放路径
        String framefile="F://video/dog.jpg";
        File targetFile = new File(framefile);
        if(!targetFile.exists()){
            targetFile.mkdir();
        }
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile.getAbsoluteFile());
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabFrame();
            if ((i > 5) && (f.image != null)) {
                break;
            }
            i++;
        }
        int width = f.imageWidth;
        int height = f.imageHeight;
        // 对截取的帧进行等比例缩放
        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage fecthedImage =converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        //ff.flush();
        ImageIO.write(bi, "jpg", targetFile);
        //ff.flush();
        ff.stop();
        System.out.println(System.currentTimeMillis() - start);
    }
}
