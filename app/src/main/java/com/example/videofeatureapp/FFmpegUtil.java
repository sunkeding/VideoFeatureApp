package com.example.videofeatureapp;


/**
 * ffmpeg tool: assemble the complete command
 * Created by frank on 2018/1/23.
 */

public class FFmpegUtil {


    /**
     * transform video, according to your assigning the output format
     *
     * @param inputPath  input file
     * @param outputPath output file
     * @return transform video success or not
     */
    public static String[] transformVideo(String inputPath, String outputPath) {
        //just copy codec
//        String transformVideoCmd = "ffmpeg -i %s -vcodec copy -acodec copy %s";
        // assign the frameRate, bitRate and resolution
//        String transformVideoCmd = "ffmpeg -i %s -r 25 -b 200 -s 1080x720 %s";
        // assign the encoder
//        String transformVideoCmd = "ffmpeg -i %s -vcodec libx264 -acodec libmp3lame %s";
        String transformVideoCmd = "ffmpeg -y -i %s -b:v 2000k -vf scale=720:-1 -ar 44100 -preset ultrafast %s";
        transformVideoCmd = String.format(transformVideoCmd, inputPath, outputPath);
        return transformVideoCmd.split(" ");
    }
}
