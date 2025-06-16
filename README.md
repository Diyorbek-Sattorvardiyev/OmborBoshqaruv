![image](https://github.com/user-attachments/assets/e006bcf5-1f97-4d20-a3ba-a94d3b698ac7)  ![image](https://github.com/user-attachments/assets/8e4e0d3f-1334-4673-b70d-200fbe9ba69d)  ![image](https://github.com/user-attachments/assets/8bf71ca1-b66d-4898-ae1c-883fd7f2f8db)
![image](https://github.com/user-attachments/assets/99ded9db-eb7d-4489-938c-e11fef09543a) ![image](https://github.com/user-attachments/assets/686a9bd6-ce8f-4dbd-8264-859ce5e9e782) ![image](https://github.com/user-attachments/assets/cf5cd5f0-cde9-40cf-a2b4-e7a0e193a6a9)
![image](https://github.com/user-attachments/assets/60c30c55-2d63-4e71-8258-af2936ed3644) ![image](https://github.com/user-attachments/assets/1a0d1436-02f7-4835-b3f5-36f65f975988) ![image](https://github.com/user-attachments/assets/2392c674-69bf-487f-b43d-e359dee4e607)

#  Ombor Boshqaruv Ilovasi (Android)

**Ombor Boshqaruv Tizimi** — bu mahsulotlar, kirim-chiqim, xarajatlar, statistik tahlillar, grafiklar va bildirishnomalarni boshqarish imkonini beruvchi Android ilova. Ilova real vaqtli monitoring, statistik tahlil va qulay interfeys orqali foydalanuvchining ish samaradorligini oshirishga yordam beradi.

---



##  Asosiy funksiyalar

-  Login / Ro‘yxatdan o‘tish
-  Mahsulot qo‘shish (rasm bilan)
-  Mahsulot kirimi va  chiqimini kiritish
-  Ombordagi mavjud zaxirani ko‘rish
-  Xarajatlarni hisoblash
-  Savdo statistikasi va grafiklari
-  Bildirishnomalarni ko‘rish (kam zaxira, muddati tugayotgan mahsulotlar)
-  Mahsulot qidiruvi (qidiruv turi bilan)

---

##  Texnologiyalar

- **Java (Android)** – mobil ilova
- **Retrofit2** – REST API bilan ishlash
- **Multipart upload** – rasm bilan mahsulot kiritish
- **JWT Token** – foydalanuvchi autentifikatsiyasi

---

##  API xizmatlari (ApiService.java)

###  Autentifikatsiya:
```java
@POST("/api/login")
Call<LoginResponse> login(...);

@POST("/api/register")
Call<ApiResponse> register(...);

@GET("/api/profile")
Call<User> getProfile();
```

###  Mahsulotlar:
```java
@Multipart @POST("/api/products")
@GET("/api/products")
@GET("/api/products/{id}")
@DELETE("/api/products/{id}")
```

###  Kirimlar va  Chiqimlar:
```java
@POST("/api/entries")
@GET("/api/entries")
@POST("/api/exits")
```

###  Statistikalar va grafiklar:
```java
@GET("/api/statistics/sales")
@GET("/api/statistics/sales-chart")
@GET("/api/charts/sales")
```

###  Xarajatlar:
```java
@POST("/api/expenses")
@GET("/api/expenses")
```

###  Bildirishnomalar:
```java
@GET("/api/notifications")
```

###  Qidiruv:
```java
@GET("/api/search")
Call<List<Product>> searchProducts(...);
```

---







