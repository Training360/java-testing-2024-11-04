# Spring Boot alkalmazás-tesztelés

A Spring Boot kivételesen sok lehetőséget biztosít arra, hogy az alkalmazás különböző komponenseit magukban és más komponensekkel együttműködve is tudjuk tesztelni. A teszt piramist itt is érdemes követni, azonban a sok lehetőség között könnyű elveszni. Ráadásul a Spring Boot több teszt eszközre is beépítetten tartalmaz függőséget, és sok könnyen is illeszthető.

Hogy lehet különböző szeleteket kiszakítani az alkalmazásból, és ezeket izoláltan tesztelni, ezt mutatja be ez az előadás gyakorlati példákon keresztül. Természetesen legjobb gyakorlatokkal, projekttapasztalatokkal fűszerezve.

## Miért jó tesztelni?

* Korábban észrevesszük a hibákat, melyeket azonnal javítani lehet
* Kisebb a valószínűsége új hibák bevezetésének
* Jobb lesz a kód struktúráltsága
    * Kívül helyezkedünk
    * Spagetti kódot nehéz tesztelni
* Dokumentációként szolgál
* Gyorsabban fejleszthetünk
    * Saját példa: varázsló

## Félelem a módosítástól

* Hogyan bizonyosodhatunk meg arról, hogy nem rontunk el semmit?
* Minél kevesebb módosítás, annál kevesebb kockázat
    * Nő a struktúrálatlanság
    * Edit and pray
* Tesztek adják meg a biztonságot
* Refactoring: funkcionalitás nem változik, előkészítjük a terepet

> To me, legacy code is simply code without tests. - Working Effectively with Legacy Code by Michael C. Feathers 

* Könnyebben merünk akár architektúrális változásokat is eszközölni
    * Saját példa: `LocalDateTime`
    * Saját példa: modularizáció, függőségek feloldása

Saját példa: technológia-modernizáció

## Miért nem tesztelünk?

* Miért azok döntenek, akik még nem teszteltek? Nekik milyen összehasonlítási alapuk van?
* "Én már teszteltem, nem volt jó." - "Rosszul csináltad!"
    * Folyamatos tanulás, fejlesztés.

Tesztek mellett áll:

* Martin Fowler, Refactoring könyv írója, egyike az Agile Manifesto aláíróinak
* Kent Beck, az extrém programozás[1] és a tesztvezérelt fejlesztés szoftverfejlesztési módszertanok megalkotója, valamint ő használta először az agilis szoftver fejlesztés kifejezést is, egyike az Agile Manifesto aláíróinak, JUnit megalkotója, Eclipse régebbi vezető tervezője
* Erich Gamma: GoF tagja, Design Patterns
* Robert C. Martin, Uncle Bob, egyike az Agile Manifesto aláíróinak, SOLID elvek terjesztője, Clean Code írója, extrém programozás, tesztvezérelt fejlesztés terjesztője
* Rod Johnson, Juergen Hoeller, a Spring Framework megalkotói
* Összes szakirodalom: Clean Code, Refactoring, Clean Architecture, Working Effectively with Legacy Code, Domain-Driven Design
* Az összes általad használt keretrendszer és library fejlesztője

Te miért nem tesztelsz? Nincs rá időd?

## Tesztek helye a szoftvertermékben

Clean Architecture szerint az architektúra része

## Tesztpiramis vs. testing honeycomb / testing trophy

Spotify: testing honeycomb
Kent C. Dodds: Testing Trophy

## Unit tesztelés a Spring Boottal

* Mockito
* `EmployeesServiceTest`
* EmployeeMapperImpl függőség? `Spy`
* `save()` mellékhatás, `doAnswer`

## E2E és integrációs tesztelés

* `EmployeesApplicationIT`
* Testcontainers, `TestcontainersConfiguration`
* `@RepeatedTest(10)` - Spring Application Context cache-elése

## Slice-ok alkalmazása

* `@SpringBootApplication`, `@EnableAutoConfiguration` elindít mindent
* (Auto-configured Tests)[https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.autoconfigured-tests]
* Limited set of components

Pl.: `@DataJpaTest` -> `@AutoConfigureTestEntityManager` -> `.imports` file -> `TestEntityManagerAutoConfiguration`

* Ha valamit nem akarunk elindítani: rendelkeznek `excludeAutoConfiguration` mezővel
* Kettő egyszerre nem indítható, helyette egy és `@AutoConfigure...`
* `@SpringBootTest` mellé is behúzható `@AutoConfigure...`

## Repository

`EmployeesRepositoryIT`

* `@DataJpaTest`
* `@Import(TestcontainersConfiguration.class)`
* embedded helyett: `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`
* `assertThat` Rest assured
* `showSql`

* `@RepeatedTest(10)` - helyesen lefut a rollback miatt
*  `@Transactional @Rollback(false)`
* `@Sql`

* DBUnit

## REST réteg tesztelése

* WebMVC: `EmployeesControllerWebMvcIT`

## Repository réteg mockolása

`MockedRepositoryIT`

```java
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
```

* `@MockBean` annotáció használata

## JSON réteg tesztelése

* RestTemplate: `EmployeesApplicationRestTemplateIT`
* WebTestClient: `EmployeesApplicationWebTestClientIT`
* HttpInterface: `EmployeesApplicationHttpInterfaceIT`
* RestAssured: `EmployeesApplicationRestAssuredIT`

## Spring Security

https://docs.spring.io/spring-security/reference/servlet/test/method.html

## Awaitility

http://www.awaitility.org/

* `EmployeesApplicationDelayedIT`

## Integráció

`employees-cloud-wiremock` project

`AddressesGatewayRestTemplateIT`

`AddressesGatewayWireMockIT`

