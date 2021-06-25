# Change Log
All notable changes to this project will be documented in this file.
Adheres to [Semantic Versioning](http://semver.org/).

---

## [2.0.3](https://github.com/ngageoint/tiff-java/releases/tag/2.0.3) (06-25-2021)

* Modifiable IOUtils copy buffer, defaulted at 8k byte chunks

## [2.0.2](https://github.com/ngageoint/tiff-java/releases/tag/2.0.2) (07-10-2020)

* Model pixel scale and model tiepoint retrieval methods
* Sample values byte buffer max capacity allocation check

## [2.0.1](https://github.com/ngageoint/tiff-java/releases/tag/2.0.1) (04-01-2019)

* xResolution and yResolution write fix
* Skip unknown tags while reading
* Allow headers to be read for unsupported compression types
* JPEG field tags
* Eclipse project cleanup
* Dropping "geopackage" from library name

## [2.0.0](https://github.com/ngageoint/tiff-java/releases/tag/2.0.0) (11-20-2017)

* Rasters modified to use buffers in place of arrays
* Deflate compression support
* Additional Rasters constructor options
* Handle missing samples per pixel with default value of 1
* Public access to tiff tags
* String Entry Value getter and setter
* maven-gpg-plugin version 1.6

## [1.0.3](https://github.com/ngageoint/tiff-java/releases/tag/1.0.3) (06-27-2017)

* Handle writing file directory entry ASCII values ending with more than one null

## [1.0.2](https://github.com/ngageoint/tiff-java/releases/tag/1.0.2) (06-12-2017)

* Handle fewer SampleFormat values specified than SamplesPerPixel. Defaults to 1 (unsigned integer data)

## [1.0.1](https://github.com/ngageoint/tiff-java/releases/tag/1.0.1) (03-02-2017)

* LZW Compression modified to handle non contiguous table codes

## [1.0.0](https://github.com/ngageoint/tiff-java/releases/tag/1.0.0) (10-04-2016)

* Initial Release
