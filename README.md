# Drone Remote Control with Local Wi-Fi & MAVLink Integration

---

## Demo Video

[![Watch the video](https://img.youtube.com/vi/EXm_AMKFGcg/0.jpg)](https://www.youtube.com/watch?v=EXm_AMKFGcg&t=357s)

---

## 

In our handmade drone, we created a local Wi-Fi network using an **ESP8266 module**. By connecting both our **computer and smartphone** to this network, we enabled data transmission using a **Flask API**.

Through sliders in our mobile application, we send commands to the drone. These commands are first received by the computer via the API and then interpreted by a **Python script**. The interpreted commands are converted into a format understandable by the **Pixhawk flight controller** using the **MAVLink protocol**, and finally transmitted to the drone's motors.

---

## 

El yapımı dronemizde bulunan **ESP8266** sayesinde lokal bir Wi-Fi ağı oluşturduk. Oluşan bu ağa hem **bilgisayarımızı** hem de **telefonumuzu** bağladığımızda, verileri **Flask API** aracılığıyla aktarabiliyoruz.

Uygulamamızda bulunan **slider’lar** ile drone’a vermek istediğimiz komutları belirliyoruz. Bu komutlar önce **API üzerinden bilgisayarımıza**, ardından **Python kodumuza** aktarılıyor. Kod, bu komutları algılayarak **MAVLink protokolü** sayesinde **uçuş kontrol kartımız Pixhawk**’ın anlayabileceği formata dönüştürüyor ve motorlara iletiyor.

---

