package com.hive.myssm.myspringmvc;

import com.hive.myssm.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

//拦截所有以.do2结尾的请求
@WebServlet("*.do2")
public class DispatcherServlet extends ViewBaseServlet {

    //用来保存benaId和实例对象的映射关系
    private Map<String,Object> beanMap = new HashMap<>();

    public DispatcherServlet(){
    }

    /*
    * FruitController去掉@WebServlet注解后不再是一个Servlet，故不会自动调用super.init()
    * FruitController已不是Servlet 不能获取上下文ServletContext
    * */

    public void init(){
        try {
            super.init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder() ;
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for(int i = 0 ; i<beanNodeList.getLength() ; i++){
                Node beanNode = beanNodeList.item(i);
                if(beanNode.getNodeType() == Node.ELEMENT_NODE){ //判断是否是元素节点
                    Element beanElement = (Element)beanNode ; //强制转换为元素节点，因为Element里有好用的getAttribute方法
                    String beanId =  beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class"); //全类名

                    //创建类的实例
                    Class controllerBeanClass = Class.forName(className);
                    Object beanObj = controllerBeanClass.newInstance() ;

                    beanMap.put(beanId , beanObj) ;
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        //假设url是：  http://localhost:8080/ThymeleafFruit/fruit.do
        //那么servletPath是：    /fruit.do
        // 思路是：
        // 第1步：/fruit.do  -> fruit
        // 第2步： fruit -> FruitController
        // (建立配置文件 /src/applicationContext.xml 对两者关系进行说明 映射关系上面已用beanMap保存)
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do") ;
        servletPath = servletPath.substring(0,lastDotIndex);

        //得到Controller对象
        Object controllerBeanObj = beanMap.get(servletPath);

        //将request 中的 operate参数字段 默认设为 index4 即默认访问首页(如第一次访问或刷新访问时operate为空)
        //其他情况下 operate值 从html页面或js中发来的request中读取
        String operate = request.getParameter("operate");
        if(StringUtil.isEmpty(operate)){
            //如果operate为空 默认设为index4
            operate = "index4" ;
        }

        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for(Method method : methods){
                if(operate.equals(method.getName())){
                    //1.统一获取请求参数

                    //1-1.获取当前方法的参数，返回参数数组
                    Parameter[] parameters = method.getParameters();
                    //1-2.parameterValues 用来承载参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        /*
                          原始参数名为
                          java.lang.String.arg0
                          java.lang.String.arg1
                          java.lang.Integer.arg2
                          ...

                          从JDK8开始 加入编译命令行参数 -parameters可以知道实际形参名，如:
                          java.lang.String.oper
                          java.lang.String.keyword
                          java.lang.Integer.pageNo
                          ...
                          再使用parameter.getName()可以获得短的实际形参名: oper,keyword,pageNo...

                          详见README
                         */
                        //需要知道实际参数名才能进行后续的参数类型识别和赋值操作
                        //需要更改编译时附加命令行参数，
                        String parameterName = parameter.getName() ;
                        //如果参数名是request,response,session 那么就不是通过请求中获取参数的方式了
                        if("request".equals(parameterName)){
                            parameterValues[i] = request ;
                        }else if("response".equals(parameterName)){
                            parameterValues[i] = response ;
                        }else if("session".equals(parameterName)){
                            parameterValues[i] = request.getSession() ;
                        }else{
                            //从请求中获取参数值
                            //当参有多个时(如复选框类) 可以用getParameters返回数组
                            String parameterValue = request.getParameter(parameterName);
                            String typeName = parameter.getType().getName();


                            // 常见错误： IllegalArgumentException: argument type mismatch
                            // http://localhost:8080/fruit/fruit.do2?pageNo=2 获取到的pageNo为字符串 "2"
                            // 需要将其转化为Integer 本例只用到Integer 其他类型转换可有可无

                            Object parameterObj = parameterValue ;

                            if(parameterObj!=null) {
                                //参数对象不为null才能进行后面强制类型转换，若不加此判断，后面会报错:
                                // java.lang.NumberFormatException:null
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }

                            parameterValues[i] = parameterObj ;
                        }
                    }
                    //2.controller组件中的方法调用
                    method.setAccessible(true); //使private方法可访问
                    Object returnObj = method.invoke(controllerBeanObj,parameterValues); //注意：传入的是整个参数数组(Java数组支持不同类型元素)
                    //本例中调用FruitController中方法返回的都是字符串类型，后面直接强制类型转换即可

                    //3.视图处理
                    String methodReturnStr = (String)returnObj ;
                    if(methodReturnStr.startsWith("redirect:")){        //比如：  redirect:fruit.do
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        //重定向
                        response.sendRedirect(redirectStr);
                    }else{
                        super.processTemplate(methodReturnStr,request,response);    // 比如：  "edit"
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
