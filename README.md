# virtual-factory

Projeyi çalıştırmak için öncelikle ServerFactory.java dosyasını konsolda çalıştırınız. Ardından MachineClient.java ve UserClient.java dosyalarını konsoldan çalıştırabilirsiniz.

Protokol, sunucuya 2 çeşit uygulama bağlandığı için öncelikle kendini tanıtma ile başlıyor. Eğer uygulama makine ise MACH, uygulama kullanıcı ise USER gönderiyor. Ardından 2 uygulama için özel haberleşme komutları ile sunucu ve uygulama iletişim kurabiliyor.

# Makine ile Sunucu iletişimi
## Request:
MACH
## İşlev:
Uygulama sunucuya, kendinin bir makine olduğunu iletiyor. İletişimi başlatmak için gönderilmesi 
zorunludur.
## Response:
Bu gönderilen komut için sunucu herhangi bir response dönmüyor.

---

## Request:
MACH <name> <id> <type> <speed>
## İşlev:
Name, id ,type ve speed alanlarının gönderimi zorunludur. Uygulama bilgileriyle beraber kendini tanıtır.
## Response:
ERROR MISSING_INFORMATION -> Gönderilen alanların herhangi biri boş.
  
ERROR ID_IS_NOT_UNIQUE -> Sunucu da gönderilen id ye sahip makine bulunmaktadır.
  
200 OK -> İşlem başarıyla tamamlandı.

---
  
Uygulama yukarıdaki istekleri sırasıyla yaptıktan sonra sunucudan çalışması için komut beklemesi gerekmektedir. Sunucu eğer makine çalışmıyor ise iş göndermektedir.
  
WORK <Yapılacak iş miktarı>
  
Makine yukarıdaki komutu aldığında çalışmaya başlar ve sunucu bir adet geri dönüş beklemeye başlar. Makine işi yapmaya başlar ve bitirdiğinde DONE komutunu gönderir.
  
DONE <type> <Yapılan iş miktarı>
  
Sunucu aldığı DONE komutu ile makinenin işi bitirdiğini anlar ve makineyi iş alabilir makine durumuna alır.
  
Makine – Sunucu ilişkisi WORK ve DONE döngüsü halinde çalışır.

---
  
## Makine State Diyagramı
  
![image](https://user-images.githubusercontent.com/56939266/155387753-42755bb6-414f-480a-b71e-b659f1999a3f.png)

---
  
# User ile Sunucu iletişimi
## Request:
USER
## İşlev:
Uygulama sunucuya, kendinin bir user uygulaması olduğunu iletiyor. İletişimi başlatmak için gönderilmesi zorunludur.
## Response:
Bu gönderilen komut için sunucu herhangi bir response dönmüyor.

---
  
## Request:
USER <username>
## İşlev:
Sunucuya giriş yapmak için gönderilmesi gerekmektedir.
## Response:
ERROR NEED_USERNAME -> USER komutundan sonra herhangi bir username gönderilmemiş.
  
ERROR USER_NOT_FOUND -> Giriş yapılmak istenen user bulunamadı. 
  
200 OK -> İşlem başarıyla tamamlandı.

---
 
## Request:
PASSWORD <password>
## İşlev:
Sunucuya giriş yapmak için password gönderir.
## Response:
ERROR NEED_PASSWORD -> PASSWORD komutundan sonra herhangi bir password gönderilmemiş.
  
ERROR THIS_USER_STILL_USING -> User başka bir yerden giriş yapmış bulunmaktadır.
  
ERROR WRONG_PASSWORD -> Password bilgisi uyuşmamaktadır.
  
200 OK -> İşlem başarıyla tamamlandı.

---
  
## Request:
LOOK <type>
## İşlev:
Sunucu, gönderilen type deki makinelerin bilgisini gönderir.
## Response:
ERROR THERE_IS_NO_MACHINE -> Belirtilen type bilgisiyle eşleşen makine yok.
  
START
  
<machine id> <machine.name> <machine.condition>
  
…
  
END -> İlk olarak START gönderilir. Ardından makinelerin listesini gönderir ve END göndererek sonlanır.

---
  
## Request:
FINDMACH <id>
## İşlev:
Belirli bir id deki makine bilgisini ve yaptığı işleri sunucudan alır.
## Response:
ERROR NO_MACHINE_WITH_ID -> Gönderilen id kullanan makine bulunmamaktadır.
  
START
  
<machine id> <machine.name> <machine.condition>
  
<type> <iş miktarı>
  
…
  
END -> İlk olarak START gönderilir. Ardından makinenin bilgisini gönderir ve bitirdiği işleri gönderir. END göndererek sonlanır.

---
  
## Request:
JOBS
## İşlev:
Sunucuda bekleyen işleri alır.
## Response:
ERROR HAVE_NO_JOB -> Sunucuda bekleyen bir iş yok.
  
START
  
<type> <iş miktarı > <id >
  
…
  
END -> İlk olarak START gönderilir. Ardından bekleyen işler gönderilir ve END göndererek sonlanır.

---
  
## Request:
NEED <type> <miktar>
## İşlev:
Sunucuya yapılması gereken işi bildirir.
## Response:
Bu gönderilen komut için sunucu herhangi bir response dönmüyor.

---
  
## Request:
QUIT
## İşlev:
Sunucudan çıkış yapmak için gönderilir.
## Response:
Bu gönderilen komut için sunucu herhangi bir response dönmüyor.

---
  
## User State Diyagramı

![image](https://user-images.githubusercontent.com/56939266/155387651-9bcf7e5f-24fd-4210-b1c3-c8b9bceaead5.png)
