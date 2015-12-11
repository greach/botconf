package grooid.lib.wpapi.volley

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.util.LruCache

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import groovy.transform.CompileStatic

@CompileStatic
class VolleySingleton {
    private static VolleySingleton mInstance
    private RequestQueue mRequestQueue
    private ImageLoader mImageLoader
    private static Context mCtx

    private VolleySingleton(Context context) {
        mCtx = context
        mRequestQueue = getRequestQueue()

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    Bitmap getBitmap(String url) {
                        cache.get(url)
                    }

                    @Override
                    void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap)
                    }
                });
    }

    static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context)
        }
        mInstance;
    }

    RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext())
        }
        mRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req)
    }

    ImageLoader getImageLoader() {
        mImageLoader
    }
}