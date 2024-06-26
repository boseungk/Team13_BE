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

## 🧐 펀더링은 어떻게 탄생했나요?

저희는 팬 커뮤니티 내에서 빈번하게 발생하는 모금 총책의 '먹튀' 행위를 예방하고 공정한 커뮤니티 활동을 보장하기 위해 모임통장과 크라우드 펀딩의 장점들을 취합하여, 효율적인 자금 관리와 투명한 거래를 위한 서비스를 생각하게 되었습니다. 큰 규모의 모임통장을 어떻게 효과적으로 관리할 수 있을까?
이런 질문에서 시작된 펀더링은 이제 당신의 모임에 필수적인 도구로 자리잡을 준비를 하고 있습니다.

<br>

## 제작 기간 & 참여 인원

- 2023년 9월 4일 ~ 11월 10일
- 팀 프로젝트
  - 참여 인원 7명(프론트 3명, 백엔드 4명)
- 담당한 역할
  - 백엔드 팀 리드 담당 


<br>

## 맡은 역할 및 기능 소개


이 프로젝트에서 제가 맡은 역할을 맡은 파트는 크게 3가지로 나눌 수 있습니다.

1. 무한 스크롤 기능
2. 팔로우 기능
3. JWT 기반 인증, 인가 기능

- **무한 스크롤 구현** 📌 **[코드 확인](https://github.com/boseungk/Team13_BE/blob/develop/src/main/java/com/theocean/fundering/domain/celebrity/service/CelebService.java)**
  - Offset 기반의 페이지네이션은 DB에 많은 부하가 걸리기 때문에 효율적인 게시물 목록을 조회를 위해 No Offset 방식의 페이지네이션을 적용하여 무한 스크롤이 가능하도록 구현하였습니다. 로직에 사용되는 동적 쿼리는 QueryDSL을 사용하여 구현하였습니다.

<br>

- **복합 키를 이용한 팔로우 기능** 📌 **[코드 확인](https://github.com/boseungk/Team13_BE/blob/develop/src/main/java/com/theocean/fundering/domain/celebrity/service/FollowService.java)**
  - 처음에는 셀럽과 사용자의 관계, 펀딩과 사용자의 관계를 객체의 관점에서 연관 관계를 통해 구현하고자 했습니다. 하지만 팔로우, 찜 기능은 객체라기보단 '관계'를 나타내는 상태를 나타내는게 더 적절하다고 판단하게 되었고, 복합 키를 통해 팔로우, 찜 기능을 구현하게 되었습니다.

<br>

- **JWT 토큰을 이용한 인증, 인가** 📌 **[코드 확인](https://github.com/boseungk/Team13_BE/tree/develop/src/main/java/com/theocean/fundering/global/jwt)**
  - 로그인 과정에서 세션 방식 대신 JWT 토큰 방식을 이용해서 DB에 의존성을 줄이고자 했습니다. 
<br>

## 회고 / 느낀점

> 프로젝트 개발 회고 글: [카카오 테크 캠퍼스 프로젝트 회고 글](https://velog.io/@gda05189/%EC%B9%B4%EC%B9%B4%EC%98%A4-%ED%85%8C%ED%81%AC-%EC%BA%A0%ED%8D%BC%EC%8A%A4-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B83%EB%8B%A8%EA%B3%84-%ED%9A%8C%EA%B3%A0)
 
<br>
<br>

## 펀더링 ERD

<img alt="ERD" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/aa0f19b4-bf97-4322-b5df-5c2803e76fa5" width="100%">

<br>
<br>

## 펀더링 서버 구조도

<img alt="server_design" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/d0e21d9a-df28-46c7-ab3c-e3a5e42dc69d" width="100%">

<br>
<br>

## 펀더링 화면 구성도

<img alt="interface" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/95d3a950-0028-47a8-b61a-ae6dc0bbeb40" width="100%">

<br>
<br>

## 사용된 기술 스택

<img alt="skillset" src="https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/f8d2f5b4-7fe5-4ebc-b6b2-0b77c92e2c29" width="100%">

<br>
<br>


## 🔗 관련 링크

* [UseCase](https://github.com/Step3-kakao-tech-campus/Team13_BE/assets/79629515/a580547d-6e72-442b-abf7-068e0b432abc)
* [API docs](https://www.notion.so/joowon-kyung/API-d54c1c7596044024b4d9da51ffdc4512)
* [Wireframe](https://www.figma.com/file/i7F7yPt3mECxI85NwQhKu3/13%EC%A1%B0_%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?node-id=0%3A1&mode=dev)
* [Market Research](https://www.notion.so/joowon-kyung/d9b7e16ab73a46c6bf92402a8f5ff8d7)
* [Team Convention](https://www.notion.so/joowon-kyung/8229e762303744a4ab5be961e2537ae7)


<br>
<br>

## 카카오 테크 캠퍼스 3단계 진행 보드


## Notice
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

------
