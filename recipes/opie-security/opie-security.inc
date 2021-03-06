DESCRIPTION = "Security settings dialog for the Opie environment."
SECTION = "opie/settings"
PRIORITY = "optional"
LICENSE = "GPL"
APPNAME = "security"

S = "${WORKDIR}/${APPNAME}"

inherit opie

# FILES plugins/application/libsecurity.so* bin/security apps/Settings/Security.desktop pics/security
do_install() {
        install -d ${D}${palmtopdir}/pics/${APPNAME}/

	for icon in Security.png lock.png multiauth.png sync.png users.png ownerinfo.png;
	do
	    install -m 0644 ${WORKDIR}/pics/${APPNAME}/$icon ${D}${palmtopdir}/pics/${APPNAME}/
	done
}

