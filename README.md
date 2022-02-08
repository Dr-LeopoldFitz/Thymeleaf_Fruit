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
