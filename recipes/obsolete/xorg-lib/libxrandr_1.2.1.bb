require xorg-lib-common.inc
DESCRIPTION = "X11 Resize and Rotate extension library"
LICENSE = "BSD-X"
DEPENDS += "randrproto libxrender libxext"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "5860360f5b038cc728f388f875ce525d"
SRC_URI[archive.sha256sum] = "2d2df642eb8e0527936cdf4ed1c096f69df53e9b95e2532c8ed0cabd06a45407"

BBCLASSEXTEND = "nativesdk"

XORG_PN = "libXrandr"
