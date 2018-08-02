package me.geeksploit.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import me.geeksploit.bakingapp.data.StepEntity;

/**
 * A fragment representing a single Recipe Step detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item_id";

    private StepEntity mItem;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private long mPlayerPosition;
    private ImageView mImage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // TODO: use a Loader to load content from a content provider.
            mItem = (StepEntity) getArguments().getSerializable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong("test");
        }

        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);

        mPlayerView = rootView.findViewById(R.id.step_video);
        mImage = rootView.findViewById(R.id.step_image);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.recipestep_detail)).setText(mItem.getDescription());
            if (!mItem.getThumbnailURL().isEmpty()) {
                Context context = getContext();
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.placeholder_recipe_image)
                        .error(R.drawable.placeholder_recipe_image);
                Glide.with(context)
                        .load(mItem.getThumbnailURL())
                        .apply(options)
                        .into(mImage);
            }
        }

        return rootView;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (mItem == null || mItem.getVideoURL().isEmpty()) return;

        initializePlayer(Uri.parse(mItem.getVideoURL()));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer == null) return;
        mPlayerPosition = mExoPlayer.getCurrentPosition();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("test", mPlayerPosition);
    }

    /**
     * Initialize ExoPlayer
     *
     * @param mediaUri the Uri of the sample to play
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer != null) return;

        Context context = getContext();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        String userAgent = getString(R.string.app_name);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaUri);

        if (mPlayerPosition != C.TIME_UNSET) {
            mExoPlayer.seekTo(mPlayerPosition);
        }
        mExoPlayer.prepare(mediaSource, false, false);

        mPlayerView.setPlayer(mExoPlayer);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void releasePlayer() {
        if (mExoPlayer == null) return;
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
}
