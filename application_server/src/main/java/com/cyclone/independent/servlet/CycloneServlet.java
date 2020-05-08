package com.cyclone.independent.servlet;

import com.alibaba.fastjson.JSONObject;
import com.cyclone.independent.annotation.CycloneController;
import com.cyclone.independent.annotation.CycloneRequestMapping;
import com.cyclone.independent.annotation.CycloneResponseBody;
import com.cyclone.independent.container.InstanceContain;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author: Cyclone-Li
 * @date: 2020/4/30 10:52
 * @description: CycloneServlet
 * @version: 1.0
 */

public class CycloneServlet extends HttpServlet {

    //controller容器
    private InstanceContain instanceContain;

    private static String projectPath = "";

    private static String prefix = "";
    private static String suffix = "";
    //将请求url,method封装, 产生映射关系
    private static Map<String, Method> methodMap = new HashMap();


    public CycloneServlet(InstanceContain instanceContain) {
        this.instanceContain = instanceContain;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
//        String myLocation = config.getInitParameter("myLocation");
        projectPath = getProjectPath();
        //扫描包
        doScanFromPath(projectPath);
    }

    /**
     * 获取项目路径
     *
     * @return
     */
    public String getProjectPath() {

        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//        String path = this.getClass().getClassLoader().getResource("").getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1, path.length());
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");

    }

    /**
     * 创建file对象
     *
     * @param projectPath
     */
    private void doScanFromPath(String projectPath) {
        File file = new File(projectPath);
        parseFile(file);
    }

    /**
     * 解析file对象，
     *
     * @param file
     */

    private void parseFile(File file) {
        String filePath = file.getPath();
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                parseFile(listFile);
            }
        } else {
            if (".jar".equals(filePath.substring(filePath.lastIndexOf(".")))) {
                JarFile jarFile = null;
                String classpath = "";
                int tag;
                try {
                    jarFile = new JarFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    classpath = entries.nextElement().toString();
                    tag = classpath.lastIndexOf(".");
                    if (tag != -1 && ".class".equals(classpath.substring(tag))) {
                        classpath = classpath.replaceAll("/", ".").substring(0, classpath.lastIndexOf("."));
                        parseAnnotation(classpath);
                    }

                }
            }
        }
    }

    /**
     * 解析注解, 将请求地址和method对象封装成Map
     *
     * @param classPath
     */
    private void parseAnnotation(String classPath) {
        try {
            Class<?> clazz = Class.forName(classPath);
            String valuePre = "";
            String valueSuf = "";
            if (clazz.isAnnotationPresent(CycloneController.class)) {
                CycloneRequestMapping crmPre =
                        clazz.getAnnotation(CycloneRequestMapping.class);
                if (crmPre != null) {
                    valuePre = crmPre.value()[0];
                } else {
                    valuePre = "";
                }
                for (Method method : clazz.getMethods()) {
                    if (!method.isSynthetic() && method.isAnnotationPresent(CycloneRequestMapping.class)) {
                        CycloneRequestMapping crmSuf = method.getAnnotation(CycloneRequestMapping.class);
                        if (crmSuf != null) {
                            valueSuf = crmSuf.value()[0];
                            String reqMapping = valuePre + valueSuf;
                            methodMap.put(reqMapping, method);
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 映射和方法执行
     *
     * @param req
     * @param resp
     */
    private void mappingAndHandle(HttpServletRequest req, HttpServletResponse resp) {
        //根据methodMap获取method对象
        Method method = methodMap.get(req.getRequestURI());
        if (method != null) {
            //分析参数类型
            Parameter[] parameters = method.getParameters();
            methodToDo(req, resp, parameters, method);

        } else {
            //请求地址不存在
            resp.setStatus(404);
        }
    }

    /**
     * 方法执行
     *
     * @param req
     * @param resp
     * @param parameters
     * @param method
     */
    private void methodToDo(HttpServletRequest req, HttpServletResponse resp, Parameter[] parameters, Method method) {
        try {
            //不能每调用一次就new一次, 后期优化
            Object instanceObj = instanceContain.getObj(method.getDeclaringClass());

//            Object instanceObj = method.getDeclaringClass().newInstance();
            Object result = null;
            if (parameters != null && parameters.length > 0) {
                //存放参数值数组
                Object[] args = new Object[parameters.length];
                //参数赋值
                argsHandle(req, resp, parameters, args);
                result = method.invoke(instanceObj, args);
            } else {
                result = method.invoke(instanceObj);
            }
            //结果处理
            resultHandle(result, method, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 结果处理
     *
     * @param result
     */
    private void resultHandle(Object result, Method method, HttpServletRequest req, HttpServletResponse resp) {

        String view = String.valueOf(result);
        try {
            if (method.isAnnotationPresent(CycloneResponseBody.class)) {
                String jsonString = JSONObject.toJSONString(result);
                resp.getWriter().write(jsonString);
                resp.getWriter().flush();
            } else {
                if (String.class.equals(result.getClass())) {
                    req.getRequestDispatcher(prefix + view + suffix).forward(req, resp);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resp.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 请求参数处理
     *
     * @param req
     * @param resp
     * @param parameters
     * @param args
     */
    private void argsHandle(HttpServletRequest req, HttpServletResponse resp, Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; i++) {
            //参数名
            String paramName = parameters[i].getName();
            //参数类型
            Class<?> parameterType = parameters[i].getType();
            if (HttpServletRequest.class.equals(parameterType)) {
                args[i] = req;
            } else if (HttpServletResponse.class.equals(parameterType)) {
                args[i] = resp;
            } else if (String.class.equals(parameterType)) {
                args[i] = req.getParameter(paramName);
            } else if (int.class.equals(parameterType)) {
                String parameterValue = req.getParameter(paramName);
                if (parameterValue != null) {
                    args[i] = Integer.parseInt(req.getParameter(paramName));
                }
            } else {
                try {
                    Object paramObj = parameterType.newInstance();
                    //反射为属性赋值
                    for (Field field : parameterType.getDeclaredFields()) {
                        field.setAccessible(true);
                        String attributeName = field.getName();
                        Class<?> attributeType = field.getType();
                        String parameterValue = req.getParameter(attributeName);
                        if (Integer.class.equals(attributeType) || int.class.equals(attributeType)) {
                            field.set(paramObj, Integer.valueOf(parameterValue));
                        } else if (String.class.equals(attributeType)) {
                            field.set(paramObj, new String(parameterValue.getBytes("iso8859-1"), "UTF-8"));
                        }//这里可以完善, 解决对象属性嵌套, 可以封装一个类递归处理
                    }
                    args[i] = paramObj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        mappingAndHandle(req, resp);


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }


}
