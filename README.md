# What you can delete from your Java code in 2026

Companion repo for the article *"What You Can Delete from Your Java Code in 2026"*.

Every topic has a `before` package (the code we all still have) and an `after` package (the same thing on Java 25 LTS).
The `after` packages use the JDK alone: no Lombok, Guava, Commons Lang, Commons IO or Apache HttpClient.
Each test runs **both** versions and asserts that they behave the same, so the deletion is safe.
Where the `before` code is actually broken, the test proves that too.

## The headliners

- **Virtual threads:** 1,000 blocking calls take ~2 s on a 50-thread pool and ~100 ms on virtual threads.
- **`SimpleDateFormat`:** a shared formatter got **a third to over four fifths of 10,000** concurrent calls wrong in our runs, almost always silently. `java.time`: zero. The same `Date` code also counts 28 → 30 March 2026 as **one** day, because of daylight saving time.
- **Old JVM flags:** `-XX:+UseConcMarkSweepGC`, `-XX:MaxPermSize`, `-XX:+UseBiasedLocking` and friends stop the JVM from starting. `-XX:+ZGenerational` only warns on JDK 25, but on JDK 27 the JVM refuses to start. `JvmFlagsTest` checks all of them.
- **For free:** an AOT cache takes startup from ~225 ms to ~140 ms (−38%), and compact object headers take 10 M small objects from 272 MB to 196 MB of heap (−28%). Neither needs a code change.
- **Structured concurrency (JDK 27 preview):** with `CompletableFuture` a failing call still waits for the slow one. With `StructuredTaskScope` it fails fast and interrupts the rest.

## Java 25 LTS: delete it today

Ordered as in the article, from search-and-replace to design and concurrency.

| # | Delete | Replace with | Since | Code |
|---|--------|--------------|-------|------|
| | **Level 1: search and replace** | | | |
| 1 | `ignored`, `unused`, `e`, `k` | Unnamed variables `_` | 22 | [`unnamed`](src/main/java/dev/deletejava/unnamed) |
| 2 | `collect(Collectors.toList())` | `.toList()` (unmodifiable!) | 16 | [`streams`](src/main/java/dev/deletejava/streams) |
| 3 | `"..." + "\n" +` SQL and JSON | Text blocks + `formatted()` | 15 | [`textblocks`](src/main/java/dev/deletejava/textblocks) |
| 4 | Guava / Commons Lang one-liners | `String`, `Objects`, `List`, `HexFormat`, `Math.clamp`, sequenced collections | 9–21 | [`utilities`](src/main/java/dev/deletejava/utilities) |
| 5 | Commons IO `FileUtils` / `IOUtils` | `Files`, `InputStream.transferTo` | 9–12 | [`fileio`](src/main/java/dev/deletejava/fileio) |
| 6 | Switch statements with `break` | Switch expressions | 14 | [`switches`](src/main/java/dev/deletejava/switches) |
| 7 | `instanceof` followed by a cast | Type patterns in `switch`, `case null` | 16 / 21 | [`instanceofcast`](src/main/java/dev/deletejava/instanceofcast) |
| 8 | Removed and deprecated JVM flags | Nothing | — | [`JvmFlagsTest`](src/test/java/dev/deletejava/JvmFlagsTest.java) |
| 9 | `public class` + `static void main` + imports in scripts | Compact source files, `import module` | 25 | [`scripts`](scripts) |
| | **Level 2: change a type or an API** | | | |
| 10 | Lombok `@Value` value objects | `record` + compact constructor | 16 | [`records`](src/main/java/dev/deletejava/records) |
| 11 | `return null`, `isPresent()` + `get()` | `Optional` used properly | 8–16 | [`optional`](src/main/java/dev/deletejava/optional) |
| 12 | `Date`, `Calendar`, shared `SimpleDateFormat` | `java.time` | 8 | [`datetime`](src/main/java/dev/deletejava/datetime) |
| 13 | Apache HttpClient for simple calls | `java.net.http.HttpClient` (HTTP/3 on JDK 26+) | 11 / 21 | [`http`](src/main/java/dev/deletejava/http) |
| 14 | `synchronizedMap` + `synchronized` check-then-act | `ConcurrentHashMap.merge` | 8 | [`concurrentmaps`](src/main/java/dev/deletejava/concurrentmaps) |
| 15 | Static helpers called inside `super(...)` | Statements before `super()` | 25 | [`constructors`](src/main/java/dev/deletejava/constructors) |
| 16 | `Lists.partition` and hand-written window loops | Stream Gatherers | 24 | [`gatherers`](src/main/java/dev/deletejava/gatherers) |
| 17 | Startup and heap workarounds | AOT cache, compact object headers | 24 / 25 | [`bench`](bench) |
| | **Level 3: design and concurrency** | | | |
| 18 | Visitor pattern | `sealed` + record patterns in `switch` | 21 | [`patterns`](src/main/java/dev/deletejava/patterns) |
| 19 | Sized thread pools + `CompletableFuture` plumbing | Virtual threads, `Gatherers.mapConcurrent` | 21 / 24 | [`virtualthreads`](src/main/java/dev/deletejava/virtualthreads) |
| 20 | `ReentrantLock` workarounds for pinning | Plain `synchronized` again (JEP 491) | 24 | [`pinning`](src/main/java/dev/deletejava/pinning) |
| 21 | `ThreadLocal` + `try/finally remove()` | `ScopedValue` | 25 | [`scopedvalues`](src/main/java/dev/deletejava/scopedvalues) |

## Level 4, JDK 27 preview: delete it next

All four are still preview features in JDK 27, so the APIs may change before they become final.

| Delete | Replace with | Code |
|--------|--------------|------|
| `CompletableFuture` fan-out that can't cancel | `StructuredTaskScope` | [`structured`](src/preview/java/dev/deletejava/preview/structured) |
| Double-checked locking for lazy fields | `LazyConstant` | [`lazyconstants`](src/preview/java/dev/deletejava/preview/lazyconstants) |
| Range checks and casts on primitives | Primitive types in patterns | [`primitivepatterns`](src/preview/java/dev/deletejava/preview/primitivepatterns) |
| Hand-rolled PEM parsing / Bouncy Castle for PEM | `PEMEncoder` / `PEMDecoder` | [`pem`](src/preview/java/dev/deletejava/preview/pem) |

Bonus: the `before` files use `/** ... */` Javadoc, the `after` files use Markdown doc comments (`///`, Java 23).

## Run it

Requires JDK 25 or later and Maven.

```bash
mvn test                                   # on JDK 25: compiles before + after, checks they match
mvn test                                   # on JDK 27+: the same, plus src/preview (profile jdk27-preview, --enable-preview)

java scripts/before/Checksum.java pom.xml  # classic single-file program
java scripts/after/Checksum.java pom.xml   # same program as a compact source file
java scripts/after/ImportModule.java

bench/startup.sh 20                        # startup with and without an AOT cache
java -XX:-UseCompactObjectHeaders bench/ObjectHeaders.java
java -XX:+UseCompactObjectHeaders bench/ObjectHeaders.java
```

The preview profile switches on by itself when Maven runs on JDK 27 or later. To pick the JDK, set `JAVA_HOME`, e.g.
`JAVA_HOME=$(/usr/libexec/java_home -v 27) mvn test`.

The benchmark numbers in the article come from JDK 25.0.5 on an Apple M3 Pro. Yours will differ, which is the point of running them.

The `before` dependencies in `pom.xml` exist only to compile the `before` packages.
Delete those packages and you can delete the dependencies too. That's the point.
