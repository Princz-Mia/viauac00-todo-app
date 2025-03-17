# Házi feladat specifikáció

Információk [itt](https://viauac00.github.io/laborok/hf)

## Mobil- és webes szoftverek

### 2023.10.21.

### TODO application

### Princz Mia - (XF7A69)

### princz.mia@gmail.com

### Laborvezető: Erőss Helga Enikő

## Bemutatás

Az alkalmazás egy egyszerű és sokoldalú TODO applikcáció. Mint minden egyetemista számára, számomra is fontos, hogy hasznosan töltsem az időmet és úgy vélem ez az alkalmazás segíteni fog, hogy növelni tudjak a produktivitásomon. A célközönség minden olyan Android-felhasználó, aki szeretné hatékonyan szervezni mindennapi feladatait és projektjeit, és könnyen kezelhető alkalmazást keres a teendők nyomon követésére és menedzselésére.

## Főbb funkciók

Az alkalmazásban a felhasználó létrehozhat listákat, amelyeket elnevezhet. Ezeket a listákat törölheti vagy a nevüket szerkesztheti. Egy adott listán belül pedig létrehozhat teendőket. Egy teendőnek adhat nevet, rövid leírást, esetleges határidőt az elvégzésére és beállíthat az adott teendőre értesítést, hogy mondjuk valamilyen időpontban értesítse az alkalmazás a felhasználót, hogy ne feledkezzen meg a teendőről. Ezeket a teendőket törölheti a listából, vagy szerkesztheti a fent említett adatait. A listázott teendőket közül be lehet jelölni az elvégzetteket. Az elvégzett teendőket is lehet külön törölni. Egy listán belül a teendőket lehetséges ABC sorrendbe listázni a nevük alapján. Az alkalmazásból kilépéskor a létrehozott listákat és teendőket nem felejti el.

- [ ] Listák kezelése
  - [ ] Új listák létrehozása névvel.
  - [ ] Listák nevének szerkesztése.
  - [ ] Listák törlése.
- [ ] Teendők kezelése listán belül
  - [ ] Új teendők létrehozása listán belül.
  - [ ] Teendők szerkesztése
    - [ ] Cím hozzáadása vagy szerkesztése.
    - [ ] Rövid leírás hozzáadása vagy szerkesztése.
    - [ ] Határidő hozzáadása vagy szerkesztése.
    - [ ] Értesítés beállítása az elvégzésére.
  - [ ] Teendők elvégzetté jelölése.
  - [ ] Teendők törlése listán belül.
  - [ ] Elvégzett teendők külön törlése.
- [ ] Teendők listázása listán belül
  - [ ] ABC sorrendbe rendezés a teendők címe alapján.
- [ ] Adatmegőrzés
  - [ ] Kilépéskor az alkalmazás megőrzi létrehozott listákat és teendőket.

Ezen funkciók kombinációjával a felhasználók hatékonyan szervezhetik mindennapi feladataikat és projekteiket az alkalmazás segítségével.

## Választott technológiák:

- UI
- Fragmentek
- RecyclerView
- Perzisztens adattárolás
