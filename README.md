# Deleting My Dusty Java Habits

A 1-to-1 before/after catalogue of Java 8-era code you can delete on Java 25 LTS, with tests that prove each replacement is safe.

Companion repo for the article *"The Best PR Has More Red Than Green: A 1-to-1 Journey to Deleting My Dusty Java Habits"*.

Every topic has a `before` package (the code we all still have) and an `after` package (the same thing on Java 25 LTS).
The `after` packages use the JDK alone: no Lombok, Guava, Commons Lang, Commons IO or Apache HttpClient.
Each test runs **both** versions and asserts that they behave the same, so the deletion is safe.
Where the `before` code is actually broken, the test proves that too.

## Java 25 LTS: delete it today

21 cases, from search-and-replace to design and concurrency.
The article walks through the ones marked ★, and the **Article** column gives their number there. The rest are only here.

| # | Delete | Replace with | Since | Code | Article |
|---|--------|--------------|-------|------|---------|
| | **Level 1: Break the Muscle Memory (Search & Replace)** | | | | |
| 1 | `ignored`, `unused`, `e`, `k` | Unnamed variables `_` | 22 | [`unnamed`](src/main/java/dev/deletejava/unnamed) |  |
| 2 | `collect(Collectors.toList())` | `.toList()` (unmodifiable!) | 16 | [`streams`](src/main/java/dev/deletejava/streams) | ★ #1 |
| 3 | `"..." + "\n" +` SQL and JSON | Text blocks + `formatted()` | 15 | [`textblocks`](src/main/java/dev/deletejava/textblocks) |  |
| 4 | Guava / Commons Lang one-liners | `String`, `Objects`, `List`, `HexFormat`, `Math.clamp`, sequenced collections | 9–21 | [`utilities`](src/main/java/dev/deletejava/utilities) | ★ #2 |
| 5 | Commons IO `FileUtils` / `IOUtils` | `Files`, `InputStream.transferTo` | 9–12 | [`fileio`](src/main/java/dev/deletejava/fileio) | ★ #2 |
| 6 | Switch statements with `break` | Switch expressions | 14 | [`switches`](src/main/java/dev/deletejava/switches) |  |
| 7 | `instanceof` followed by a cast | Type patterns in `switch`, `case null` | 16 / 21 | [`instanceofcast`](src/main/java/dev/deletejava/instanceofcast) | ★ #3 |
| 8 | Removed and deprecated JVM flags | Nothing | — | [`JvmFlagsTest`](src/test/java/dev/deletejava/JvmFlagsTest.java) |  |
| 9 | `public class` + `static void main` + imports in scripts | Compact source files, `import module` | 25 | [`scripts`](scripts) | ★ #4 |
| | **Level 2: Mind the Semantics (Change a Type or an API)** | | | | |
| 10 | Lombok `@Value` value objects | `record` + compact constructor | 16 | [`records`](src/main/java/dev/deletejava/records) | ★ #5 |
| 11 | `return null`, `isPresent()` + `get()` | `Optional` used properly | 8–16 | [`optional`](src/main/java/dev/deletejava/optional) | ★ #6 |
| 12 | `Date`, `Calendar`, shared `SimpleDateFormat` | `java.time` | 8 | [`datetime`](src/main/java/dev/deletejava/datetime) | ★ #7 |
| 13 | Apache HttpClient for simple calls | `java.net.http.HttpClient` (HTTP/3 on JDK 26+) | 11 / 21 | [`http`](src/main/java/dev/deletejava/http) |  |
| 14 | `synchronizedMap` + `synchronized` check-then-act | `ConcurrentHashMap.merge` | 8 | [`concurrentmaps`](src/main/java/dev/deletejava/concurrentmaps) |  |
| 15 | Static helpers called inside `super(...)` | Statements before `super()` | 25 | [`constructors`](src/main/java/dev/deletejava/constructors) |  |
| 16 | `Lists.partition` and hand-written window loops | Stream Gatherers | 24 | [`gatherers`](src/main/java/dev/deletejava/gatherers) |  |
| 17 | Startup and heap workarounds | AOT cache, compact object headers | 24 / 25 | [`bench`](bench) | ★ #8 |
| | **Level 3: Rethink the Runtime (Design & Concurrency)** | | | | |
| 18 | Visitor pattern | `sealed` + record patterns in `switch` | 21 | [`patterns`](src/main/java/dev/deletejava/patterns) |  |
| 19 | Sized thread pools + `CompletableFuture` plumbing | Virtual threads, `Gatherers.mapConcurrent` | 21 / 24 | [`virtualthreads`](src/main/java/dev/deletejava/virtualthreads) | ★ #9 |
| 20 | `ReentrantLock` workarounds for pinning | Plain `synchronized` again (JEP 491) | 24 | [`pinning`](src/main/java/dev/deletejava/pinning) | ★ #9 |
| 21 | `ThreadLocal` + `try/finally remove()` | `ScopedValue` | 25 | [`scopedvalues`](src/main/java/dev/deletejava/scopedvalues) |  |

## Level 4, JDK 27 preview: delete it next

All four are still preview features in JDK 27, so the APIs may change before they become final.

| Delete | Replace with | Code |
|--------|--------------|------|
| `CompletableFuture` fan-out that can't cancel | `StructuredTaskScope` | [`structured`](src/preview/java/dev/deletejava/preview/structured) |
| Double-checked locking for lazy fields | `LazyConstant` | [`lazyconstants`](src/preview/java/dev/deletejava/preview/lazyconstants) |
| Range checks and casts on primitives | Primitive types in patterns | [`primitivepatterns`](src/preview/java/dev/deletejava/preview/primitivepatterns) |
| Hand-rolled PEM parsing / Bouncy Castle for PEM | `PEMEncoder` / `PEMDecoder` | [`pem`](src/preview/java/dev/deletejava/preview/pem) |

Bonus: the `before` files use `/** ... */` Javadoc, the `after` files use Markdown doc comments (`///`, Java 23).

## Use it with your coding agent

LLMs learned Java from a decade of legacy repositories, so they still default to Java 8 idioms unless you tell them otherwise.
Each pair here is a small, verified example of a "before" pattern and its replacement, and the tests document the traps.
Point your agent (Claude Code, Cursor, Copilot...) at this repo and give it a prompt like this one:

```text
Use https://github.com/DMColmi/java-delete-2026 as a reference. For each before/after
pair, find the "before" pattern in this repository and replace it with the "after"
version, as long as our target JDK supports it. Read the matching test first: it
documents where the two versions behave differently (null handling, unmodifiable
lists, missing directories, HTTP error codes). One pattern per commit, and run the
test suite after each one.
```

One pattern per commit keeps every diff small enough to review and easy to revert.
And review them: the tests here prove the examples are equivalent, not that your code is.

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
