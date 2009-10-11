package org.apache.wicket.ng.request.mapper.mount;

import org.apache.wicket.ng.request.Request;
import org.apache.wicket.ng.request.RequestHandler;

/**
 * TODO javadoc, explain "parameters resolved from the mount"
 * 
 * @author igor.vaynberg
 * 
 */
public interface MountedRequestMapper
{
    /**
     * Returns {@link RequestHandler} for the request or <code>null</code> if the encoder does not
     * recognize the URL.
     * 
     * @param request
     *            provides access to request data (i.e. Url and Parameters)
     * @param mountParams
     *            parameters resolved from the mount
     * @return RequestHandler instance or <code>null</code>
     */
    RequestHandler mapRequest(Request request, MountParameters mountParams);

    /**
     * Returns the score representing how compatible this request mapper is to processing the given
     * request. When a request comes in all mappers are scored and are tried in order from highest
     * score to lowest.
     * <p>
     * A good criteria for calculating the score is the number of matched url segments. For example
     * when there are two encoders for mounted page, one mapped to <code>/foo</code> another to
     * <code>/foo/bar</code> and the incomming reqest URL is </code>/foo/bar/baz</code>, the encoder
     * mapped to <code>/foo/bar</code> will handle the request first as it has matching segments
     * count of 2 while the first one has only matching segments count of 1.
     * <p>
     * Note that the method can return value greater then zero even if the encoder can not decode
     * the request.
     * 
     * @param request
     * @return count of matching segments
     */
    int getCompatibilityScore(Request request);

    /**
     * Returns the {@link Mount} for given {@link RequestHandler} or <code>null</code> if the
     * encoder does not recognize the request handler.
     * 
     * @param requestHandler
     * @return Url instance or <code>null</code>.
     */
    Mount mapHandler(RequestHandler requestHandler);
}
