package com.isaacrf.imageurlview.listeners;


/**
 * Listener to get a callback when an URL image request asynctask is completed
 * 
 * @author Isaac
 *
 */
public interface OnBitmapFailureListener {
	/**
	 * Callback to be invoked when the bitmap URL async task fails
	 * @param errorMessage Custom error message
	 * @param exception Exception thrown
	 */
	void onBitmapFailure(String errorMessage, Exception exception);
}
