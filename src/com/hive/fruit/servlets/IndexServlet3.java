package com.hive.fruit.servlets;

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
/**
 * @author Hive
 * Description: 增加关键字查询功能
 * Date: 2022/2/7 20:54
 */


@WebServlet("/index3")
public class IndexServlet3 extends ViewBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    public void doGet(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {

        //遇到post 最好设置编码
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession() ;
        Integer pageNo = 1 ;

        String oper = request.getParameter("oper");
        //如果oper!=null 说明 通过表单的查询按钮点击过来的
        //如果oper是空的，说明 不是通过表单的查询按钮点击过来的

        String keyword = null ;
        if(oper!=null && !"".equals(oper) && "search".equals(oper)){
            //说明是点击表单查询发送过来的请求
            //此时，pageNo应该还原为1 ， keyword应该从 请求参数 中获取
            pageNo = 1 ;
            keyword = request.getParameter("keyword");
            if(keyword==null || "".equals(keyword)){
                //如果keyword空的，keyword重新设为空字符串(收到index3发送的搜索请求 但是搜索内容为空)
                keyword = "" ;
            }
            session.setAttribute("keyword",keyword);
        }else{
            //说明此处不是点击表单查询发送过来的请求（比如点击下面的上一页下一页或者直接在地址栏输入网址）
            //此时keyword应该从 session作用域 获取
            String pageNoStr = request.getParameter("pageNo");
            if(pageNoStr!=null && !"".equals(pageNoStr)){
                pageNo = Integer.parseInt(pageNoStr);
            }
            Object keywordObj = session.getAttribute("keyword");
            if(keywordObj!=null){
                keyword = (String)keywordObj ;
            }else{
                keyword = "" ;
            }
        }

        session.setAttribute("pageNo",pageNo);

        FruitDAO fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword , pageNo);

        session.setAttribute("fruitList",fruitList);

        //总记录条数
        int fruitCount = fruitDAO.getFruitCount(keyword);
        //总页数
        int pageCount = (fruitCount+5-1)/5 ;

        session.setAttribute("pageCount",pageCount);

        super.processTemplate("index3",request,response);
    }
}
