package grooid.lib.wpapi

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import grooid.lib.wpapi.volley.VolleySingleton
import grooid.lib.wpapi.volley.WPIAPIResponseRequest
import groovy.transform.CompileStatic

@CompileStatic
class WPAPIFetcher {

    private static final int TIMEOUT_MS = 20_000 // 20 seconds =  20 * 1000

    static final String TAG = WPAPIFetcher.class.getSimpleName()

    void fetchPosts(Context ctx, String baseUrlStr, String type, Closure completion) {

        String urlStr = linkFor(baseUrlStr, type)

        def block = { error, objectNotation, total, totalPages, link ->
            if (completion) {
                completion(error, objectNotation, total, totalPages, link)
            }
        };
        fetchPostLink(ctx, urlStr, block)

    }

    String linkFor(String baseUrl, String type, int posts_per_page = 100) {
        "${baseUrl}/wp-json/posts?type=${type}&filter[posts_per_page]=${posts_per_page}"
    }

    void fetchPostLink(Context ctx, String link, Closure completion) {

        WPIAPIResponseRequest wpapiRequest = new WPIAPIResponseRequest(Request.Method.GET, link,
                new Response.Listener<WPIAPIResponse>() {
                    @Override
                    void onResponse(WPIAPIResponse response) {
                        completion(response.objectNotation, response.total, response.totalPages, response.path)
                    }
                }, new Response.ErrorListener() {

            @Override
            void onErrorResponse(VolleyError error) {
                Log.e(TAG, "On error:" + error)
            }
        })
        wpapiRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        VolleySingleton.getInstance(ctx).getRequestQueue().add(wpapiRequest)
    }
}