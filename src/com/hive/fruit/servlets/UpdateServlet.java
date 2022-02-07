package com.hive.fruit.servlets;

import com.hive.fruit.dao.FruitDAO;
import com.hive.fruit.dao.impl.FruitDAOImpl;
import com.hive.fruit.pojo.Fruit;
import com.hive.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.设置编码
        request.setCharacterEncoding("utf-8");

        //2.获取参数
        String fidStr = request.getParameter("fid");
        Integer fid = Integer.parseInt(fidStr);
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        //3.执行更新
        fruitDAO.updateFruit(new Fruit(fid,fname, price ,fcount ,remark ));

        //4.资源跳转
        //super.processTemplate("index",request,response);服务器的内部转换  这个index不同于下面sendRedirect的，这个index会被添加/和.html，本句作用相当于下一句：
        //request.getRequestDispatcher("index.html").forward(request,response); 本句从session中展示数据，数据更新完后session里存的是老数据
        //此处需要重定向，目的是重新给IndexServlet发请求，重新获取furitList，然后覆盖到session中，这样index.html页面上显示的session中的数据才是最新的
        //错误写法：response.sendRedirect("index.html"); 应该将index发到url-pattern，跳转到IndexServlet重新获取一次对数据库的请求，而不是跳转到index.html这个静态页面
        response.sendRedirect("index");
    }
}

// java.lang.NumberFormatException: For input string: ""