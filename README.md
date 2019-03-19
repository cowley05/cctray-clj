# clj-cctray

[![Clojars Project](https://img.shields.io/clojars/v/clj-cctray.svg)](https://clojars.org/clj-cctray) [![CircleCI](https://circleci.com/gh/build-canaries/clj-cctray.svg?style=shield)](https://circleci.com/gh/build-canaries/clj-cctray)

A Clojure library designed to parse the cctray format into a user friendly clojure map.

## Usage

```clojure
(ns your-app.whatever
  (:require [clj-cctray.core :as :parser]))

; some-source can be a File, InputStream or String naming a URI

(parser/get-projects some-source)

; or with options

(parser/get-projects some-source {:some-option "the-value"})
```

## Options

Options are a map of keywords with values that can be passed to modify the returned project map list in some way or set global library settings.

Most options will require the value to be in a specific format and checks are not performed, this is your responsibility.
Failure to set the value correctly will most likely result in an exception being thrown.

- `:server`
  A keyword representing the CI server the xml is coming from to allow any server specific parsing. Currently the only
  values that trigger specific parsing are `:go`, `:snap` and `:circle` any other values will result in this option being ignored.

- `:normalise`
  This will cause the given map keys (if they exist) to be normalised (see below for more details about normalisation).
  Can take a collection of keys to normalise or a `truthy` value to normalise the following `:name`, `:stage`, `:job` and `:owner`.

- `:print-dates`
  This causes the next and last build times to be printed out as strings instead of being returned as DateTime objects.
  Can take a string specifying a format to use or a `truthy` value to default to the ISO format. A complete list of format
  patterns can be found on the [Joda-Time website](http://www.joda.org/joda-time/key_format.html)

## Map keys

- `:name`
  The name of the project.

- `:activity`
  The current state of the project as a keyword. Either `:sleeping`, `:building` or `:checking-modifications`.

- `:last-build-status`
  A brief description of the last build as a keyword. Either `:success`, `:failure`, `:error`, `:exception` or `:unknown`.

- `:last-build-label`
  The build label exactly as it appeared in the xml file.

- `:last-build-time`
  When the last build occurred as a JodaTime `DateTime` object.

- `next-build-time`
  When the next build is scheduled to occur (or when the next check to see whether a build should be performed is
  scheduled to occur) as a JodaTime `DateTime` object.

- `:web-url`
  A URL for where more detail can be found about this project exactly as it appeared in the xml file.

- `:prognosis`
  The derived health of the project based on the activity and last build status as a keyword. Either `:sick-building`,
  `:sick`, `:healthy`, `:healthy-building` or `:unknown`

- `:messages`
  A seq of messages as strings that exist for this project or an empty seq if there are no messages.

If `:normalise` is used as an option then for any key that has been normalised an `:unnormalised-` key will be added
that contains the original unnormalised value. For example if `:name` was normalised an `:unnormalised-name` key would 
exist.

If `:go` is used as the `:server` option then the following keys will also be added:

- `:stage`
  The projects stage name.

- `:job`
  The projects job name.

If `:circle` is used as the `:server` option then the following keys will also be added:

- `:owner`
  The name of the project owner, this is the Github user or organisation name.

## Normalised strings

Normalised strings are lower case and "sentenceized" which means camel, snake, kebab and dot cased words are converted to
normal sentences with spaces. The following exceptions apply to the splitting rules:
                              
1. Multiple uppercased letters in a row
2. Digits separated by dots

This is in an attempt to keep acronyms/initialisms and version numbers from getting split.

```
CamelCased_SNAKE-kebab.dot_JSON-1.2.3 => camel cased snake kebab dot json 1.2.3
```

## Local files and remote files can be read

By default, you can load local files on your disk and remote files over http or https. If you are using `clj-cctray` on
a webserver then we recommend you ensure only http[s] urls are being parsed or you load the url yourself and just pass
an InputStream to `clj-cctray`.

## Installation

`clj-cctray` is available as a Maven artifact from [Clojars](http://clojars.org/clj-cctray)

**Note:** v1.0.1 is broken on Clojars, see [issue #22](https://github.com/build-canaries/clj-cctray/issues/22) for more details.

## Development

clj-cctray has primarily been developed from XML generated by the [Go](http://www.thoughtworks.com/products/go-continuous-delivery)
CI server, so please help us by raising issues if it doesn't work correctly with other servers.

### CC Tray XML Spec

See https://cctray.org

### API docs

The complete API docs can be found here: http://build-canaries.github.io/clj-cctray/

### Contributing

If you would like to add a feature/fix a bug for us please create a pull request. Be sure to include or update any tests
if you want your pull request accepted.

You can run linting and tests using `./lein.sh pre-push`.

## License

Copyright © 2014 - 2019 Build Canaries

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
