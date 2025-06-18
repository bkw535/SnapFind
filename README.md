# 📸 SnapFind - 상품 인식 기반 검색 애플리케이션

SnapFind는 사용자가 카메라로 촬영한 상품 이미지를 기반으로 관련 정보를 자동으로 검색해주는 **상품 인식 기반 쇼핑 도우미**입니다. 상품명을 몰라도 이미지 하나로 간편하게 쇼핑 정보를 찾아보세요.

---

## 🧩 프로젝트 개요

### 🔍 프로젝트 정의

**SnapFind**는 이미지 인식 기술과 웹 크롤링을 결합하여, 상품명을 모르는 사용자도 상품 정보를 쉽게 확인하고 쇼핑에 활용할 수 있도록 돕는 **모바일 기반 애플리케이션**입니다.

### 🧠 프로젝트 배경

온라인 쇼핑의 급증과 더불어, 사용자들은 실물 상품을 본 후 온라인에서 정보를 검색하거나 구매하는 일이 많아졌습니다. 하지만 상품명을 정확히 모르거나 키워드 입력이 번거로운 상황에서는 검색이 어렵습니다. 특히 모바일 환경에서는 직관적이고 빠른 검색 방식이 필요합니다.

---

## 🎯 프로젝트 목표

1. **Google 기반 OAuth 로그인 기능 구현**
2. **카메라로 촬영한 상품 이미지 분석 (Vision API 사용)**
3. **연관된 상품 정보를 웹에서 자동 크롤링**
4. **사용자별 검색 기록 저장 및 마이페이지 제공 (최대 10건)**

---

## 🚀 주요 기능

- 📷 **카메라 촬영 및 Vision API 연동**  
- 🔐 **SNS 기반 OAuth 로그인**  
- 🔍 **상품 기반 관련 제품군 자동 웹 크롤링**  
- 🗂 **사용자 검색 기록 저장 및 마이페이지 확인 기능**

---

## 시나리오

### 🔑 로그인  
<img src="https://github.com/user-attachments/assets/fc2c1ef9-27ec-4123-98e1-9e2ca37cb047" width="300"/>
<img src="https://github.com/user-attachments/assets/2fdf3b85-eafb-429a-bde0-f369cfb48c7d" width="300"/>

### 📷 카메라  
<img src="https://github.com/user-attachments/assets/9e178041-516f-4c3a-bf58-019817073ef0" width="300"/>

### 📋 결과  
<img src="https://github.com/user-attachments/assets/3f430b43-5e15-4075-8549-6071a926a1a0" width="300"/>

### 🌐 크롤링  
<img src="https://github.com/user-attachments/assets/b1cb83e7-18c9-4c7e-9d23-dea7f857537d" width="300"/>

### 👤 마이페이지  
<img src="https://github.com/user-attachments/assets/71e83e33-6fd5-4f41-acff-8b2e2cc6a80e" width="300"/>

---

## ⚙️ 기술 스택

| 영역         | 기술                    |
|--------------|-------------------------|
| 프론트엔드   | SwiftUI (iOS 전용)      |
| 백엔드       | Spring Boot             |
| 이미지 분석  | Google Cloud Vision API |
| 인증         | OAuth 2.0 (SNS 로그인) |
| 웹 크롤링    | Selenium |
| 데이터 저장  | MySQL |

---

## 🎉 기대효과

- 🔎 **편리한 상품 검색**: 키워드 입력 없이 사진만으로 정보 검색
- 🛒 **쇼핑 효율성 향상**: 상품 연관 정보 자동 제공 → 사용자의 검색 부담 감소
- 👤 **맞춤형 사용자 경험**: 개인별 검색 이력 제공으로 사용자 편의성 향상

---

## 🎥 데모 영상

📺 **YouTube 링크**  
[👉 영상 보러가기](https://youtu.be/gqpS2xNayUo)

---
