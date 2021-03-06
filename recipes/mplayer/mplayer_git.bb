DESCRIPTION = "Open Source multimedia player."
SECTION = "multimedia"
PRIORITY = "optional"
HOMEPAGE = "http://www.mplayerhq.hu/"
DEPENDS = "libvpx live555 libdvdread libtheora virtual/libsdl ffmpeg xsp zlib libpng jpeg liba52 freetype fontconfig alsa-lib lzo ncurses lame libxv virtual/libx11 virtual/kernel \
	   ${@base_conditional('ENTERPRISE_DISTRO', '1', '', 'libmad liba52 lame', d)}"

RDEPENDS_${PN} = "mplayer-common"
LICENSE = "GPL"
SRC_URI = "git://repo.or.cz/mplayer/glamo.git;protocol=git;branch=mplayer2 \
   "

SRC_URI_append_armv7a = " \
	   file://0001-video-out-for-omapfb-support.patch \
	  "

# This is required for the collie machine only as all stacks in that
# machine seem to be set to executable by the toolchain. If someone
# discovers this is more general than please make this more general
# ie. for all armv4 machines.
SRC_URI_append_collie = "file://disable-executable-stack-test.patch"

SRCREV = "5a2f2b69c67011e7c5c0e0e76f9135f33303ac58"

PACKAGE_ARCH_collie = "collie"
PACKAGE_ARCH_c7x0 = "c7x0"
PACKAGE_ARCH_hx4700 = "hx4700"

ARM_INSTRUCTION_SET = "ARM"

RCONFLICTS_${PN} = "mplayer-atty"
RREPLACES_${PN} = "mplayer-atty"

PV = "2.0+gitr${SRCPV}"
DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_shr = "2"

PARALLEL_MAKE = ""

S = "${WORKDIR}/git"

FILES_${PN} = "${bindir}/mplayer ${libdir} /usr/etc/mplayer/"
CONFFILES_${PN} += "/usr/etc/mplayer/input.conf \
                    /usr/etc/mplayer/example.conf \
                    /usr/etc/mplayer/codecs.conf \
                   "

inherit autotools pkgconfig

# We want a kernel header for armv7a, but we don't want to make mplayer machine specific for that
STAGING_KERNEL_DIR = "${STAGING_DIR}/${MACHINE_ARCH}${TARGET_VENDOR}-${TARGET_OS}/kernel"

EXTRA_OECONF = " \
	--prefix=/usr \
	--mandir=${mandir} \
	--target=${SIMPLE_TARGET_SYS} \
	\
	--enable-largefiles \
	--disable-lirc \
	--disable-lircc \
	--disable-joystick \
	--disable-vm \
	--disable-xf86keysym \
	--enable-tv \
	--enable-tv-v4l1 \	 
	--enable-tv-v4l2 \
	--disable-tv-bsdbt848 \
	--enable-rtc \
	--enable-networking \
	--disable-smb \
	--enable-live \
	--disable-dvdnav \
	--enable-dvdread \
	--disable-dvdread-internal \
	--disable-libdvdcss-internal \
	--disable-cdparanoia \
	--enable-freetype \
	--enable-menu \
	--enable-sortsub \
	--disable-fribidi \
	--disable-enca \
	--disable-ftp \
	--disable-vstream \
	\
	--disable-gif \
	--enable-png \
	--enable-jpeg \
	--disable-libcdio \
	--disable-qtx \
	--disable-xanim \
	--disable-real \
	--disable-xvid \
	\
	--disable-speex \
	--enable-theora \
	--disable-ladspa \
	--disable-libdv \
	--enable-mad \
	--disable-xmms \
	--disable-musepack \
	\
	--disable-gl \
	--disable-vesa \
	--disable-svga \
	--enable-sdl \
	--disable-aa \
	--disable-caca \
	--disable-ggi \
	--disable-ggiwmh \
	--disable-directx \
	--disable-dxr3 \
	--disable-dvb \
	--disable-mga \
	--disable-xmga \
	--enable-xv \
	--disable-xvmc \
	--disable-vm \
	--disable-xinerama \
	--enable-x11 \
	--enable-fbdev \
	--disable-3dfx \
	--disable-tdfxfb \
	--disable-s3fb \
	--disable-directfb \
	--disable-bl \
	--disable-tdfxvid \
	--disable-tga \
	--disable-pnm \
	--disable-md5sum \
	\
	--enable-alsa \
	--enable-ossaudio \
	--disable-arts \
	--disable-esd \
	--disable-pulse \
	--disable-jack \
	--disable-openal \
	--disable-nas \
	--disable-sgiaudio \
	--disable-sunaudio \
	--disable-win32waveout \
	--enable-select \
	\
	--extra-libs=' -lBasicUsageEnvironment -lUsageEnvironment -lgroupsock -lliveMedia -lstdc++' \
