package com.isaacrf.imageurlview.main;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.isaacrf.imageurlview.R;
import com.isaacrf.imageurlview.asynctasks.GetBitmapFromUrl;
import com.isaacrf.imageurlview.listeners.OnBitmapFailureListener;
import com.isaacrf.imageurlview.listeners.OnBitmapLoadedListener;

/**
 * ImageView custom control that allows loading images from URLs and supports bitmap caching on memory
 * 
 * @author Isaac
 *
 */
public class ImageUrlView extends ImageView {	
	//Aynctask for loading images from URLs
	private GetBitmapFromUrl getImageUrlTask;
	
	//Enables or disables the use of cache for URL loading
	private boolean useCaches;
	private BitmapCache cache;
	
	/**
	 * Get if the control is using caches for URL loadings
	 * @return true if the control is using caches, false if not
	 */
	public boolean isUseCaches() {
		return useCaches;
	}
	/**
	 * Enable or disable the use of caches for URL loading
	 * @param useCaches true for enabling cache, false for disabling it
	 */
	public void setUseCaches(boolean useCaches) {
		this.useCaches = useCaches;
	}
	
	/**
	 * Get the current Bitmap cache for this image
	 * @return {@link BitmapCache}
	 */
	public BitmapCache getCache() {
		return cache;
	}
	/**
	 * Set the current Bitmap cache for this image
	 * @param cache
	 */
	public void setCache(BitmapCache cache) {
		this.cache = cache;
	}
	
	/**
	 * Basic constructor
	 * 
	 * @param context Activity that created and initialized the control
	 */
	public ImageUrlView(Context context) {
		super(context);

		//Load the loading icon by default
		this.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
	}

	/**
	 * Constructor with attributes
	 * 
	 * @param context Activity that created and initialized the control
	 * @param attrs Attributes to be set
	 */
	public ImageUrlView(Context context, AttributeSet attrs) {
		super(context, attrs);

		//Load the loading icon by default
		this.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
	}

	/**
	 * Style and attributes constructor
	 * 
	 * @param context Activity that created and initialized the control
	 * @param attrs Attributes to be set
	 * @param defStyleAttr Definition of the default style
	 */
	public ImageUrlView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		//Load the loading icon by default
		this.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
	}
	
	/**
	 * Load image from bitmap, pending URL loading tasks are cancelled
	 * 
	 * @param bm Bitmap to be loaded into the view
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		cancelLoading();
		super.setImageBitmap(bm);
	}

	/**
	 * Load image from drawable resources, pending URL loading tasks are cancelled
	 * 
	 * @param drawable Drawable resource to be loaded into the view
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		cancelLoading();
		super.setImageDrawable(drawable);
	}

	/**
	 * Load image from resources, pending URL loading tasks are cancelled
	 * 
	 * @param resId ID of the resource to be loaded into the view
	 */
	@Override
	public void setImageResource(int resId) {
		cancelLoading();
		super.setImageResource(resId);
	}

	/**
	 * Load image from URI, pending URL loading tasks are cancelled
	 * 
	 * @param uri URI of the image to be loaded into the view
	 */
	@Override
	public void setImageURI(Uri uri) {
		cancelLoading();
		super.setImageURI(uri);
	}

	/**
	 * Load image from URL or cache if active/available
	 * 
	 * @param url URL where the image to be loaded is stored
	 */
	public void setImageURL(String url) {
		if (useCaches && cache != null)
		{
			//When enabled, try to get the Bitmap from cache first
			Bitmap bitmap = cache.getBitmapFromMemCache(url);
			
			if (bitmap != null)
			{
				//Load the image from cache
				this.setImageBitmap(bitmap);
			}
			else
			{
				//Image not located in cache, load from URL
				loadFromUrl(url);
			}
		}
		else
		{
			//Cache disabled, load the image directly from URL
			loadFromUrl(url);
		}
	}
	
	/**
	 * Helper method for {@link #setImageURL(String url)}, load an image from URL
	 * 
	 * @param url URL where the image to be loaded is stored
	 */
	private void loadFromUrl(String url)
	{
		try
		{
			cancelLoading();
			getImageUrlTask = new GetBitmapFromUrl();
			getImageUrlTask.setOnBitmapLoadedListener(new OnBitmapLoadedListener() {			
				@Override
				public void onBitmapLoaded(String url, Bitmap bitmap) {
					//Load the bitmap once the async task finishes
					setImageBitmap(bitmap);
					getImageUrlTask = null;
					cache.addBitmapToMemoryCache(url, bitmap);
				}
			});
			getImageUrlTask.setOnBitmapFailureListener(new OnBitmapFailureListener() {				
				@Override
				public void onBitmapFailure(String errorMessage, Exception exception) {
					getImageUrlTask = null;
					
					//If the asynctask fails to get the bitmap, load an error drawable instead
					setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
				}
			});
			getImageUrlTask.execute(new URL(url));
		}
		catch (MalformedURLException e)
		{
			Log.e("IUV", "Wrong URL format", e);
		}
	}

	/**
	 * Cancels current image loading
	 */
	public void cancelLoading() {
		if (getImageUrlTask != null) 
		{
			getImageUrlTask.cancel(true);
			getImageUrlTask = null;
		}
	}
}
