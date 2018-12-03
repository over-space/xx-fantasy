package com.fantasy.xxweb.ftl;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class XXFreeMarkerView extends FreeMarkerView {

    private static final String CONTEXT_PATH = "requestURL";

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        model.put(CONTEXT_PATH, request.getContextPath());
        super.exposeHelpers(model, request);
    }
}
