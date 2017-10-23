#Change Log
All notable changes to this project will be documented in this file.
Adheres to [Semantic Versioning](http://semver.org/).

---

## 2.0.0 (TBD)

* Rasters modified to use buffers in place of arrays
* Deflate compression support
* Additional Rasters constructor options
* Handle missing samples per pixel with default value of 1
* Public access to tiff tags
* String Entry Value getter and setter
* maven-gpg-plugin version 1.6

## [1.0.3](https://github.com/ngageoint/geopackage-tiff-java/releases/tag/1.0.3) (06-27-2017)

* Handle writing file directory entry ASCII values ending with more than one null

## [1.0.2](https://github.com/ngageoint/geopackage-tiff-java/releases/tag/1.0.2) (06-12-2017)

* Handle fewer SampleFormat values specified than SamplesPerPixel. Defaults to 1 (unsigned integer data)

## [1.0.1](https://github.com/ngageoint/geopackage-tiff-java/releases/tag/1.0.1) (03-02-2017)

* LZW Compression modified to handle non contiguous table codes

## [1.0.0](https://github.com/ngageoint/geopackage-tiff-java/releases/tag/1.0.0) (10-04-2016)

* Initial Release
