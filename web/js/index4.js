function delFruit(fid){
    if(confirm('是否确认删除？')){
        window.location.href='fruit.do2?fid='+fid+'&operate=del';
    }
}

//加入搜索功能后的跳转
function page(pageNo){
    window.location.href="fruit.do2?pageNo="+pageNo;
}