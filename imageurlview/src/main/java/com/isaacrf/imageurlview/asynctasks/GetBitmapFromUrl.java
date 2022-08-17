package com.isaacrf.imageurlview.asynctasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Pair;

import com.isaacrf.imageurlview.listeners.OnBitmapFailureListener;
import com.isaacrf.imageurlview.listeners.OnBitmapLoadedListener;

/**
 * Loads an image from a URL asynchronously and stores it in a Bitmap
 * 
 * @author Isaac
 *
 */
public class GetBitmapFromUrl extends AsyncTask<URL, Void, Pair<String, Bitmap>> {
	//Event listeners
	private OnBitmapLoadedListener onBitmapLoadedListener;
	private OnBitmapFailureListener onBitmapFailureListener;

	private boolean isCancelled = false;
	private InputStream urlInputStream;	
	
	//Getters and Setters
	/**
	 * Callback to be invoked when the bitmap is successfully loaded from URL
	 * @return OnBitmapLoadedListener - The callback that will run
	 */
	public OnBitmapLoadedListener getOnBitmapLoadedListener() {
		return onBitmapLoadedListener;
	}
	/**
	 * Set the callback to be invoked when the bitmap is successfully loaded from URL
	 * @param onBitmapLoaded Callback that will run
	 */
	public void setOnBitmapLoadedListener(OnBitmapLoadedListener onBitmapLoaded) {
		this.onBitmapLoadedListener = onBitmapLoaded;
	}
	/**
	 * Callback to be invoked when the async task fails to load the bitmap from URL
	 * @return OnBitmapFailureListener - Callback that will run
	 */
	public OnBitmapFailureListener getOnBitmapFailureListener() {
		return onBitmapFailureListener;
	}
	/**
	 * Set the callback to be invoked when the async task fails to load the bitmap from URL
	 * @param onBitmapFailureListener Callback that will run
	 */
	public void setOnBitmapFailureListener(
			OnBitmapFailureListener onBitmapFailureListener) {
		this.onBitmapFailureListener = onBitmapFailureListener;
	}

	/**
	 * Simple constructor
	 */
	public GetBitmapFromUrl()
	{
		
	}
	
	@Override
	protected Pair<String, Bitmap> doInBackground(URL... params) {
		try 
		{
			//Get the bitmap image from URL
			URLConnection con = params[0].openConnection();
			con.setUseCaches(true);        
			urlInputStream = con.getInputStream();

			//Return the URL and the decoded Bitmap
			return new Pair<String, Bitmap>(params[0].toString(), BitmapFactory.decodeStream(urlInputStream));
		} 
		catch (IOException e) 
		{
			return null;
		} 
		finally 
		{
			//Close and delete Input Stream
			if (this.urlInputStream != null) 
			{
				try {
					this.urlInputStream.close();
				} 
				catch (IOException e) {
					
				} 
				finally {
					this.urlInputStream = null;
				}
			}
		}
	}

	@Override
	protected void onPostExecute(Pair<String, Bitmap> bitmap) {
		if (!isCancelled) 
		{
			if (bitmap != null)
			{
				//Return the URL and the decoded Bitmap
				onBitmapLoadedListener.onBitmapLoaded(bitmap.first, bitmap.second);
			}
			else
			{
				//Return error if the Bitmap couldn't be loaded
				onBitmapFailureListener.onBitmapFailure("Failed to load image", null);
			}
		}
	}

	@Override
	protected void onCancelled() {
		isCancelled = true;
		
		if (this.urlInputStream != null) 
		{
			try {
				this.urlInputStream.close();
			} 
			catch (IOException e) {
				
			} 
			finally {
				this.urlInputStream = null;
			}
		}
		
		super.onCancelled();
	}
}
