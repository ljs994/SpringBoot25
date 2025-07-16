package org.mbc.board.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {
    // 컨트롤러는 url 생성과 프론트를 연결하는 부분으로 과거에 servlet-context.xml과 같은 역할을 한다.

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("SampleController.hello 메서드 실행");

        model.addAttribute("msg", "안녕! 나 부트!");
    }

    @GetMapping("/ex/ex1") // http://192.168.111.105/ex/ex1 -> /resources/templates/ex/ex1.html
    public void ex1(Model model) {
        // 리스트 타입으로 데이터를 보내 보자
        List<String> list = Arrays.asList("김기원", "이현우", "홍경훈", "박채은", "양지민");

        model.addAttribute("list", list);
    }
    class SampleDTO{
        // 이너 클래스로 클래스 안쪽에 클래스를 선언할 때 활용 된다.
        // 필드
        private String p1, p2, p3;

        // 기본생성자



        public String getP1() {
            return p1;
        }

        public String getP2() {
            return p2;
        }

        public String getP3() {
            return p3;
        }
    }   // 이너 클래스 종료
    
    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
        log.info("SampleController.ex2 메서드 실행");
        // 이너 클래스를 사용해서 객체를 뿌려보자

        List<String> strList = IntStream.range(1,10) // 1~10까지 정수를 생성한다.
                .mapToObj(i -> "데이터" + i).collect(Collectors.toList()); // 리스트에 정수(숫자)문자열이 생성된다.
        // [데이터1, 데이터2, 데이터3,~데이터9]

        Map<String,String> map = new HashMap<>();
        map.put("id","kkw");
        map.put("pw","1234"); // key, value

        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 = "값 : p1";
        sampleDTO.p2 = "값 : p2";
        sampleDTO.p3 = "값 : p3";
        // 최종적으로 객체 3개 완성
        
        model.addAttribute("list", strList);
        model.addAttribute("map",map);
        model.addAttribute("dto",sampleDTO);
        
        // 리턴타입이 void이므로 /resources/templates/ex/ex2.html을 찾음
    } // ex2 메서드 종료
    
}
