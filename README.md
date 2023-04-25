# Springboot Basic 🌱

## Why SpringBoot? → 경량화
### 의존성 관리 간소화
- Spring starter, lib, structure

### 자동 설정
- Annotation
<br>→ Rest API
<br>→ Datasource

### 배포 간소화
- Jar 
<br>→ MSA 특성 이득
<br>→ JDK만 있으면 실행
<br>→ 번들로 묶여있다보니까 버전 변경으로 인한 앱이 중단될 위험이 감소
<br>→ 무중단 배포

### + Spring Boot CLI

<br>

## 데이터 중심 어플리케이션 설계
### Entity 추가? → SpringBoot - SpringData
- @Entity
- Domain Class
    - 도메인 클래스는 다른 엔티티와 연결되지 않은 때에도 단독으로 존재하고 그 자체로 의미 있음
    - 첫 단계는 데이터를 처리하는 데 사용할 도메인 클래스를 정의
- Domain Entity
  - 유용한 데이터란 여러 요소가 긴밀하게 결합돼 의미 있는 전체를 구성
  - 스프링 데이터는 다양한 복잡성(추상성) 수준에 어떤 형태로든 스프링 부트 애플리케이션이 사용할 수 있는 다양한 메커니즘, 데이터 액세스 옵션을 제공
    - 모든 작업을 수행하고 스프링부트와 스프링 데이터를 사용하면 도메인 작업이 쉬워짐
    - 도메인 클래스를 하나 정의한 후에 데이터 사용 범위, 클라이언트가 사용하는 외부 API, DB 종류를 고려해 데이터베이스와 추상화 수준을 정함
  - 스프링 생태계 가장 많이 쓰이는 옵션 - repository 사용
    - data store - 영속성 저장소 - 내장
    - 아이템은 영속 가능 엔티티(Persistable Entity)이므로 아이템 클래스에 javax.persistence의 @Entity 어노테이션을 추가
    - 또 기존 id 멤버 변수를 DB 테이블의 ID 필드로 표시하기 위해 @Id 어노테이션 붙임

### Data 반복? → Repository
 - JPA : 사용 - DB에 데이터를 생성할 때는 기본 생성자가 필요 (멤버변수 final - mutable)

<br>

## Spring Core Concept
### DI
- 객체를 만들 필요는 없지만, 객체를 만드는 방법을 설명해주면 됨. 이때, 간단한 문법 (e.g @annotion)을 쓰기만 하면 됨 <br>→ 비즈니스 로직에 집중 및 의존성 감소
    - 개발자는 비즈니스 로직 작성하면 실행은 SpringBoot가 함
    - Component 간의 의존성을 해결하기 위해서 IoC(DI) 를 사용
      > Dependency = 한 코드의 변경이 그 method 등을 사용하는 다른 component에 영향을 미침
- 컴포넌트와 서비스는 코드에서 직접 연결할 필요가 없음. configuration 파일을 참고해, Spring에 있는 IoC 컨테이너가 연결


- <의존성을 관리>하기 위해 → DI
  ```java
  class OwnerController {
    private OwnerRepository repository = new OwnerRepository();
  }
  ```
  - OwnerController 가 repository 를 직접 만들어서 의존성을 가지고 있음

  ```java
  class OwnerController {
    private OwnerRepository repo;
    public OwnerController(OwnerRepository repo){
      this.repo = repo;
    }
  }
  ```
  - 어디선가 생성된 repo를 주입받음 (Dependency를 injection 받음)
  - 내가 사용할 것 의존성 type or interface 만 맞으면 됨
  - 의존 주입 대상이 되는 객체를 생성하는 곳만 수정해주면 됨
  - 테스트하기도 편함

  ```java
  class OwnerController {
    private final OwnerRepository owners;
    private VisitRepository visits;

    public OwnerController(OwnerRepository clinicService, VisitRepository visits) {
      this.owners = clinicService;
      this.visits = visits;
    }
  }
  ```
  - Spring의 IoC container가 clinicService,visits 타입에 맞는 (OwnerRepository, VisitRepository) Bean(Spring이 관리하는 객체) 찾아서 Injection시켜줌
  - injection은 annotation 등을 보고 해줌. springboot는 autoconfiguration에 패턴이 들어있음


