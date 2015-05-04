package com.smile.volleythirdpartylibraryextension.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class LoadingDialogController {
	
	private static LoadingDialogController INSTANCE = null;
	private LoadingDialog mLoadingDialog;
	private int mReferenceCounter = 0;
	public LoadingDialogController() {
	}
	
	public synchronized static LoadingDialogController getInstance(){
		if(INSTANCE == null){
			INSTANCE = new LoadingDialogController();
		}
		return INSTANCE;
	}
	
	public synchronized  void show(Context pContext){
		if(mReferenceCounter == 0){
			if(this.mLoadingDialog == null ){
				this.initialLoadingDialog(pContext);
			}
			if(this.mLoadingDialog != null){
//				if(!this.mLoadingDialog.isShowing()){
					this.mLoadingDialog.show();
					System.out.println("Show Dialog");
//				}
			}
		}
		this.mReferenceCounter += 1;
		System.out.println(" show mReferenceCounter : "+this.mReferenceCounter);
	}
	
	public synchronized void dismiss(){
		if(this.mReferenceCounter == 1){
			if(this.mLoadingDialog != null && this.mLoadingDialog.isShowing()){
				this.mLoadingDialog.dismiss();
				System.out.println("Dismiss Dialog");
			}
		}
		this.mReferenceCounter -= 1;
		System.out.println(" dismiss mReferenceCounter : "+this.mReferenceCounter);
	}
	private void initialLoadingDialog(Context pContext){
			this.mLoadingDialog = new LoadingDialog(pContext);
	}
	
	private static class LoadingDialog extends Dialog{

		public LoadingDialog(Context context, int theme) {
			super(context, theme);
			this.initial(context);
		}
		protected LoadingDialog(Context context, boolean cancelable,
				OnCancelListener cancelListener) {
			super(context, cancelable, cancelListener);
			this.initial(context);
		}
		public LoadingDialog(Context context) {
			super(context);
			this.initial(context);
		}
		private void initial(Context pContext){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	        setContentView(this.doInitialLoadingDialogContent(pContext));
	        setCanceledOnTouchOutside(false);
		}
		protected View doInitialLoadingDialogContent(Context pContext){
			LinearLayout linearLayout = new LinearLayout(pContext);
			linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			linearLayout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			linearLayout.setGravity(Gravity.CENTER);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			ProgressBar progressBar = new ProgressBar(pContext,null,android.R.attr.progressBarStyle);
			int padding = 13;
			progressBar.setPadding(padding, padding, padding, padding);
			progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			progressBar.setBackgroundDrawable(this.doCreateShape());
			linearLayout.addView(progressBar);
			return linearLayout;
		}
		private GradientDrawable doCreateShape(){
			int cornersRadius = 12;
			int padding = 13;
		    int solidColor= Color.parseColor("#5F000000");
		    GradientDrawable gd = new GradientDrawable();
		    gd.setColor(solidColor);
		    gd.setCornerRadius(cornersRadius);
		    ShapeDrawable pShapeDrawable = new ShapeDrawable();
		    pShapeDrawable.setPadding(padding, padding, padding, padding);
		    return gd;
		}
	}
	
}
