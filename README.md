# 📖 < Mangoplate > 서비스 소개
이 프로젝트는 망고플레이트 회원에게는 종류별 맛집 검색 및 후기작성 기능을, 사업자에게는 음식점 등록 및 관리를 할 수 있도록 구현한 프로젝트입니다. **Java**와 **Oracle DB** 개념을 활용해 보고자
간단하게 Eclipse IDE **console**로만 출력되도록 프로젝트를 구현하였고,     
**맛집 추천**이라는 주제는 매년 검색어 순위를 차지하는 키워드이고, 제가 자주 이용하는 검색어 이기 때문에 이와 관련된 프로젝트를
구현해 보고자 **Mangoplate**라는 프로젝트를 주제로 설정하였습니다.

 
  
   <br/>
          
## 💻 개발 환경
- **Java** : JDK 11
- **Editor** : Eclipse IDE
- **Database** : Oracle SQL Developer
- **Output Format** : Console
<br/>
<br>
     
## 🌱 프로젝트 범위 구조도
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/7b5f19e7-540a-45ec-9143-9261df225999)

   <br>
       
## 🧩 ERD
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/d8bfde4f-ca3d-481a-b538-f872ad6c2580)
<br>
<br>     
<br>
## ✨기능 별 출력 결과     
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/11321870-897c-456f-b10a-854a0d95aba0)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/2c7c9906-6a48-4fa5-9c1f-bfcd1805b094)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/47f8bb02-f471-45f9-a386-7672e16b51d7)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/b0d8195d-f87d-4830-b848-653bbd2d774e)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/62dffc9b-626c-4a0c-9840-3578a81760a2)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/f21fca67-581a-46ca-bd95-18e5bdcfaa93)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/0492f7e0-29c4-4213-b972-fee78a152234)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/f373b5bf-0344-4366-8ed8-48877607d203)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/c57b2331-3e37-4a13-acf2-61eb8d765000)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/e9096bde-c2cd-4532-937e-a35b66a024d2)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/6ca2c2a1-0bd9-4e1e-bdd7-fb4a9e05d0b2)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/fd971745-69be-4394-a74f-628a117f49af)
![image](https://github.com/choihjhj/Mangoplate/assets/148078504/7175d6e3-12d7-4751-ac59-3d7e8b675b06)


       
## 👉 개선 사항
- 텍스트 기반의 Console 출력이 아닌 Springboot framework를 이용해 웹사이트로 구현하여 정보를 더 시각화해 사용자에게 더 인터랙티브한 경험을 제공할 것
<!--
콘솔로 출력하는 것과 웹사이트로 프로젝트를 구현하는 것은 매우 다른 접근 방식과 목적을 가지고 있습니다. 여러 측면에서 이 두 방법 간의 차이를 살펴보겠습니다:

사용자 경험과 시각화:

콘솔 출력: 주로 텍스트 기반의 정보를 단순하게 출력하며, 사용자와의 상호작용이 제한적입니다. 시각적인 요소가 부족하며 특별한 디자인이나 사용자 경험이 필요하지 않습니다.
웹사이트: 웹사이트는 사용자에게 더 풍부하고 인터랙티브한 경험을 제공할 수 있습니다. 그래픽, 애니메이션, 사용자 입력 등을 활용하여 정보를 시각적으로 전달하고 상호작용을 통해 사용자와 소통합니다.
접근성과 사용자 도달성:

콘솔 출력: 텍스트 기반이기 때문에 특별한 도구나 지식 없이도 기본적인 정보를 얻을 수 있습니다. 그러나 대화형이나 그래픽 요소가 없어 비전 또는 기타 장애를 가진 사용자에게는 제한적일 수 있습니다.
웹사이트: 웹은 다양한 디바이스와 브라우저를 지원하며, 웹 표준을 준수함으로써 높은 접근성을 제공합니다. 다양한 사용자 그룹에게 적합한 사용자 경험을 제공할 수 있습니다.
기능과 확장성:

콘솔 출력: 주로 텍스트 기반의 간단한 출력에 사용되며, 대규모의 데이터 처리나 다양한 기능을 제공하기에는 한계가 있습니다.
웹사이트: 더 많은 기능을 통합할 수 있으며, 데이터 시각화, 사용자 계정 관리, 외부 API 연동 등 다양한 기능을 제공할 수 있습니다. 또한, 웹은 다양한 플러그인 및 라이브러리를 활용하여 확장성을 높일 수 있습니다.
배포 및 유지 보수:

콘솔 출력: 단일 환경에서 실행되는 경우가 많아 배포가 상대적으로 간단할 수 있지만, 업데이트 및 유지 보수에 제한이 있을 수 있습니다.
웹사이트: 다양한 사용자 환경에서 동작해야 하므로 배포 및 유지 보수에 더 많은 고려가 필요합니다. 그러나 웹 기술의 발전으로 자동화된 배포 및 지속적 통합(CI/CD)도구를 활용하여 효율적으로 관리할 수 있습니다.
콘솔 출력과 웹사이트는 각각의 상황과 목적에 따라 선택되어야 하며, 프로젝트의 성격과 사용자 요구에 맞게 적절한 방법을 선택하는 것이 중요합니다.
-->
  
   
 <!-- 
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/2c7c9906-6a48-4fa5-9c1f-bfcd1805b094">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/47f8bb02-f471-45f9-a386-7672e16b51d7">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/b0d8195d-f87d-4830-b848-653bbd2d774e">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/62dffc9b-626c-4a0c-9840-3578a81760a2">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/f21fca67-581a-46ca-bd95-18e5bdcfaa93">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/0492f7e0-29c4-4213-b972-fee78a152234">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/f373b5bf-0344-4366-8ed8-48877607d203">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/c57b2331-3e37-4a13-acf2-61eb8d765000">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/e9096bde-c2cd-4532-937e-a35b66a024d2">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/6ca2c2a1-0bd9-4e1e-bdd7-fb4a9e05d0b2">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/fd971745-69be-4394-a74f-628a117f49af">
<img width="500" alt="image" src="https://github.com/choihjhj/Mangoplate/assets/148078504/7175d6e3-12d7-4751-ac59-3d7e8b675b06">

-->










