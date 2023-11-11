# Team13_BE

<div align="center">
    <img alt="fundering" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/f97d4de1-4fb5-4de4-ab99-18203287a98f" width="40%">
</div>

<p align="center">
  "크라우드 펀딩에서 모임통장까지"
  <br>
  펀더링은 불특정 다수를 대상으로 한 그룹의 자금 투명성을 보장해주는 모임 통장 플랫폼입니다.
</p>

<br>

### 🧐 펀더링은 어떻게 탄생했나요?

---
저희는 팬 커뮤니티 내에서 빈번하게 발생하는 모금 총책의 '먹튀' 행위를 예방하고 공정한 커뮤니티 활동을 보장하기 위해 모임통장과 크라우드 펀딩의 장점들을 취합하여, 효율적인 자금 관리와 투명한 거래를 위한 서비스를 생각하게 되었습니다. 큰 규모의 모임통장을 어떻게 효과적으로 관리할 수 있을까?
이런 질문에서 시작된 펀더링은 이제 당신의 모임에 필수적인 도구로 자리잡을 준비를 하고 있습니다.

<br>

### 개발 과정 속 기술적 이슈와 문제 해결

---
- 무한 스크롤 구현
  - Offset 기반의 페이지네이션은 DB에 많은 부하가 걸리기 때문에 효율적인 게시물 목록을 조회를 위해 No Offset 방식의 페이지네이션을 적용하여 무한 스크롤이 가능하도록 구현하였습니다. 로직에 사용되는 동적 쿼리는 QueryDSL을 사용하여 구현하였습니다.

<br>

- 이벤트를 이용한 캐시 업데이트
  - 대댓글 수를 조회할 때 캐싱을 하여 DB에 가해지는 부하를 감소시키고자 했습니다. 이때 대댓글 수 조회는 댓글 조회의 일부에 해당하는 기능이기에 따로 캐시를 업데이트할 만한 케이스가 마땅치 않았습니다. 저희는 스프링 프레임워크의 이벤트를 이용하여 이를 해결하고자 했습니다.

<br>


- 스케쥴링
  - 모금 기간이 지난 펀딩 게시물은 목표 모금액 달성 여부에 따라 "진행중"에서 "종료"와 "완료"로 상태가 나뉘게 됩니다. 각 게시물의 모금 종료 시점은 다르기 때문에 스케쥴러를 이용하여 매 정해진 시각마다 각 게시물의 펀딩 현황을 점검한 후 결과에 따라 펀딩 상태를 바꿔주도록 하였습니다.

<br>

- 복합 키를 이용한 팔로우, 찜 기능
  - 팔로우, 찜 기능을 구현하면서 처음에는 셀럽과 사용자의 관계, 펀딩과 사용자의 관계를 객체의 관점에서 연관 관계를 통해 구현하고자 했습니다. 하지만 팔로우, 찜 기능은 객체라기보단 '관계'를 나타내는 상태를 나타내는게 더 적절하다고 판단하게 되었고, 복합 키를 통해 팔로우, 찜 기능을 구현하게 되었습니다.

<br>

- JWT를 이용한 인증, 인가
  - 로그인 과정에서 세션 방식 대신 JWT 토큰 방식을 이용해서 DB에 의존성을 줄이고자 했습니다. 이 과정에서 Access token으로 인한 보안 이슈 고려해서 Refresh Token을 도입했습니다. Access Token의 유효 시간을 줄이고, 별도의 Refresh token으로 Access token을 업데이트해주는 API를 만들어 보안 이슈를 해결하고자 했습니다.
<br>
<br>

### 펀더링 ERD

---

<img alt="ERD" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/aa0f19b4-bf97-4322-b5df-5c2803e76fa5" width="100%">

<br>
<br>

### 펀더링 서버 구조도

---

<img alt="server_design" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/d0e21d9a-df28-46c7-ab3c-e3a5e42dc69d" width="100%">

<br>
<br>

### 펀더링 화면 구성도

---

<img alt="interface" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/95d3a950-0028-47a8-b61a-ae6dc0bbeb40" width="100%">

<br>
<br>

### 사용된 기술 스택

---

<img alt="skillset" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/f8d2f5b4-7fe5-4ebc-b6b2-0b77c92e2c29" width="100%">

<br>
<br>


### 🔗 관련 링크

