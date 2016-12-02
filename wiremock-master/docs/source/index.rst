WireMock
========

WireMock is a flexible library for stubbing and mocking web services. Unlike general purpose mocking tools it works by
creating an actual HTTP server that your code under test can connect to as it would a real web service.

It supports HTTP response stubbing, request verification, proxy/intercept, record/playback of stubs and fault injection,
and can be used from within a unit test or deployed into a test environment.

Although it's written in Java, there's also a JSON API so you can use it with pretty much any language out there.


Video - Using WireMock in Practice
----------------------------------
Rob Elliot and I recently gave a presentation at Skillsmatter to the London Java Community demonstrating some of the ways
we use WireMock in practice after 4 years of using it.

.. raw:: html

   <p><a href="https://skillsmatter.com/skillscasts/6853-scalable-management-of-test-data-making-tests-readable#video" target="_blank">Watch the video here</a>.</p>


New new new! Version 2.0 in beta
--------------------------------
WireMock 2.0 is now in development. We're using the major version bump to make a few breaking changes (but nothing too major, so don't worry!).

Changes from 1.x include:

* Dropping support for JDK 6 and 7, which allows us to use some newer libraries we previously couldn't.
* Upgrading to Jetty 9.2, which is higher performing, and fixes some bugs.
* Upgrading to JSONPath 2.0.0.
* Improving the extensions API, supporting ``Response`` transformations directly in addition to ``ResponseDefinition``.
* Support for custom matchers.
* Putting the standalone JAR under its own Maven artifact so it can have it's own POM (avoiding the need to exlclude all large swathes of dependencies).
* Better logging.

If there's something you'd like to see added, particularly something involving a breaking change, please post a topic on the `mailing list <https://groups.google.com/forum/#!forum/wiremock-user>`_.



What's it for?
--------------

Some scenarios you might want to consider WireMock for:

* Testing mobile apps that depend on third-party REST APIs
* Creating quick prototypes of your APIs
* Injecting otherwise hard-to-create errors in 3rd party services
* Any unit testing of code that depends on a web service


Who makes it?
-------------
WireMock was created and is maintained by `Tom Akehurst <http://www.tomakehurst.com/about>`_.

The following people have been kind enough to submit improvements:

* `Tim Perry <https://github.com/pimterry>`_
* `Dominic Tootell <https://github.com/tootedom>`_
* `mangotang <https://github.com/mangotang>`_
* `Rob Elliot <https://github.com/mahoney>`_
* `Neil Green <https://github.com/neilg>`_
* `Rowan Hill <https://github.com/rowanhill>`_
* `Christian Trimble <https://github.com/ctrimble>`_
* `Aman King <https://github.com/amanking>`_
* `Oliver Schönherr <https://github.com/oschoen>`_
* `Jay Goldberg <https://github.com/carthoris>`_
* `Matt Nathan <https://github.com/mattnathan>`_



Why shouldn't I just use my favourite mocking library?
------------------------------------------------------

Mocking HTTP client classes in a way that adequately reflects their real behaviour is pretty hard. Creating real HTTP
exchanges alleviates this by allowing you to use your production HTTP client implementation in your tests.

Object based mocking isn't really suitable for acceptance/functional testing scenarios.
WireMock can be run as a standalone service or deployed into a servlet container to enable it to be installed into your dev/test
environments.


I like the idea, but the implementation stinks/you've missed something I need/it's the wrong colour
---------------------------------------------------------------------------------------------------

Here are some alternative JVM based libraries with similar goals:

* `Betamax <http://freeside.co/betamax/>`_
* `REST-driver <https://github.com/rest-driver/rest-driver>`_
* `MockServer <http://www.mock-server.com/>`_
* `Moco <https://github.com/dreamhead/moco>`_


I couldn't possibly be seen using Java, I've got my image to think about!
-------------------------------------------------------------------------

Luckily, `Rowan Hill <https://github.com/rowanhill>`_ has built a `PHP binding <https://github.com/rowanhill/wiremock-php>`_,
so you can bring it to your next Shoreditch hackathon without fear of ridicule!


Contents
--------

.. toctree::
   :maxdepth: 1

   getting-started
   running-standalone
   https
   junit-rule
   java-usage
   stubbing
   verifying
   proxying
   record-playback
   stateful-behaviour
   simulating-faults
   extending-wiremock





