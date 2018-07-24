package com.hy.library.utils;

import android.media.MediaMetadataRetriever;

import java.util.HashMap;

/**
 * Created time : 2018/7/7 9:56.
 * 视频信息获取工具
 * @author HY
 */
public class VideoUtils {

    private VideoInformation videoInformation;

    public VideoUtils(VideoInformation videoInformation) {
        this.videoInformation = videoInformation;
    }

    //获取视频的宽高,和时长
    public void getVideoWidthAndHeightAndVideoTimes(String videoUrl) {
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoUrl, new HashMap<>());
        new Thread() {
            @Override
            public void run() {
                float videoTimes = 0;
                float videoWidth = 0;
                float videoHeight = 0;
                super.run();
                try {
                    videoTimes = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    videoWidth = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    videoHeight = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                } catch (Exception e) {
                    videoTimes = 0;
                    videoWidth = 0;
                    videoHeight = 0;
                } finally {
                    Logger.i("视频的宽：  " + videoWidth);
                    Logger.i("视频的高：  " + videoHeight);
                    Logger.i("视频的长度：  " + videoTimes);
                    mediaMetadataRetriever.release();
                    videoInformation.dealWithVideoInformation(videoWidth, videoHeight, videoTimes);
                }
            }
        }.start();
    }

   public interface VideoInformation {
        void dealWithVideoInformation(float w, float h, float vt);
    }
}
