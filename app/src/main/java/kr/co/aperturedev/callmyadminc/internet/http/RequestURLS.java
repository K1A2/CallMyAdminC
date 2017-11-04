package kr.co.aperturedev.callmyadminc.internet.http;

/**
 * Created by 5252b on 2017-10-29.
 */

public interface RequestURLS {
    String DEFAULT_URL = "http://aperturedev.co.kr/callmyadmin/";

    // Create
    String DEVICE_REGIST = DEFAULT_URL + "create/device-regi.jsp";
    String DEVICE_SERVER_REGIST = DEFAULT_URL + "create/admin-serverregi.jsp";

    // Update

    // Read
    String DEVICE_CHECK_REGIST = DEFAULT_URL + "read/device-isregist.jsp";
    String DEVICE_AUTHME = DEFAULT_URL + "read/device-authme.jsp";
    String SERVER_SEARCH = DEFAULT_URL + "read/server-search.jsp";

    // Delete
}
