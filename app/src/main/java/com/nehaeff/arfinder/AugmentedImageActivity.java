/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nehaeff.arfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.nehaeff.arfinder.helpers.SnackbarHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AugmentedImageActivity extends AppCompatActivity {

    private static final String EXTRA_ITEM_ID =
            "com.nehaeff.arfinder.item_id";
    private static final String EXTRA_ROOM_ID =
            "com.nehaeff.arfinder.room_id";

  private ArFragment arFragment;
  private ImageView fitToScanView;
  private Button mButton_add_anchor;
  ModelRenderable polyPostRenderable;
  private ItemLab mItemLab;
  private RoomLab mRoomLab;

  // Augmented image and its associated center pose anchor, keyed by the augmented image in
  // the database.
  private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();

    public static Intent newIntent(Context packageContext, UUID itemId, UUID roomId) {
        Intent intent = new Intent(packageContext, AugmentedImageActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ar_fragment);

    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
    fitToScanView = findViewById(R.id.image_view_fit_to_scan);

    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

    mButton_add_anchor = (Button) findViewById(R.id.button_ar_add_anchor);

    ModelRenderable.builder()
            .setSource(this, Uri.parse("poly.sfb"))
            .build()
            .thenAccept(renderable -> polyPostRenderable = renderable)
            .exceptionally(throwable -> {
              Toast toast =
                      Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
              toast.setGravity(Gravity.CENTER, 0, 0);
              toast.show();
              return null;
            });

      arFragment.setOnTapArPlaneListener(
              (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                  if (polyPostRenderable == null){
                      return;
                  }

                  Anchor anchor = hitresult.createAnchor();
                  //mItemLab.setPoseItem(anchor.getPose());

                  AnchorNode anchorNode = new AnchorNode(anchor);
                  anchorNode.setParent(arFragment.getArSceneView().getScene());

                  TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                  lamp.setParent(anchorNode);
                  lamp.setRenderable(polyPostRenderable);
                  lamp.select();

                  List<Item> items = mItemLab.getItems();

                  Item item = mItemLab.getItem(mItemLab.getSelectedItemId());
                  item.setArFlag(1);
                  item.setPose(anchor.getPose());
                  item.setDescription("govno");
                  mItemLab.updateItem(item);

                  items = mItemLab.getItems();
              }
      );

      mItemLab = ItemLab.get(this);
      mRoomLab = RoomLab.get(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (augmentedImageMap.isEmpty()) {
      fitToScanView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Registered with the Sceneform Scene object, this method is called at the start of each frame.
   *
   * @param frameTime - time since last frame.
   */
  private void onUpdateFrame(FrameTime frameTime) {
    Frame frame = arFragment.getArSceneView().getArFrame();

    // If there is no frame, just return.
    if (frame == null) {
      return;
    }

    Collection<AugmentedImage> updatedAugmentedImages =
        frame.getUpdatedTrackables(AugmentedImage.class);
    for (AugmentedImage augmentedImage : updatedAugmentedImages) {
      switch (augmentedImage.getTrackingState()) {
        case PAUSED:
          // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
          // but not yet tracked.
          String text = "Detected Image " + augmentedImage.getIndex();
          SnackbarHelper.getInstance().showMessage(this, text);
          break;

        case TRACKING:
          // Have to switch to UI Thread to update View.
          fitToScanView.setVisibility(View.GONE);

          // Create a new anchor for newly found images.
          if (!augmentedImageMap.containsKey(augmentedImage)) {
            AugmentedImageNode node = new AugmentedImageNode(this);
            node.setImage(augmentedImage);
            augmentedImageMap.put(augmentedImage, node);
            arFragment.getArSceneView().getScene().addChild(node);

            Item item = mItemLab.getItem(mItemLab.getSelectedItemId());

            if (item.getArFlag() > 0) {
                Anchor anchor = augmentedImage.createAnchor(item.getPose());
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                lamp.setParent(anchorNode);
                lamp.setRenderable(polyPostRenderable);
                lamp.select();
            }

/*            mButton_add_anchor.setVisibility(View.VISIBLE);
            mButton_add_anchor.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  Anchor anchor = augmentedImage.createAnchor(mItemLab.getPoseItem());
                  AnchorNode anchorNode = new AnchorNode(anchor);
                  anchorNode.setParent(arFragment.getArSceneView().getScene());

                  TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                  lamp.setParent(anchorNode);
                  lamp.setRenderable(polyPostRenderable);
                  lamp.select();
              }
            });*/
            //FinderArNode node1 = new FinderArNode(augmentedImage, arFragment);
          }
          break;

        case STOPPED:
          augmentedImageMap.remove(augmentedImage);
          break;
      }
    }
  }
}
