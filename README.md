# Thymeleaf_Fruit

Fruit Store manage system, using Thymeleaf
Thymeleaf实现的水果库存管理系统
其中版本2的优化思想涉及到了Spring MVC简单框架，会比较难，但理解后非常有利于学习框架

使用到 Thymeleaf, Servlet, HTML, CSS, JavaScript, MySQL...
```hive
功能：文件名对应功能能实现版本
*1 :初始版本
*2 :增加分页功能的版本
*3 :增加关键字搜索的版本(功能最完善)
```
版本1访问地址：
```
http://localhost:8080/fruit/index
http://localhost:8080/fruit/index2
http://localhost:8080/fruit/index3
```

第二个大版本更新：MVC-Servlet优化，设计Spring MVC简单框架
多一个中央控制器的好处：FruitController，UserController...中不需要重复出现反射的代码
直接将每个Controller中都一样的反射的代码抽取写入中央控制器中
```
新增文件：
DispatcherServlet.java
FruitController.java
StringUtil.java
add4.html
edit4.html
add4.html
index4.html
index4.js
ProgramStructure-MVC.jpg
applicationContext.xml

注意：改进MVC后要获取方法的形参实际名称，从JDK8开始可以获取到
要改变 设置-构建执行部署-编译器-Java编译器-附加命令行参数： -parameters
表示Java虚拟机在编译的时候得到的class文件里附带形参的实际名称

```
版本2访问地址：
```
http://localhost:8080/fruit/fruit.do2
```

```
Review:
1. 最初的做法是： 一个请求对应一个Servlet，这样存在的问题是servlet太多了
2. 把一些列的请求都对应一个Servlet, IndexServlet/AddServlet/EditServlet/DelServlet/UpdateServlet -> 合并成FruitServlet
   通过一个operate的值来决定调用FruitServlet中的哪一个方法
   使用的是switch-case
3. 在上一个版本中，Servlet中充斥着大量的switch-case，试想一下，随着我们的项目的业务规模扩大，那么会有很多的Servlet，也就意味着会有很多的switch-case，这是一种代码冗余
   因此，我们在servlet中使用了反射技术，我们规定operate的值和方法名一致，那么接收到operate的值是什么就表明我们需要调用对应的方法进行响应，如果找不到对应的方法，则抛异常
4. 在上一个版本中我们使用了反射技术，但是其实还是存在一定的问题：每一个servlet中都有类似的反射技术的代码。因此继续抽取，设计了中央控制器类：DispatcherServlet
   DispatcherServlet这个类的工作分为两大部分：
   1.根据url定位到能够处理这个请求的controller组件：
    1)从url中提取servletPath : /fruit.do -> fruit
    2)根据fruit找到对应的组件:FruitController ， 这个对应的依据我们存储在applicationContext.xml中
   <bean id="fruit" class="com.atguigu.fruit.controllers.FruitController/>
   通过DOM技术我们去解析XML文件，在中央控制器中形成一个beanMap容器，用来存放所有的Controller组件
   3)根据获取到的operate的值定位到我们FruitController中需要调用的方法
   2.调用Controller组件中的方法：
    1) 获取参数
       获取即将要调用的方法的参数签名信息: Parameter[] parameters = method.getParameters();
       通过parameter.getName()获取参数的名称；
       准备了Object[] parameterValues 这个数组用来存放对应参数的参数值
       另外，我们需要考虑参数的类型问题，需要做类型转化的工作。通过parameter.getType()获取参数的类型
    2) 执行方法
       Object returnObj = method.invoke(controllerBean , parameterValues);
    3) 视图处理
       String returnStr = (String)returnObj;
       if(returnStr.startWith("redirect:")){
       ....
       }else if.....
```