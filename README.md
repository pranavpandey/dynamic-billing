<img src="./graphics/icon.png" width="160" height="160" align="right" hspace="20">

# Dynamic Billing

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Release](https://img.shields.io/maven-central/v/com.pranavpandey.android/dynamic-billing)](https://search.maven.org/artifact/com.pranavpandey.android/dynamic-billing)

A library to implement Google Play in-app products and subscriptions on Android 
4.1 (API 16) and above.

> It uses [AndroidX][androidx] so, first [migrate][androidx-migrate] your project to AndroidX.
<br/>It is dependent on Java 8 due to the dependency on [Dynamic Utils][dynamic-utils].

---

## Contents

- [Installation](#installation)
- [Usage](#usage)
  - [Sponsor](#sponsor)
  - [Dependency](#dependency)
- [License](#license)

---

## Installation

It can be installed by adding the following dependency to your `build.gradle` file:

```groovy
dependencies {
    // For AndroidX enabled projects.
    implementation 'com.pranavpandey.android:dynamic-billing:1.0.2'
}
```

---

## Usage

It provides an `interface` that can be implemented by the `application` or `activity` class to 
perform various operations accordingly. It also has support to create an in-app `product` 
or `subscription` that can be linked to a `feature` so that it can be enabled on-demand after 
a successful purchase.

> For a complete reference, please read the [documentation][documentation].

### Sponsor

Please become a [sponsor][sponsor] to get a detailed guide and priority support.

### Dependency

It depends on the [dynamic-utils][dynamic-utils] to perform various internal operations. 
So, its functions can also be used to perform other useful operations.

---

## Author

Pranav Pandey

[![GitHub](https://img.shields.io/github/followers/pranavpandey?label=GitHub&style=social)](https://github.com/pranavpandey)
[![Follow on Twitter](https://img.shields.io/twitter/follow/pranavpandeydev?label=Follow&style=social)](https://twitter.com/intent/follow?screen_name=pranavpandeydev)
[![Donate via PayPal](https://img.shields.io/static/v1?label=Donate&message=PayPal&color=blue)](https://paypal.me/pranavpandeydev)

---

## License

    Copyright 2022 Pranav Pandey

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[androidx]: https://developer.android.com/jetpack/androidx
[androidx-migrate]: https://developer.android.com/jetpack/androidx/migrate
[documentation]: https://pranavpandey.github.io/dynamic-billing
[sponsor]: https://github.com/sponsors/pranavpandey
[dynamic-utils]: https://github.com/pranavpandey/dynamic-utils
[dynamic-support]: https://github.com/pranavpandey/dynamic-support
