# webapp

Simple Web app allowing you to modify the URI and consequently return raw responses for all RPAS resources.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Usage

	LIST: /sdk/:resource-name
	SELECT /sdk/:resource-name/:resource-id
