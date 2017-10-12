package com.lib.base.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lib.base.R;
import com.lib.base.widget.CustomVideoView;

/**
 * Created by jarylan on 2017/4/6.
 * 使用示例 ：在 Activity 中调用如下代码
    String uri = "android.resource://" + getPackageName() + "/" + R.raw.guide_2;
    getSupportFragmentManager().beginTransaction()
        .add(R.id.frame_content, SplashVideoFragment.newInstance(uri, new SplashVideoFragment.OnPlayCompletionListener() {
            //@Override
                public void onPlayCompletion(boolean error) {
                Log.d("videoplayfragment","------------------播放完成逻辑 isError : " + error);
            }
        }))
        .commitAllowingStateLoss();
 *
 */

public class SplashVideoFragment extends Fragment{

    public final static String VIDEO_URL_KEY = "video.url.key";
    private static OnPlayCompletionListener onPlayCompletionListener;
    private CustomVideoView customVideoView;

    public static SplashVideoFragment newInstance(String uri, OnPlayCompletionListener listener) {
        onPlayCompletionListener = listener;
        Bundle args = new Bundle();
        args.putString(VIDEO_URL_KEY,uri);
        SplashVideoFragment fragment = new SplashVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videoview, container, false);
        customVideoView = (CustomVideoView) view.findViewById(R.id.videoview);
        customVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(onPlayCompletionListener != null){
                    onPlayCompletionListener.onPlayCompletion(false);
                }
            }
        });
        customVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(onPlayCompletionListener != null){
                    onPlayCompletionListener.onPlayCompletion(true);
                }
                return true;
            }
        });
        Uri uri = Uri.parse(getArguments().getString(VIDEO_URL_KEY));
        customVideoView.playVideo(uri,new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);//循环播放
                mp.setVolume(0f,0f);//屏蔽声音
                /*开始播放*/
                mp.start();
                if(onPlayCompletionListener != null){
                    onPlayCompletionListener.onPrepared();
                }
            }
        });//播放视频
        return view;
    }

    /**
     * 记得在销毁的时候让播放的视频终止
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customVideoView != null) {
            customVideoView.stopPlayback();
        }
    }

    public interface OnPlayCompletionListener{
        void onPlayCompletion(boolean error);
        void onPrepared();
    }
}
