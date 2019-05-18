package ar.edu.itba.paw.webapp.Utilities;

import org.springframework.web.servlet.ModelAndView;

import java.net.HttpURLConnection;

public class StatusCodeUtility {

    public static ModelAndView parseStatusCode(int statusCode, String successJsp) {
        switch (statusCode) {
            case HttpURLConnection.HTTP_OK:
                return new ModelAndView(successJsp);
            case HttpURLConnection.HTTP_NOT_FOUND:
                return new ModelAndView("notFound");
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                return new ModelAndView("internalServerError");
            default:
                return new ModelAndView("internalServerError");
        }
    }
}