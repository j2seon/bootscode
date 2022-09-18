package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void TestClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample"+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("======================================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }

    }

    @Test
    @Transactional
    public void testSelect2(){
        Long mno = 100L;

        Memo result = memoRepository.getOne(mno);
        System.out.println("======================================");

        System.out.println(result);
    }

    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("Update test").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete(){
        Long mno = 99L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){

        //페이지 처리는 반드시 0부터 이다!!
        Pageable pageable = PageRequest.of(0,10); // Pageable 인터페이스로 페이지 지정해주고
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        System.out.println("--------------------------------------------");

        System.out.println("total Page : "+result.getTotalPages()); // 총페이지
        System.out.println("total Count : "+result.getTotalElements()); //전체페이지 개수
        System.out.println("Page Num : "+result.getNumber()); // 현재페이지번호
        System.out.println("Page Size : "+result.getSize()); // 페이지당 데이터 개수
        System.out.println("has next : "+result.hasNext() ); // 다음페이지 존재여부
        System.out.println("first page : "+result.isFirst()); //시작페이지(0) 여부
        System.out.println("==============================================");

        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(System.out::println);
    }

    @Test
    public void testSort2(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); //orderby 조건이 붙는다.

        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(System.out::println);
    }

    @Test
    public void testQureyMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        for(Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").ascending());
        Page<Memo> list = memoRepository.findByMnoBetween(10L,50L,pageable);
        list.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Commit //커밋해야 결과반영됨 --> 실제로 잘 사용안됨 한번에 삭제가 안되서
    @Transactional //없으면 에어남 select 하고 삭제하는거라
    @Test
    public void testDeleteMemoByMnoLessThan(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }




}