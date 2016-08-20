package org.b3log.xiaov.service;

import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.urlfetch.HTTPRequest;
import org.b3log.latke.urlfetch.HTTPResponse;
import org.b3log.latke.urlfetch.URLFetchService;
import org.b3log.latke.urlfetch.URLFetchServiceFactory;
import org.b3log.xiaov.util.XiaoVs;

/**
 * <a href="http://www.itpk.cn">ITPK</a> bot query service.
 *
 * @author <a href="http://relyn.cn">Relyn</a>
 * @version 1.0.0.0, Aug 8, 2016
 * @since 2.0.1
 */
@Service
public class ItpkQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ItpkQueryService.class.getName());

    /**
     * ITPK Robot URL.
     */
    private static final String ITPK_API = XiaoVs.getString("itpk.api");

    /**
     * ITPK Robot API.
     */
    private static final String ITPK_KEY = XiaoVs.getString("itpk.key");

    /**
     * ITPK Robot Key.
     */
    private static final String ITPK_SECRET = XiaoVs.getString("itpk.secret");

    /**
     * URL fetch service.
     */
    private static final URLFetchService URL_FETCH_SVC = URLFetchServiceFactory.getURLFetchService();

    /**
     * Chat with ITPK Robot.
     *
     * @param msg the specified message
     * @return robot returned message, return {@code null} if not found
     */
    public String chat(String msg) {
        if (StringUtils.isBlank(msg)) {
            return null;
        }

        if (msg.startsWith(XiaoVs.QQ_BOT_NAME + " ")) {
            msg = msg.replace(XiaoVs.QQ_BOT_NAME + " ", "");
        }
        if (msg.startsWith(XiaoVs.QQ_BOT_NAME + "，")) {
            msg = msg.replace(XiaoVs.QQ_BOT_NAME + "，", "");
        }
        if (msg.startsWith(XiaoVs.QQ_BOT_NAME + ",")) {
            msg = msg.replace(XiaoVs.QQ_BOT_NAME + ",", "");
        }
        if (msg.startsWith(XiaoVs.QQ_BOT_NAME)) {
            msg = msg.replace(XiaoVs.QQ_BOT_NAME, "");
        }

        if (StringUtils.isBlank(msg)) {
            return null;
        }

        final HTTPRequest request = new HTTPRequest();
        request.setRequestMethod(HTTPRequestMethod.POST);

        try {
            request.setURL(new URL(ITPK_API));
            final String body = "api_key=" + URLEncoder.encode(ITPK_KEY, "UTF-8")
                    + "&limit=8"
                    + "&api_secret=" + URLEncoder.encode(ITPK_SECRET, "UTF-8")
                    + "&question=" + URLEncoder.encode(msg, "UTF-8");
            request.setPayload(body.getBytes("UTF-8"));
            final HTTPResponse response = URL_FETCH_SVC.fetch(request);

            return new String(response.getContent(), "UTF-8").substring(1);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Chat with ITPK Robot failed", e);
        }

        return null;
    }
}
