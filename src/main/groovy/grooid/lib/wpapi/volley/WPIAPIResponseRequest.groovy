package grooid.lib.wpapi.volley

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser
import grooid.lib.wpapi.WPAPIResponseLink
import grooid.lib.wpapi.WPIAPIResponse
import groovy.transform.CompileStatic
/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
@CompileStatic
class WPIAPIResponseRequest extends Request<WPIAPIResponse> {

    private final static String kWPAPIReponseHeaderTotal = "X-WP-Total";
    private final static String kWPAPIReponseHeaderTotalPages = "X-WP-TotalPages";
    private final static String kWPAPIReponseHeaderLink = "Link";

    private final Listener<WPIAPIResponse> mListener

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the WPIAPIResponseRequest response
     * @param errorListener Error listener, or null to ignore errors
     */
    WPIAPIResponseRequest(int method, String url, Listener<WPIAPIResponse> listener,  ErrorListener errorListener) {
        super(method, url, errorListener)
        mListener = listener
    }

    @Override
    protected void deliverResponse(WPIAPIResponse response) {
        mListener.onResponse(response)
    }

    @Override
    protected Response<WPIAPIResponse> parseNetworkResponse(NetworkResponse response) {
        String parsed
        Integer total
        Integer totalPages
        String path
        try {
            total = Integer.valueOf(response.headers[kWPAPIReponseHeaderTotal])
            totalPages = Integer.valueOf(response.headers[kWPAPIReponseHeaderTotalPages])
            List responseLinks = WPAPIResponseLink.extractResponseLinksFromString(response.headers[kWPAPIReponseHeaderLink])
            path = WPAPIResponseLink.nextPathForResponseLinks(responseLinks)
            String charset = HttpHeaderParser.parseCharset(response.headers)
            parsed = new String(response.data)

        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data)
        }

        Response.success(new WPIAPIResponse(total: total, totalPages: totalPages, path: path, objectNotation: parsed), HttpHeaderParser.parseCacheHeaders(response))
    }
}
