# High-Performance URL Shortener

A production-ready URL Shortener built with **Java Spring Boot**. It turns long links into short codes, runs incredibly fast using **RAM Caching**, and tracks every click.

---

## Features
* Shorten Links: Converts `google.com` -> `localhost:8080/s/1`.
* Instant Speed: Uses Caffeine Cache (RAM) to avoid Database slowness.
* Analytics: Tracks how many times a link is clicked.
* Auto-Expiry: Links automatically die after 5 minutes.
* Math Algorithm: Uses Base62 (no random guessing) for unique IDs.

---

## Tech Stack
* Java 17
* Spring Boot 3
* MySQL Database
* Caffeine Cache

---

## Quick Start

### 1. Database Setup
Run this inside MySQL Workbench:

```sql
CREATE DATABASE url_shortener_db;
```

### 2. Configuration

Make sure your `src/main/resources/application.yml` has the correct password:

```yaml
spring:
  datasource:
    username: root
    password: root  # <--- Change this if your MySQL password is not 'root'
```

### 3. Run the App

1. Open Eclipse.
2. Right-click `UrlShortenerApplication.java`.
3. Select **Run As → Java Application**.

---

## How to Use (API)

### 1. Create a Short Link

* Method: POST  
* URL: `http://localhost:8080/api/shorten`  
* Body (JSON):

```json
{
  "originalUrl": "https://www.google.com"
}
```

* Result: You get a short code (e.g., "1" or "A").

---

### 2. Use the Short Link

* Method: GET  
* URL: `http://localhost:8080/s/1`  
* Result: Redirects to the original website.

---

### 3. Check Stats

* Method: GET  
* URL: `http://localhost:8080/api/info/1`  
* Result: Shows:
  - click count  
  - expiry time

---

## Testing the "Time Bomb"

1. Create a new short link.  
2. Confirm it works.  
3. Wait 5 minutes.  
4. Restart the app (clears the cache).  
5. Try the link again.  
6. You should see: "Link has expired".

