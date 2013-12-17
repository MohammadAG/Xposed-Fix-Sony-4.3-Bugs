package com.mohammadag.fixsony43bugs;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class FacepalmMod implements IXposedHookInitPackageResources {

	private static final String PACKAGE_NAME = "com.android.packageinstaller";

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals(PACKAGE_NAME))
			return;

		float scale = resparam.res.getDisplayMetrics().density;
		final int topPixels = (int) (12 * scale + 0.5f);
		final int leftRightPixels = (int) (16 * scale + 0.5f);

		resparam.res.hookLayout(PACKAGE_NAME, "layout", "app_details", new XC_LayoutInflated() {

			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				RelativeLayout layout = (RelativeLayout) liparam.view;
				layout.setMinimumHeight(-1);
				layout.setPadding(leftRightPixels, 0, leftRightPixels, 0);

				ImageView icon =
						(ImageView) layout.findViewById(liparam.res.getIdentifier("app_icon",
								"id", PACKAGE_NAME));
				RelativeLayout.LayoutParams layoutParams = 
						(RelativeLayout.LayoutParams)icon.getLayoutParams();
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				layoutParams.setMargins(0, topPixels, 0, 0);
				icon.setLayoutParams(layoutParams);

				FrameLayout divider = (FrameLayout) layout.findViewById(liparam.res.getIdentifier("top_divider",
						"id", PACKAGE_NAME));

				divider.setPaddingRelative(0, topPixels, 0, 0);

				View progressBar = layout.findViewById(liparam.res.getIdentifier("progress_bar",
						"id", PACKAGE_NAME));
				FrameLayout.LayoutParams progressBarParams = 
						(FrameLayout.LayoutParams)icon.getLayoutParams();
				progressBarParams.setMargins(0, topPixels*2, 0, 0);
				progressBar.setLayoutParams(progressBarParams);

				layout.invalidate();
			}
		});
	}

}
