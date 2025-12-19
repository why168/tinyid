package com.xiaoju.uemc.tinyid.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
public class TinyIdHttpUtils {

    private static final Logger logger = Logger.getLogger(TinyIdHttpUtils.class.getName());

    private TinyIdHttpUtils() {
    }

    public static String post(String url, Integer readTimeout, Integer connectTimeout) {
        return post(url, null, readTimeout, connectTimeout);
    }

    public static String post(String url, Map<String, String> form, Integer readTimeout, Integer connectTimeout) {
        HttpURLConnection conn = null;
        OutputStreamWriter os = null;
        BufferedReader rd = null;
        StringBuilder param = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String line;
        String response = null;
        
        if (form != null) {
            for (Map.Entry<String, String> entry : form.entrySet()) {
                String key = entry.getKey();
                if (!param.isEmpty()) {
                    param.append("&");
                }
                param.append(key).append("=").append(entry.getValue());
            }
        }
        
        try {
            conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(connectTimeout);
            conn.setUseCaches(false);
            conn.connect();
            
            os = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            os.write(param.toString());
            os.flush();
            
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            response = sb.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error post url:" + url + param, e);
        } finally {
            closeQuietly(os);
            closeQuietly(rd);
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.log(Level.WARNING, "error close resource", e);
            }
        }
    }
}