"

EXTRA_OECONF_append_armv6 = " --enable-armv6"
EXTRA_OECONF_append_armv7a = " --enable-armv6 --enable-neon"

EXTRA_OECONF_append_om-gta02 = " --enable-glamo"

#build with support for the iwmmxt instruction and pxa270fb overlay support (pxa270 and up)
#not every iwmmxt machine has the lcd connected to pxafb, but building the module doesn't hurt 
MY_ARCH := "${PACKAGE_ARCH}"
PACKAGE_ARCH = "${@base_contains('MACHINE_FEATURES', 'iwmmxt', 'iwmmxt', '${MY_ARCH}',d)}"

MY_TARGET_CC_ARCH := "${TARGET_CC_ARCH}"
TARGET_CC_ARCH = "${@base_contains('MACHINE_FEATURES', 'iwmmxt', '-march=iwmmxt -mtune=iwmmxt', '${MY_TARGET_CC_ARCH}',d)}"

EXTRA_OECONF_append = " ${@base_contains('MACHINE_FEATURES', 'iwmmxt', ' --enable-iwmmxt', '',d)} "
EXTRA_OECONF_append = " ${@base_contains('MACHINE_FEATURES', 'x86', '--enable-runtime-cpudetection', '',d)} "

FULL_OPTIMIZATION = "-fexpensive-optimizations -fomit-frame-pointer -frename-registers -O4 -ffast-math"
FULL_OPTIMIZATION_armv7a = "-fexpensive-optimizations  -ftree-vectorize -fomit-frame-pointer -O4 -ffast-math"
BUILD_OPTIMIZATION = "${FULL_OPTIMIZATION}"

do_configure_prepend_armv7a() {
	cp ${STAGING_KERNEL_DIR}/arch/arm/plat-omap/include/mach/omapfb.h ${S}/libvo/omapfb.h || true
 	cp ${STAGING_KERNEL_DIR}/include/asm-arm/arch-omap/omapfb.h ${S}/libvo/omapfb.h || true
	cp ${STAGING_KERNEL_DIR}/include/linux/omapfb.h ${S}/libvo/omapfb.h || true
 	sed -e 's/__user//g' -i ${S}/libvo/omapfb.h || true
}

CFLAGS_append = " -I${S}/libdvdread4 "

do_configure() {
	sed -i 's|/usr/include|${STAGING_INCDIR}|g' ${S}/configure
	sed -i 's|/usr/lib|${STAGING_LIBDIR}|g' ${S}/configure
	sed -i 's|/usr/\S*include[\w/]*||g' ${S}/configure
	sed -i 's|/usr/\S*lib[\w/]*||g' ${S}/configure
	sed -i 's|_install_strip="-s"|_install_strip=""|g' ${S}/configure
	sed -i 's|HOST_CC|BUILD_CC|' ${S}/Makefile

	export SIMPLE_TARGET_SYS="$(echo ${TARGET_SYS} | sed s:${TARGET_VENDOR}::g)"
	./configure ${EXTRA_OECONF}
	
}

do_compile () {
	oe_runmake
}

do_install_append() {
        install -d ${D}/usr/etc/mplayer
        install ${S}/etc/input.conf ${D}/usr/etc/mplayer/
        install ${S}/etc/example.conf ${D}/usr/etc/mplayer/
        install ${S}/etc/codecs.conf ${D}/usr/etc/mplayer/
}
