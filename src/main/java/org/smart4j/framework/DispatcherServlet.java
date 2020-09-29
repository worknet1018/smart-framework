package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求方法与请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();
        if (requestPath.equals("/favicon.ico")){
            return;
        }
        //获取 Action 处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler != null) {
            //获取 ControllerBean 类及其Bean 实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            Param param;
            if (UploadHelper.isMultipart(request)){
                param =UploadHelper.createParam(request);
            }else {
                param= RequestHelper.createParam(request);
            }
            Object result;
            Method actionMethod =handler.getActionMethod();
            if (param.isEmpty()){
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
            }else {
                result =ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            }

            if (result instanceof View){
                handlerViewResult((View) result, request,response);
            }else if(result instanceof Data){
                handlerDataResult((Data)result,response);
            }
        }
            /*//创建请求对象参数
            Map<String,Object> fieldMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                fieldMap.put(paramName,paramValue);
            }
            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params =StringUtil.splitString(body,"&");
                if (ArrayUtil.isNotEmpty(params)){
                    for (String param :params){
                        String[] array = StringUtil.splitString(param,"=");
                        if (ArrayUtil.isNotEmpty(array) && array.length == 2){
                            String paramName = array[0];
                            String paramValue =array[2];
                            fieldMap.put(paramName,paramValue);
                        }
                    }
                }
            }

            Param param = new Param(fieldMap);

            //调用 Action 方法
            //Method actionMethod =handler.getActionMethod();
            //Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            Object result;
            Method actionMethod =handler.getActionMethod();
            if (param.isEmpty()){
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            }else {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            }
            //处理 Action 方法返回值
            if (result instanceof View){
                //返回 JSP 页面
                View view =(View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)){
                    response.sendRedirect(request.getContextPath() + path);
                }else {
                    Map<String,Object> model = view.getModel();
                    for (Map.Entry<String,Object> entry :model.entrySet()){
                        request.setAttribute(entry.getKey(),entry.getValue());
                    }
                    request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request,response);
                }
            }else if (result instanceof Data){
                //返回 JSON 数据
                Data data =(Data) result;
                Object model = data.getModel();
                if (model != null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    String json =JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }*/
    }

    private void handlerDataResult(Data data, HttpServletResponse response) throws IOException{
        Object model =data.getModel();
        if (model !=null){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer=response.getWriter();
            String json =JsonUtil.toJson(model);
            writer.write(json   );
            writer.flush();
            writer.close();

        }

    }

    private void handlerViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        String path =view.getPath();
        if (StringUtil.isNotEmpty(path)){
            response.sendRedirect(request.getContextPath()+path);
        }else {
            Map<String,Object> model =view.getModel();
            for (Map.Entry<String ,Object> entry:model.entrySet()){
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);

        }

    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化相关 Helper 类
        HelperLoader.init();
        //获取 ServletContext 对象（用于注册 Servlet)
        ServletContext servletContext = servletConfig.getServletContext();
        //注册处理 JSP 的 Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() +"*");
        //注册处理静态资源的默认 Servlet
        ServletRegistration defaultServlet =servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() +"*");

        UploadHelper.init(servletContext);
    }
}
