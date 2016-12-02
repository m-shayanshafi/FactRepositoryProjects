WireMock - a web service test double for all occasions
======================================================

[![Build Status](https://travis-ci.org/tomakehurst/wiremock.svg?branch=master)](https://travis-ci.org/tomakehurst/wiremock)

**PLEASE NOTE: The 2.0-beta branch is now the active line of development so we're no longer taking pull requests against version 1.x (master)**

Key Features
------------
	
-	HTTP response stubbing, matchable on URL, header and body content patterns
-	Request verification
-	Runs in unit tests, as a standalone process or as a WAR app
-	Configurable via a fluent Java API, JSON files and JSON over HTTP
-	Record/playback of stubs
-	Fault injection
-	Per-request conditional proxying
-   Browser proxying for request inspection and replacement
-	Stateful behaviour simulation
-	Configurable response delays
 

Full documentation can be found at [wiremock.org](http://wiremock.org/ "wiremock.org")

Questions and Issues
--------------------
If you have a question about WireMock, or are experiencing a problem you're not sure is a bug please post a message to the
[WireMock mailing list](https://groups.google.com/forum/#!forum/wiremock-user).

On the other hand if you're pretty certain you've found a bug please open an issue.

Contributing
------------
We welcome bug fixes and new features in the form of pull requests. If you'd like to contribute, please be mindful of the
following guidelines:
* All changes should include suitable tests, whether to demonstrate the bug or exercise and document the new feature.
* Please make one change per pull request.
* If the new feature is significantly large/complex/breaks existing behaviour, please first post a summary of your idea
on the mailing list to generate a discussion. This will avoid significant amounts of coding time spent on changes that ultimately get rejected.
* Try to avoid reformats of files that change the indentation, tabs to spaces etc., as this makes reviewing diffs much
more difficult.

