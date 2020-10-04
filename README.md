# banking-on-clojure-webapp

A Clojure web application using `clojure.spec` libraries for defining data and function contracts to be used for generative testing.

A [guide to the development of this project](http://practicalli.github.io/clojure-webapps/projects/banking-on-clojure/) is on the Practicalli ClojureWebApps website.

[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=svg)](https://circleci.com/gh/practicalli/banking-on-clojure-webapp)

Code Repository: [practicalli/banking-on-clojure-webapp](https://github.com/practicalli/banking-on-clojure-webapp)

## Development
Open the code in a Clojure aware editor and start a REPL session.

Run all the tests in the project using the Cognitect Labs test runner, setting the classpath to include `test` directory.  The aliases are included in the project `deps.edn` file.

```shell
clojure -A:test:runner
```

## Running the code
Use the `-M -m` option to set the main namespace to inform Clojure where it can find the `-main` function to start the code running.

```shell
clojure -M -m practicalli.banking-on-clojure
```
> Use the `-m` option by itself if using Clojure CLI tools version before 1.10.1.697

## Packaging / Deployment
Clojure is deployed as a Java archive (jar) file, an archive created using zip compression.  To package the code to run in a JVM environment, an uberjar is created which included the project code and the Clojure standard library.  This is called an uberjar.

Use the alias for depstar tool to build an uberjar for this project.  The alias is defined in the `deps.edn` file for this project.

```shell
clojure -A:uberjar
```

The code can be run from the uberjar on the command line

```shell
java -jar banking-on-clojure-webapp.jar
```


## License

Copyright © 2020 Practicalli

Distributed under the Creative Commons Attribution Share-Alike 4.0 International
