package com.wiggins.teaching.ui.view.banners.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wiggins.teaching.R;
import com.wiggins.teaching.ui.view.banners.Banner;
import com.wiggins.teaching.ui.view.banners.DrawableBanner;
import com.wiggins.teaching.ui.view.banners.RemoteBanner;
import com.wiggins.teaching.ui.view.banners.events.OnBannerClickListener;
import com.wiggins.teaching.ui.view.banners.views.AdjustableImageView;
import com.wiggins.teaching.utils.GlideUtils;


/**
 * @author S.Shahini
 * @since 11/23/16
 */

public class BannerFragment extends Fragment {
    private Banner banner;

    public BannerFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        banner = getArguments().getParcelable("banner");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (banner != null) {
            View view = inflater.inflate(R.layout.item_banner, null);
            AdjustableImageView imageView = view.findViewById(R.id.adj_img);
            TextView bannerTitle = view.findViewById(R.id.banner_title);
            TextView commentNums = view.findViewById(R.id.comment_nums);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(banner.getScaleType());
            if (banner instanceof DrawableBanner) {
                DrawableBanner drawableBanner = (DrawableBanner) banner;
                imageView.setImageResource(drawableBanner.getDrawable());
            } else {
                final RemoteBanner remoteBanner = (RemoteBanner) banner;
                if (remoteBanner.getErrorDrawable() == null && remoteBanner.getPlaceHolder() == null) {
                    GlideUtils.loadImageViewLoading(getContext(), remoteBanner.getUrl(), imageView);
                    bannerTitle.setText(remoteBanner.getTitle());
                    commentNums.setText(remoteBanner.getNums());
                } else {
                    if (remoteBanner.getPlaceHolder() != null && remoteBanner.getErrorDrawable() != null) {
                        GlideUtils.loadImageViewLoading(getContext(), remoteBanner.getUrl(), imageView);
                        bannerTitle.setText(remoteBanner.getTitle());
                        commentNums.setText(remoteBanner.getNums());
                    } else if (remoteBanner.getErrorDrawable() != null) {
                        GlideUtils.loadImageViewLoading(getContext(), remoteBanner.getUrl(), imageView);
                        bannerTitle.setText(remoteBanner.getTitle());
                        commentNums.setText(remoteBanner.getNums());
                    } else if (remoteBanner.getPlaceHolder() != null) {
                        GlideUtils.loadImageViewLoading(getContext(), remoteBanner.getUrl(), imageView);
                        bannerTitle.setText(remoteBanner.getTitle());
                        commentNums.setText(remoteBanner.getNums());
                    }
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnBannerClickListener onBannerClickListener = banner.getOnBannerClickListener();
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onClick(banner.getPosition());
                    }
                }
            });

            return view;
        } else {
            throw new RuntimeException("banner cannot be null");
        }
    }

    public static BannerFragment newInstance(Banner banner) {
        Bundle args = new Bundle();
        args.putParcelable("banner", banner);
        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