---
* [UseCase](https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/a580547d-6e72-442b-abf7-068e0b432abc)
* [API docs](https://www.notion.so/joowon-kyung/API-d54c1c7596044024b4d9da51ffdc4512)
* [Wireframe](https://www.figma.com/file/i7F7yPt3mECxI85NwQhKu3/13%EC%A1%B0_%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?node-id=0%3A1&mode=dev)
* [Market Research](https://www.notion.so/joowon-kyung/d9b7e16ab73a46c6bf92402a8f5ff8d7)
* [Team Convention](https://www.notion.so/joowon-kyung/8229e762303744a4ab5be961e2537ae7)


<br>
<br>

## 카카오 테크 캠퍼스 3단계 진행 보드

</br>

## 배포와 관련하여

```

최종 배포는 크램폴린으로 배포해야 합니다.

하지만 배포 환경의 불편함이 있는 경우를 고려하여 

임의의 배포를 위해 타 배포 환경을 자유롭게 이용해도 됩니다. (단, 금액적인 지원은 어렵습니다.)

아래는 추가적인 설정을 통해 (체험판, 혹은 프리 티어 등)무료로 클라우드 배포가 가능한 서비스입니다.

ex ) AWS(아마존), GCP(구글), Azure(마이크로소프트), Cloudtype 

```
## Notice

```
필요 산출물들은 수료 기준에 영향을 주는 것은 아니지만, 
주차 별 산출물을 기반으로 평가가 이루어 집니다.

주차 별 평가 점수는 추 후 최종 평가에 최종 합산 점수로 포함됩니다.
```

![레포지토리 운영-001 (1)](https://github.com/Step3-kakao-tech-campus/practice/assets/138656575/acb0dccd-0441-4200-999a-981865535d5f)
![image](https://github.com/Step3-kakao-tech-campus/practice/assets/138656575/b42cbc06-c5e7-4806-8477-63dfa8e807a0)

[git flowchart_FE.pdf](https://github.com/Step3-kakao-tech-campus/practice/files/12521045/git.flowchart_FE.pdf)


</br>

## 필요 산출물

<details>
<summary>Step3. Week-1</summary>
<div>
    
✅**1주차**
    
```
    - 5 Whys
    - 마켓 리서치
    - 페르소나 & 저니맵
    - 와이어 프레임
    - 칸반보드
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-2</summary>
<div>
    
✅**2주차**
    
```
    - ERD 설계서
    
    - API 명세서
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-3</summary>
<div>
    
✅**3주차**
    
```
    - 최종 기획안
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-4</summary>
<div>
    
✅**4주차**
    
```
    - 4주차 github
    
    - 4주차 노션
```
    
</div>
</details>

---
<details>
<summary>Step3. Week-5</summary>
<div>
    
✅**5주차**
    
```
    - 5주차 github
    
    - 5주차 노션
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-6</summary>
<div>
    
✅**6주차**
    
```
    - 6주차 github
    
    - 중간발표자료
    
    - 피어리뷰시트
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-7</summary>
<div>
    
✅**7주차**
    
```
    - 7주차 github
    
    - 7주차 노션
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-8</summary>
<div>
    
✅**8주차**
    
```
    - 중간고사
    
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-9</summary>
<div>
    
✅**9주차**
    
```
    - 9주차 github
    
    - 9주차 노션
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-10</summary>
<div>
    
✅**10주차**
    
```
    - 10주차 github
    
    - 테스트 시나리오 명세서
    
    - 테스트 결과 보고서
```
    
</div>
</details>

---

<details>
<summary>Step3. Week-11</summary>
<div>
    
✅**11주차**
    
```
    - 최종 기획안
    
    - 배포 인스턴스 링크
```
    
</div>
</details>

---

<br>

## **과제 상세 : 수강생들이 과제를 진행할 때, 유념해야할 것**


```
1. README.md 파일은 동료 개발자에게 프로젝트에 쉽게 랜딩하도록 돕는 중요한 소통 수단입니다.
해당 프로젝트에 대해 아무런 지식이 없는 동료들에게 설명하는 것처럼 쉽고, 간결하게 작성해주세요.

2. 좋은 개발자는 디자이너, 기획자, 마케터 등 여러 포지션에 있는 분들과 소통을 잘합니다.
UI 컴포넌트의 명칭과 이를 구현하는 능력은 필수적인 커뮤니케이션 스킬이자 필요사항이니 어떤 상황에서 해당 컴포넌트를 사용하면 좋을지 고민하며 코드를 작성해보세요.

```

</br>

## **코드리뷰 관련: review branch로 PR시, 아래 내용을 포함하여 코멘트 남겨주세요.**


**1. PR 제목과 내용을 아래와 같이 작성 해주세요.**

> PR 제목 : 전남대_13조_아이템명_0주차
> 

</br>

</div>

---
