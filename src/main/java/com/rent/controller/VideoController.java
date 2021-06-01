//package com.rent.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.rent.util.Response;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import ws.schild.jave.Encoder;
//import ws.schild.jave.EncoderException;
//import ws.schild.jave.MultimediaObject;
//import ws.schild.jave.encode.AudioAttributes;
//import ws.schild.jave.encode.EncodingAttributes;
//import ws.schild.jave.encode.VideoAttributes;
//import ws.schild.jave.info.MultimediaInfo;
//import ws.schild.jave.info.VideoSize;
//import ws.schild.jave.progress.EncoderProgressListener;
//
//import java.io.File;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/video")
//public class VideoController {
//
//    /**
//     * 视频转换，视频格式转mp4
//     *
//     * @param file
//     * @throws EncoderException
//     */
//    @RequestMapping("/videoToMp4")
//    @ResponseBody
//    public void videoToMp4(@RequestParam(value = "file", required = false) MultipartFile file) throws EncoderException {
//
//        File videoFile = null;
//        String fileName = "";
//        String targetSuffix = ".mp4";
//        try {
//            String originalFilename = file.getOriginalFilename();
//            String[] filename = originalFilename.split("\\.");
//            fileName = filename[0];
//            String suffix = "." + filename[1];
//            videoFile = File.createTempFile(filename[0], suffix);
//            file.transferTo(videoFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //转换后的MP4地址
//        String distFile = "F://video" + File.separator + fileName + targetSuffix;
//        File target = new File(distFile);
//        if (target.exists()) {
//            target.delete();
//        }
//        System.out.println("开始视频转换");
//        AudioAttributes audioAttr = new AudioAttributes();
//        VideoAttributes videoAttr = new VideoAttributes();
//        EncodingAttributes encodingAttr = new EncodingAttributes();
//        audioAttr.setChannels(2);
//        audioAttr.setCodec("aac");
//        audioAttr.setBitRate(128000);
//        audioAttr.setSamplingRate(44100);
//        videoAttr.setCodec("libx264");
//        videoAttr.setBitRate(2 * 1024 * 1024);
//        videoAttr.setSize(new VideoSize(1080, 720));
//        videoAttr.setFaststart(true);
//        videoAttr.setFrameRate(29);
//        encodingAttr.setAudioAttributes(audioAttr);
//        encodingAttr.setVideoAttributes(videoAttr);
//        encodingAttr.setOutputFormat("mp4");
//        Encoder encoder = new Encoder();
//        encoder.encode(new MultimediaObject(videoFile), target, encodingAttr, new EncoderProgressListener() {
//            @Override
//            public void sourceInfo(MultimediaInfo multimediaInfo) {
//                System.out.println(JSON.toJSONString(multimediaInfo));
//            }
//
//            @Override
//            public void progress(int i) {
//            }
//
//            @Override
//            public void message(String s) {
//                System.out.println(s);
//            }
//        });
//        Response.json(distFile);
//    }
//
//}
