function delFruit(fid){
    if(confirm('是否确认删除？')){
        window.location.href='del.do?fid='+fid;
    }
}

/*跳转到指定页*/
function page(pageNo){
    window.location.href="index2?pageNo="+pageNo;
}

//加入搜索功能后跳转到index3
function page2(pageNo){
    window.location.href="index3?pageNo="+pageNo;
}