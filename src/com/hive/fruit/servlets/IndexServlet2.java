package com.hive.fruit.servlets;

/**
 * @author Hive
 * Description: 增加了分页功能后的Servlet
 * Date: 2022/2/7 20:23
 */
import com.hive.fruit.dao.FruitDAO;
import com.hive.fruit.dao.impl.FruitDAOImpl;
import com.hive.fruit.pojo.Fruit;
import com.hive.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

//Servlet从3.0版本开始支持注解方式的注册
@WebServlet("/index2")
public class IndexServlet2 extends ViewBaseServlet {
    @Override
    public void doGet(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {


        int pageNo = 1 ; //当前在第几页
        String pageNoStr = request.getParameter("pageNo");
        if(pageNoStr!=null && !"".equals(pageNoStr)){
            pageNo = Integer.parseInt(pageNoStr);
        }

        HttpSession session = request.getSession() ;
        session.setAttribute("pageNo",pageNo);


        FruitDAO fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList(pageNo);

        session.setAttribute("fruitList",fruitList);

        //总记录条数
        int fruitCount = fruitDAO.getFruitCount();
        //总页数
        int pageCount = (fruitCount+5-1)/5 ;
        //不能是 int pageCount = fruitCount/5+1 ; 当10项时应该有2页(可以整除时不能加1)
        /*
        总记录条数       总页数
        1               1
        5               1
        6               2
        10              2
        11              3
        fruitCount      (fruitCount+5-1)/5
         */
        session.setAttribute("pageCount",pageCount);
        super.processTemplate("index2",request,response);
    }
}
