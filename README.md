Android SQLite Cursor Loader
============================
This is like the framework's `CursorLoader` but for SQLite. Content changes not detected, do it yourself: call the `yourLoader.onContentChanged()` method after insert/delete/update.

There is also a version for using with support library.

Setup
=====
Maven
-----
```xml
<dependency>
    <groupId>org.deejdev.database.sqlitecursorloader</groupId>
    <artifactId>sqlitecursorloader</artifactId>
    <version>1.1</version>
    <type>aar</type>
</dependency>
```
Gradle
------
```groovy
dependencies {
    ...
    compile 'org.deejdev.database.sqlitecursorloader:sqlitecursorloader:1.1'
}
```

License
=======

    Copyright 2014 Maxim Naumov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
