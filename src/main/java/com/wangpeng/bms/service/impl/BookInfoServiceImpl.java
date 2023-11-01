package com.wangpeng.bms.service.impl;

import com.wangpeng.bms.mapper.BookInfoMapper;
import com.wangpeng.bms.model.BookInfo;
import com.wangpeng.bms.service.BookInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Override
    public Integer getCount() {
        return bookInfoMapper.selectCount();
    }

    @Override
    public List<BookInfo> queryBookInfos() {
        System.out.println(bookInfoMapper.selectAll());
        return bookInfoMapper.selectAll();
    }

    @Override
    public BookInfo queryBookInfoById(Integer bookid) {
        return bookInfoMapper.selectByPrimaryKey(bookid);
    }

    @Override
    public Integer getSearchCount(Map<String, Object> params) {
        return bookInfoMapper.selectCountBySearch(params);
    }

    @Override
    public List<BookInfo> searchBookInfosByPage(Map<String, Object> params) {
        List<BookInfo> list = bookInfoMapper.selectBySearch(params);
        //System.out.println("searchBookInfosByPage:"+list);
        return bookInfoMapper.selectBySearch(params);
    }

    @Override
    public Integer addBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.insertSelective(bookInfo);
    }

    @Override
    public Integer deleteBookInfo(BookInfo bookInfo) {
        int count = 0;
        try{
            count = bookInfoMapper.deleteByPrimaryKey(bookInfo.getBookId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Integer deleteBookInfos(List<BookInfo> bookInfos) {
        int count = 0;
        for(BookInfo bookInfo : bookInfos) {
            count += deleteBookInfo(bookInfo);
        }
        return count;
    }

    @Override
    public Integer updateBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
    }

    @Override
    public void rateBook(Integer bookId, double score) {
        // 获取原先的书籍信息
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);

        // 更新评论数量
        int originalScoreCount = bookInfo.getBookSoreCount();
        bookInfo.setBookSoreCount(originalScoreCount + 1);

        // 计算新的平均分
        double originalScore = bookInfo.getBookSore();
        double newScore = ((originalScore * originalScoreCount) + score) / (originalScoreCount + 1);
        bookInfo.setBookSore(newScore);

        // 更新数据库中的书籍信息
        bookInfoMapper.rateBook(bookInfo);
    }

}
