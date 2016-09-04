package com.monkporter.zafran.generic;

import android.app.Activity;
import android.app.ProgressDialog;


public class ShowLoader {
	private static ShowLoader loadDialog = null;
	private static Component iComponent;
	private static Activity iActivity = null;
	private static String progressValue;

	private ShowLoader() {

	}

	ProgressDialog dialog = null;

	public static ShowLoader getInstance(Activity mActivity, String loadingValue) {
		iActivity = mActivity;
		progressValue = loadingValue;
		iComponent = new Component();
		loadDialog = new ShowLoader();
		return loadDialog;
	}

	public void run(boolean isLocked) {

		if(isLocked) Component.lockScreenOrientation(iActivity);
		CustomProgressDialog.showProgressDialog(iActivity, progressValue, false);
	}
	public void dismis(boolean isLocked){
		if(isLocked) iComponent.unlockScreenOrientation(iActivity);
		CustomProgressDialog.removeDialog();
	}
	public boolean isShowing()
	{
		return CustomProgressDialog.isShowDialog();
	}
}
