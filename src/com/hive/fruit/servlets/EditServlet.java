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

/**
 * @author Hive
 * Description:
 * Date: 2022/2/7 16:21
 */

@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO=new FruitDAOImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fidStr = req.getParameter("fid");
        if(fidStr!=null && !"".equals(fidStr)){
            int fid=Integer.parseInt(fidStr);
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            req.setAttribute("fruit",fruit);
            super.processTemplate("edit",req,resp);
        }
    }
}
