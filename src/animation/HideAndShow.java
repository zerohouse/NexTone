/*
 * Copyright (C) 2010 The Android Open Source Project
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

package animation;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

/**
 * This application demonstrates how to use LayoutTransition to automate
 * transition animations as items are hidden or shown in a container.
 */
@SuppressLint("NewApi")
public class HideAndShow extends Activity {

	ViewGroup container = null;
	private LayoutTransition mTransitioner;

	public HideAndShow(ViewGroup container) {
		this.container = container;
	}

	public void animate(boolean flip) {
		resetTransition();

		long duration;
		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
		mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
		if (flip)
			setupCustomAnimations(); // 플립애니메이션
		duration = 500;
		mTransitioner.setDuration(duration);
	}

	public void animateForHelper() {
		resetTransition();

		long duration;
		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
		mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
		setupCustomAnimations(); // 플립애니메이션
		duration = 200;
		mTransitioner.setDuration(duration);
	}

	public void resetTransition() {
		mTransitioner = new LayoutTransition();
		container.setLayoutTransition(mTransitioner);
	}

	public void setupCustomAnimations() {
		// Changing while Adding
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0,
				1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
				1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
				1f, 0f, 1f);
		final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
				this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX,
				pvhScaleY).setDuration(
				mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		changeIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// Changing while Removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
				"rotation", kf0, kf1, kf2);
		final ObjectAnimator changeOut = ObjectAnimator
				.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight,
						pvhBottom, pvhRotation)
				.setDuration(
						mTransitioner
								.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
				changeOut);
		changeOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotation(0f);
			}
		});

		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f,
				0f).setDuration(
				mTransitioner.getDuration(LayoutTransition.APPEARING));
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f,
				90f).setDuration(
				mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}
}