# OutSourcing Project  
요구사항에 따라 백엔드 API 를 설계, 구현하고  
프론트엔드 페이지와 연동하는 프로젝트입니다

### API 명세서
https://www.notion.so/API-2178976d5bc9807993adc14d1c560ab0?source=copy_link

### ERD
![아웃소싱 ERD.png](image/%EC%95%84%EC%9B%83%EC%86%8C%EC%8B%B1%20ERD.png)

- 사용 기술
    - 언어: Java 17
    - 프레임워크: Spring Boot 3.4.5
    - 데이터베이스: MySQL 9.2.0
    - 라이브러리
        - Spring Data JPA: ORM(Object Relational Mapping)
        - Bean Validation: 입력값 검증
        - Bcrypt: 비밀번호 암호화
        - JJWT: JWT(Json Web Token) 사용
        - Spring Security: 인증, 인가 관리


- 테스트 실행 환경
  - 프론트엔드 연동: Docker 28.2.2 / Docker Compose v2.35.1

### Test 커버리지
![Test Coverage.png](image/Test%20Coverage.png)