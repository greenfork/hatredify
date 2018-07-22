
> DISCLAIMER: This page may contain offensive content. Please leave this page
> immideately if you could be offended.

# hatredify

Add some **HATRED** to your text!

This is a web-application which changes the text you supply as follows:

1. Find all positive adjectives.
2. Change them to their opposites.
3. Make them upper-case.

Which makes it evil. Really evil.

![Main screen][1]

[1]: docs/main_screen.png

## Motivation

This is a project mainly for self-educational purposes. For this reason it is
also very bloated and may be inaccurate in some places.

## Production
### Prerequisites

You should have [Java Runtime Envorionment][2] installed on your system.

[2]: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

### Running

Unzip the latest [release][3] to the custom folder and run the `.jar` file.

GNU/Linux system example:

    mkdir hatredify
    cp hatredify-1.0.zip hatredify/
    7z x hatredify-1.0.zip
    java -jar hatredify.jar

You can also double-click on the `hatredify.jar` and it should run.

Now open you browser at [`localhost:3000`][4] and put some kind words in it.

[3]: https://github.com/greenfork/hatredify/releases
[4]: http://localhost:3000/

## Development
### Prerequisites

You should have [Clojure][5] 1.9.0 or above installed.

Additionally you will need [Leiningen][6] 2.0 or above installed.

[5]: https://clojure.org/guides/getting_started
[6]: https://github.com/technomancy/leiningen

### Running

Start a web server application with:

    lein run

Run tests with:

    lein test

Run benchmarks with:

    lein benchmark

## Documentation

The core logic is in [`hatredify.lib.core`][7]. This file consists of [pure functions][8] only.
It has the main function `hatredify-text` which forms a pipeline, consisting of smaller
functions above. You can read the code, tests at [hatredify.test.lib.core][9] have a good
explanation of what is happening at every stage.

Function `hatredify-text` uses a dictionary map `dict` which is currently implemented with
the help of [Wordnet][10]. This implementation is in namespace [`hatredify.lib.wordnet`][11].
It heavily uses an updated version of [`clj-wordnet`][12] library which maps every positive
word to its antonyms as follows:

1. Read a prepared list of positive words.
2. For one word from a list:
    - Find all similar words to it.
    - Find all synonyms to all the similar words.
    - Find all antonyms for each of the word in the combined list.
    - Put every antonym found in the dictionary as the antonym to the original word.
3. Repeat step 2 for all remaining words.

[7]: https://github.com/greenfork/hatredify/blob/master/src/clj/hatredify/lib/core.clj
[8]: https://en.wikipedia.org/wiki/Pure_function
[9]: https://github.com/greenfork/hatredify/blob/master/test/clj/hatredify/test/lib/core.clj
[10]: https://wordnet.princeton.edu/
[11]: https://github.com/greenfork/hatredify/blob/master/src/clj/hatredify/lib/wordnet.clj
[12]: https://github.com/greenfork/clj-wordnet

## License

Copyright (c) 2018 Dmitriy Matveyev

Licensed under MIT, set LICENSE.md.
