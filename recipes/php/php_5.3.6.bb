require php.inc

PR = "${INC_PR}.0"

SRC_URI += "file://acinclude-xml2-config.patch \
            file://php-m4-divert.patch"

SRC_URI_append_pn-php += "file://iconv.patch \
            file://imap-fix-autofoo.patch \
            file://pear-makefile.patch \
            file://phar-makefile.patch \
            file://php_exec_native.patch \
            "

SRC_URI[md5sum] = "2286f5a82a6e8397955a0025c1c2ad98"
SRC_URI[sha256sum] = "30ae880f22e3ee93eccc9b80e3c58b0d6364d139bb4366dcc27f2fab658d3198"
