## 🍽️ Lucky7s Table 플러스 팀 프로젝트

### 🙋‍♀️ 플러스 팀 프로젝트 설명
- **플러스 팀 프로젝트**는 <u>Spring을 활용하여 위치 기반 맛집 검색 웹페이지를 구현</u>한 팀 프로젝트입니다.
- **팀원 : 이민정, 명민준, 이범서, 이호수, 조예인**
- 기간 : 2025.03.24 - 2025.03.31

<br>

### 📌 프로젝트 개요
**Lucky7s Table**은 사용자의 위치를 기반으로 맛집을 검색할 수 있는 웹 서비스입니다.
공통 관심사인 ‘맛집’을 주제로 삼아, **다이닝코드** 사이트를 참고하여 다음과 같은 기능을 구현하였습니다:
- 거리, 음식 종류, 키워드 기반 맛집 검색
- 조회수가 많은 순으로 정렬된 검색 결과 제공
- 위치 기반 정보 제공을 통한 사용자 편의성 향상
- 도전적인 기술 실험 및 성능 최적화
<br>

### 역할 분담
| 이름     | 역할 |
|--------------|-------------|
| [이민정](https://github.com/minjonyyy)   | Store CRUD,일반 검색 → Redis 적용한 성능 향상 검색 / Jmeter 사용한 성능 테스트GeoHash와 Redis 적용한 위치 기반 조회 |
| [명민준](https://github.com/mmj-159)   | 일반 검색, 인기 검색어 기능, 검색 인덱싱 및 최적화 |
| [이범서](https://github.com/polaris65b)   | Kakao Map API를 이용한 주소 → 좌표 변환, 위치 기반 조회 |
| [이호수](https://github.com/Hokirby)   | JWT + Spring Security 인증/인가, 리뷰 CRUD, 자동완성, 인기 검색어 기능 |
| [조예인](https://github.com/codingTrip-IT)   | User CRUD, MySQL 기반 위치 검색 기능 |

<br>

## 주요 기능
### 사용자 인증 및 권한 관리
- JWT + Spring Security 기반 인증/인가
### 위치 기반 맛집 검색
- Kakao Map API로 주소 → 좌표 변환
- 카테고리, 키워드, 거리 기준 맛집 검색
- 조회수가 많은 순으로 정렬
### 인기 검색어
- Redis를 활용해 조회수 기반 인기 검색어 목록 제공
### 검색어 자동완성
- 검색어 접두사를 기준으로 자동완성 기능 제공
- 사전순 정렬로 사용자 편의성 향상
### 리뷰 기능
- 리뷰 등록, 조회, 삭제 가능

<br>

📑 진행 및 회의 기록 : [Lucky7](https://www.notion.so/teamsparta/7-1c02dc3ef5148049a69afaa56f467438) 노션에서 진행


<br>

<div align=center> 

## 📚 STACKS

<br>
<div align=left> 
  
- **Language & Framework**: Java, Spring Boot
- **Database**: MySQL
- **Cache**: Redis
- **Security**: Spring Security, JWT
- **API**: Kakao Map API
- **Version Control**: Git, GitHub

<br>
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

<br><br><br>

<div align=left> 
  
## 👩🏻‍ API 명세
  
### 회원 API
<img width="1145" alt="회원 API" src="https://github.com/user-attachments/assets/f421c7dc-cf15-40dc-bffb-03dae7ea40aa" />

### 가게 API
<img width="1244" alt="가게 API" src="https://github.com/user-attachments/assets/5f6f8724-763d-4def-affe-878ba248ca0e" />

### 검색 API
<img width="1250" alt="검색 API" src="https://github.com/user-attachments/assets/9d8a2486-1723-4577-b9f6-4bb28a5ccc82" />

### 리뷰 API
<img width="1245" alt="리뷰 API" src="https://github.com/user-attachments/assets/80dcac75-47d3-4b42-8405-20b74687f721" />


<br><br><br>

## 👩 ERD
<img width="817" alt="ERD" src="https://github.com/user-attachments/assets/4f9d614c-5cef-432f-99b5-187ba7c03014" />



<br><br><br>

<div align=left> 

## 성능 테스트 결과
### 일반 검색 vs Redis 캐싱 검색 성능 비교
| 항목             | 일반 검색 | Redis 검색 |
|------------------|-----------|-------------|
| 평균 응답 시간   | 35ms      | 5ms          |
| 성능 향상률      | 약 87% 개선 |
- 테스트 환경: 100 Users / Ramp-up 60
- Redis 검색은 캐시가 누적될수록 더 큰 성능 향상 기대 가능
---
## 검색 성능 최적화
### 대상 쿼리
- 핵심 기능인 **검색 쿼리**를 성능 개선 대상으로 선정
### 최적화 방법
- `FULLTEXT INDEX`와 `Ngram`을 활용한 유사도 기반 검색
- 검색어와의 유사도(score)를 기준으로 정렬
- 정렬 기준 적용 전후 비교:
  | 항목                 | 정렬 미적용 | 정렬 적용 |
  |----------------------|-------------|-----------|
  | 응답 시간            | 288ms       | 122ms     |
  | 성능 향상률          | 약 57% 개선 |
```sql
-- Ngram 기반 인덱스 예시 DDL
ALTER TABLE store
ADD FULLTEXT(name, category)
WITH PARSER ngram;
```

<br>

✅ 왜 검색 API에 Cache를 적용하는가?
- 검색 요청이 많을 경우, DB 부하를 줄이기 위해
- 같은 검색어에 대한 반복적인 조회를 빠르게 응답하기 위해
- API 응답 속도를 개선하여 사용자 경험을 향상시키기 위해
  
  <br>

  
✅ 왜 Redis를 선택하는가?
- Redis는 영속성 지원, 다양한 자료구조(Hash, List, Sorted Set 등) 제공
- Redis는 데이터 만료 설정, TTL 관리가 용이하여 검색어 캐싱에 적합

    <br>


✅ Redis에서 사용할 자료구조
- Sorted Set (ZSET)
  - 검색어를 키로 하고, 검색 횟수를 점수(Score)로 설정하여 인기 검색어 순위 관리 
  - ZINCRBY 명령어를 사용하여 검색어 검색 시마다 점수 증가
  - ZREVRANGE로 인기 검색어 순위 조회




