package com.hive.fruit.controllers;

import com.hive.fruit.dao.FruitDAO;
import com.hive.fruit.dao.impl.FruitDAOImpl;
import com.hive.fruit.pojo.Fruit;
import com.hive.myssm.myspringmvc.ViewBaseServlet;
import com.hive.myssm.util.StringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FruitController extends ViewBaseServlet {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    //各方法return值 是为了继续发送给中央控制器进行第三步的视图处理，(即重定向或转发到return值代表的地址)

    private String update(Integer fid , String fname , Integer price , Integer fcount , String remark ){
        //3.执行更新
        fruitDAO.updateFruit(new Fruit(fid,fname, price ,fcount ,remark ));
        //4.资源跳转
        return "redirect:fruit.do2";
    }

    private String edit4(Integer fid , HttpServletRequest request){
        if(fid!=null){
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            request.setAttribute("fruit",fruit);
            //super.processTemplate("edit4",request,response);
            return "edit4";
        }
        return "error" ;
    }

    private String del(Integer fid){
        if(fid!=null){
            fruitDAO.delFruit(fid);
            return "redirect:fruit.do2";
        }
        return "error";
    }

    private String add4(String fname , Integer price , Integer fcount , String remark ) {
        Fruit fruit = new Fruit(0,fname , price , fcount , remark ) ;
        fruitDAO.addFruit(fruit);
        return "redirect:fruit.do2";
    }

    private String index4(String oper , String keyword , Integer pageNo , HttpServletRequest request ) {
        HttpSession session = request.getSession() ;

        //首此访问或刷新访问时 keyword pageNo都为空，oper为默认值index4
        if(pageNo==null){
            pageNo = 1;
        }
        if(StringUtil.isNotEmpty(oper) && "search".equals(oper)){
            pageNo = 1 ;
            if(StringUtil.isEmpty(keyword)){
                keyword = "" ;
            }
            session.setAttribute("keyword",keyword);
        }else{
            Object keywordObj = session.getAttribute("keyword");
            if(keywordObj!=null){
                keyword = (String)keywordObj ;
            }else{
                keyword = "" ;
            }
        }

        // 重新更新当前页的值存入Session中，供接下来return后 中央控制器 读取并进行指定页数的视图渲染处理
        session.setAttribute("pageNo",pageNo);

        FruitDAO fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword , pageNo);
        session.setAttribute("fruitList",fruitList);

        //总记录条数
        int fruitCount = fruitDAO.getFruitCount(keyword);
        //总页数
        int pageCount = (fruitCount+5-1)/5 ;
        session.setAttribute("pageCount",pageCount);

        return "index4" ;
    }
}