- 의존하는 객체 없이 MockObject 를 사용해 테스트 가능
  - MockObject 를 사용하면 상태 초기화도 할 수 있기 때문에 원하는 상황에 맞게 설정 자유롭게 할 수 있음

- 스프링에서는 생성자 주입을 공식 권장
  - Spring 4.0부터 생성자 한개만 있을 경우 @Autowired를 생략해도 주입이 가능하도록 편의성을 제공.
    ```java
    @Service
    public class UserService {
      private UserRepository userRepository;
      private MemberService memberService;

      @Autowired
      public UserService(UserRepository userRepository, MemberService memberService) {
        this.userRepository = userRepository;
        this.memberService = memberService;
       }   
    }
    ```

- 순환참조 '감지'
  - 순환참조 A -> B , B -> A 것처럼 사이클처럼 서로를 순환참조하는 문제 ---> 빈 주입 순서 다름
  - 하려는 빈 먼저 찾음. 생성자 주입은 어규먼트 빈을 찾거나 빈 팩토리에서 만듦 -> 그 후 빈 생성자 호출
  - 객체 생성할 때 빈 주입 -> 서로 참조하는 객체가 없음 -> 그 빈 참조할 수 없음 -> 오류 발생


- IoC(Inversion of Control) 컨테이너
  - Spring 컨테이너는 Spring Framework의 핵심Spring 컨테이너는 DI(Dependency Injection)를 사용하여 객체를 생성하고 전체 수명 주기를 구성하고 관리

<br>

## AOP
- 트랜잭션 관리, 인증, 로깅, 보안 등과 같은 cross-cutting concern는 전체 애플리케이션에 영향을 미칠 수 있는 문제이며 보안 및 모듈화 목적을 위해 가능한 한 코드의 한 위치에 집중되어야 함
- AOP는 간단한 플러그형 구성을 사용하여 실제 로직 전후 또는 주변에 이러한 cross-cutting concern를 동적으로 추가할 수 있는 플랫폼을 제공
<br>→ 코드를 쉽게 유지 관리  전체 소스 코드를 다시 컴파일할 필요 없이 간단히 concern을 추가하거나 제거할 수 있음.
- 사용 어떻게? 필터와 연관됨
- Spring AOP 구현 2가지 유형 - XML 구성 파일 사용, AspectJ 주석 스타일 사용
  ```xml
  <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.7.2</version>
  </parent>

  <dependencies>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-aop</artifactId>
      </dependency>
  </dependencies>
  ```

<img src = "https://user-images.githubusercontent.com/29566893/234223899-8cbf5c32-baa9-4211-b14f-0855bc06469a.png" width = "50%">

- Joinpoint : 메서드 실행 또는 예외 처리와 같은 프로그램 실행 중 지점. Spring AOP에서 JoinPoint는 항상 메소드 실행
- Pointcut : 특정 Joinpoint에 aspect가 적용  `execution( [리턴 타입] [패키지 경로] [클래스명].[메소드명]( [매개 변수] ) )`
- 클래스와 패키지, 메소드 시그니처를 이용해 비즈니스 메소드 필터링
- 원하는 특정 메소드에게만 횡단 관심에 해당하는 공통 기능을 수행
- 포인트컷 표현식을 활용해 공통 기능을 적용시킬 메소드들을 정밀하게 뽑아올 수 있음.
- Advice
  - 특정 Joinpoint 에서 aspect가 취하는 조치 <br>다양한 유형의 조언에는 “around,” “before,” and “after.”포함. 인터셉터로 모델링
