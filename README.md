# GeoPackage TIFF Java

#### GeoPackage Tagged Image File Format Lib ####

The [GeoPackage Libraries](http://ngageoint.github.io/GeoPackage/) were developed at the [National Geospatial-Intelligence Agency (NGA)](http://www.nga.mil/) in collaboration with [BIT Systems](http://www.bit-sys.com/). The government has "unlimited rights" and is releasing this software to increase the impact of government investments by providing developers with the opportunity to take things in new directions. The software use, modification, and distribution rights are stipulated within the [MIT license](http://choosealicense.com/licenses/mit/).

### Pull Requests ###
If you'd like to contribute to this project, please make a pull request. We'll review the pull request and discuss the changes. All pull request contributions to this project will be released under the MIT license.

Software source code previously released under an open source license and then modified by NGA staff is considered a "joint work" (see 17 USC § 101); it is partially copyrighted, partially public domain, and as a whole is protected by the copyrights of the non-government authors and must be released according to the terms of the original open source license.

### About ###

[TIFF](http://ngageoint.github.io/geopackage-tiff-java/) is a Java library for reading and writing Tagged Image File Format files. It was primarily created to provide license friendly TIFF functionality to Android applications. Although developed as part of the [GeoPackage Libraries](http://ngageoint.github.io/GeoPackage/), this library does not contain GeoPackage functionality and can be used separately.  Implementation is based on the [TIFF specification](https://partners.adobe.com/public/developer/en/tiff/TIFF6.pdf) and this JavaScript implementation: https://github.com/constantinius/geotiff.js

### Usage ###

View the latest [Javadoc](http://ngageoint.github.io/geopackage-tiff-java/docs/api/)

#### Read ####

    //TODO

#### Write ####

    //TODO

### Installation ###

Pull from the [Maven Central Repository](http://search.maven.org/#artifactdetails|mil.nga|tiff|1.0.0|jar) (JAR, POM, Source, Javadoc)

    <dependency>
        <groupId>mil.nga</groupId>
        <artifactId>tiff</artifactId>
        <version>1.0.0</version>
    </dependency>

### Build ###

Build this repository using Eclipse and/or Maven:

    mvn clean install
