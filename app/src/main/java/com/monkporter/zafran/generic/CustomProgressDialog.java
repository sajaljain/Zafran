
package com.monkporter.zafran.generic;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.monkporter.zafran.R;


public class CustomProgressDialog
{
	protected static Dialog mDialog = null;
	public static TextView progressDescription;

	public CustomProgressDialog() 
	{
		super();
	}

	public static void showProgressDialog(Context mContext, String loaderText, boolean cancellable)
	{
		removeDialog();
		mDialog = new Dialog(mContext);
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		View layout = mInflater.inflate(R.layout.progress_dialog, null);
		progressDescription=(TextView)layout.findViewById(R.id.progressdescription);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(layout);
	    final Window window = mDialog.getWindow();
	    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		progressDescription.setText(loaderText);
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
				switch (keyCode){
				case KeyEvent.KEYCODE_BACK:
					//GetHttpConnector.isConnectivity = true;
					removeDialog();
					return true;
				case KeyEvent.KEYCODE_SEARCH:
					return true;
				}

				return false;
			}
		});
		mDialog.setCancelable(cancellable);
		if (mDialog != null) 
		{
			try {
				mDialog.show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}
	
	public static void showProgressDialogWithoutOpac(Context mContext, String text, boolean cancellable) 
	{
		//removeDialog();
		mDialog = new Dialog(mContext);
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		if (text.equals("")){
		//	tvProgressDescription.setVisibility(View.GONE);
		}
		else{
		//	tvProgressDescription.setText(text);
		}
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
				switch (keyCode){
				case KeyEvent.KEYCODE_BACK:
		//			GetHttpConnector.isConnectivity = true;
					removeDialog();
					return true;
				case KeyEvent.KEYCODE_SEARCH:
					return true;
				}

				return false;
			}
		});
		mDialog.setCancelable(cancellable);
		if (mDialog != null) 
		{
			try {
				mDialog.show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}

	

	public static boolean isShowDialog(){
		if(mDialog != null){
			return mDialog.isShowing();
		}
		return false;
	}
	public static void removeDialog(){
		if (mDialog != null) {
			try{
			mDialog.dismiss();
			mDialog = null;
			}
			catch(Exception e){

			}
		}

	}
}
