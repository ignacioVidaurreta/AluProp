package ar.edu.itba.paw.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("error")
public class ErrorController {

//    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ModelAndViewPopulator navigationUtility;
//
//    @RequestMapping(value = "errors", method= RequestMethod.GET)
//    public ModelAndView renderErrorPage(@ModelAttribute FilteredSearchForm searchForm,
//                                        HttpServletRequest httpRequest){
//
//        ModelAndView errorPage = new ModelAndView();
//        int httpErrorCode = getErrorCode(httpRequest);
//
//        switch (httpErrorCode){
//            case HttpURLConnection.HTTP_BAD_REQUEST:
//            case HttpURLConnection.HTTP_NOT_FOUND:
//                errorPage.setViewName("redirect:/404");
//                break;
//            case HttpURLConnection.HTTP_FORBIDDEN:
//                errorPage.setViewName("redirect:/403");
//                break;
//            case HttpURLConnection.HTTP_BAD_METHOD:
//            case HttpURLConnection.HTTP_INTERNAL_ERROR:
//                errorPage.setViewName("redirect:/500");
//                break;
//            default:
//                errorPage = navigationUtility.mavWithNavigationAttributes("errorPage");
//                // TODO: hardcoded english message
//                errorPage.addObject("errorMsg", "Unhandled Error. Something went wrong!");
//                break;
//        }
//
//        return errorPage;
//    }
    @GET
    public Response error(@Context HttpServletRequest request){
        return Response.status(getErrorCode(request)).build();
    }

    private int getErrorCode(HttpServletRequest request) {
        return (Integer) request
                .getAttribute("javax.servlet.error.status_code");
    }
}
