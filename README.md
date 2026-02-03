# Aniverse-common

Aniverse 플랫폼에서 사용되는 **공통 도메인 백엔드 서비스**입니다.  
유저를 중심으로 한 기본 기능(로그인, 프로필, 팔로우, 펫 등)을  
하나의 서버로 분리하여 관리합니다.

---

## 역할

- 플랫폼 공통 도메인 API 제공
- 유저 중심 데이터 관리
- 로그인 및 소셜 로그인 처리
- 여러 서비스에서 공통으로 사용하는 비즈니스 로직 담당

---

## 담당 도메인

- User
- Login (일반 / Kakao / Naver)
- Profile
- Follow
- Pets
- Certification

---

## 기술 스택

- Java / Spring Boot
- MyBatis / PostgreSQL/MongoDB
- Redis
- Gradle / Docker

---
## 패키지 구조
  com/Aniverse/Common
  ├─ controller
  ├─ services
  ├─ mapper
  ├─ dto
  ├─ entity
  └─ exception


## 패키지 구조

