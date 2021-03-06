###############################################################################
# Reciprocal Net project
#
# recipnet-site.spec
#
# 11-Mar-2003: ekoperda added changelog; updated file for 0.6.0 release
# 01-Jul-2003: ekoperda made j2sdk dependency more flexible
# 10-Jul-2003: midurbin added recipnet-getrepositorysize shell script
# 17-Jul-2003: ekoperda adjusted to support shell scripts in new directory
#              native/scripts and added support for librecipnet_server.so
# 31-Jul-2003: nsanghvi changed ownership and file permissions for 
#              recipnetd.conf
# 07-Apr-2004: cwestnea fixed typo in %post section
# 08-Apr-2004: ekoperda added file /usr/bin/recipnet-httpddetect to
#              distribution
# 09-Apr-2004: midurbin replaced mod_recipnet_auth.so with different versions
#              for different distributions
# 15-Apr-2004: ekoperda increased dependency versions for 0.6.2 release
# 12-Apr-2004: cwestnea added a test to %post to support the name change
# 07-May-2004: cwestnea removed rmiregistry and drmiregistry
# 02-Jul-2004: ekoperda updated %files section to support multiple versions
#              of mod_recipnet_auth 
# 04-Feb-2005: ekoperda made various changes to support Fedora Core 3
# 15-Jun-2005: ekoperda updated % install section to support uploader applet
# 04-Aug-2005: ekoperda updated % build section to propagate Ant properties
#              related to applet signing
# 12-Oct-2005: midurbin added lines to % pre and % post sections to set an
#              appropriate SELinux context for data directory and files
# 14-Nov-2006: jobollin removed the Copyright: tag because rpm-build 4.4.2
#              refused to permit it
# 18-Nov-2006: jobollin added the recipnet-mergeconfig script to the package
# 19-Nov-2006: jobollin changed the required httpd MMN
# 27-Dec-2007: ekoperda added ref to xalan-j2-serializer.jar to match changes
#              by the Xalan project
# 29-Dec-2007: ekoperda removed references to recipnet-webapp-ap22.conf
# 31-Dec-2007: ekoperda added dependencies on xml-commons and dom
# 31-Dec-2007: ekoperda modernized dependencies for webapp subpackage
# 01-Jan-2008: ekoperda removed references to mod_recipnet_auth; added
#              references to ModRewriteAuthConnector
# 02-Jan-2008: ekoperda added an selinux policy module
# 02-Jan-2008: ekoperda adjusted the 'recipnet' user account inside %pre script
# 02-Jan-2008: ekoperda added site-example-index.html file
# 03-Jan-2008: ekoperda added recipnet-backup utility
# 04-Jan-2008: ekoperda fixed bug #1856 inside the %pre script
# 05-Jan-2008: ekoperda fixed bug #1863 inside the %pre and %post scripts
# 06-Jan-2008: ekoperda fixed bug #1869 inside the %pre and %post scripts
# 09-Jan-2008: ekoperda fixed bug #1876 inside the %post scripts
# 11-Jan-2008: ekoperda changed 'License' tag
# 20-Mar-2009: ekoperda refreshed dependencies for RHEL 5.3 and release 0.9.1
###############################################################################



###############################################################################
# BASIC INFORMATION
#   Information text fields about the RPM are here.  Also the dependency list.
#   There are four sections here: options for each of the three subpackages
#   (server, webapp, utils), and a fourth one that contains common information.
#   Values of the form '@valuekeyword@' are substituted with real values by the
#   build script at compile time.
###############################################################################
Name: recipnet-site
Version: 0.9.1
Release: 50
Source: recipnet-snapshot-HEAD-0376-20090321-source.tgz
Patch0: recipnet-site-0.9.1-20160229.patch
Buildroot: /tmp/recipnet-site-0.9.1-50
Summary: A distributed database for molecular structures
Group: System Environment/Daemons
Vendor: Indiana University Molecular Structure Center
License: GPL
URL: http://www.reciprocalnet.org/
%description
The base 'recipnet' package never gets built, so this line is just a 
placeholder.

%package server
Summary: Reciprocal Net site software daemon - recipnetd
Group: Networking/Daemons
Requires: cvs >= 1.11.22
Requires: java-sun >= 1.6.0.11
Requires: jaxp_transform_impl
Requires: mysql-connector-java >= 5.0.8
Requires: mysql-server >= 5.0.45
Requires: xml-commons >= 1.3.02
Requires: xml-commons-apis >= 1.3.02
Obsoletes: recipnet
Obsoletes: recipnet-server
%description server
Reciprocal Net Site Network is a distributed database for molecular structures.
This site software package lets the server join the Site Network.  The package
is intended to be installed at a professional service crystallography 
laboratory (which normally would be run by a major university).  The package's
primary user interface is through the web (via Apache and Tomcat),  although it
also installs a dameon (recipnetd) and uses an underlying MySQL database.  It 
will be necessary for you to obtain a "site grant" file separately from the 
Reciprocal Net Coordinator in order to participate in the Site Network.  Please
see http://www.reciprocalnet.org/ for more details.  This 'server' package
contains the code for recipnetd, the Reciprocal Net daemon.  This package is
installed on exactly one computer at each "site".

%package webapp
Summary: Reciprocal Net site software web application
Group: Applications/Web
Requires: ghostscript >= 8.15.2
Requires: httpd >= 2.2.3
Requires: java-sun >= 1.6.0.11
Requires: java-sdk-sun >= 1.6.0.11
Requires: jaxp_transform_impl
Requires: libjpeg >= 6b-37
Requires: mod_proxy.so
Requires: mod_proxy_ajp.so
Requires: mod_rewrite.so
Requires: netpbm-progs >= 10.35
Requires: tomcat5 >= 5.5.23
Requires: xml-commons >= 1.3.02
Requires: xml-commons-apis >= 1.3.02
Obsoletes: recipnet-webapp
%description webapp
Reciprocal Net Site Network is a distributed database for molecular structures.
This web application connects to recipnetd, part of the recipnet-server
package.  The host running recipnetd may be either local or remote, and
potentially many computers at the same site may run recipnet-webapp at the same
time.

%package utils
Summary: Reciprocal Net site software utilities
Group: Utilities/System
Requires: java-sun >= 1.6.0.11
Obsoletes: recipnet-utils
%description utils
Reciprocal Net Site Network is a distributed database for molecular structures.
This set of command-line and graphical utilities connects to recipnetd, part of
the recipnet-server package.  The host running recipnetd may be either local or
remote.



###############################################################################
# PREP SECTION
#   this shell script runs at build time.  Currently the only thing we need
#   to do to prepare our sources is just unpack them.  (The %setup% macro does
#   this.)  We might insert patch statements here also.
###############################################################################
%prep
%setup -c recipnet-site-0.9.1
%patch0 -p1



###############################################################################
# BUILD SECTION
#   this shell script runs at build time and is responsible for actually
#   compiling our software.  We can assume that the current directory is the
#   base of our untar'ed source tree.  Property values of the form @value@ are
#   substituted by the build script at compile time.
###############################################################################
%build
ant site-server \
    -Ddist.site.name=recipnet-site \
    -Ddist.site.release=0.9.1 \
    -Ddist.site.buildnumber=50 \
#    -Ddist.site.signature-alias=signFiles \
#    -Ddist.site.signature-keystore=/mnt/recipnet-project/keys/javasign/jarsigner-keystore/keys \
#    -Ddist.site.signature-storepass=giep5afr



###############################################################################
# INSTALL SECTION
#   this shell script runs at build time.  It takes all the files that 
#   'ant site-server' produced in the BUILD SECTION above and copies them to
#   RPM_BUILD_ROOT, exactly where they should be installed on a real server.
#   These files will be subsequently packaged into the RPM.
###############################################################################
%install
# Create the install directory
if [ -d $RPM_BUILD_ROOT ]; then
    rm -rf $RPM_BUILD_ROOT
fi
mkdir $RPM_BUILD_ROOT

# The startup scripts for recipnetd go in /etc/rc.d/init.d
mkdir -p $RPM_BUILD_ROOT/etc/rc.d/init.d
cp native/scripts/recipnetd $RPM_BUILD_ROOT/etc/rc.d/init.d

# The daemon scripts for recipnetd go in /usr/bin.  Also all
#   of our other shell scripts and command-line utilities.
mkdir -p $RPM_BUILD_ROOT/usr/bin
cp native/scripts/drecipnet $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-authconnector $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-backup $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-checkerfixer $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-cifimporter $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-createreposdir $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-fixreposfile $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-getrepositorysize $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-httpddetect $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-ismdebug $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-mergeconfig $RPM_BUILD_ROOT/usr/bin
cp native/scripts/recipnet-samplestats $RPM_BUILD_ROOT/usr/bin
cp build/recipnet-ortep3 $RPM_BUILD_ROOT/usr/bin
cp build/recipnet-renderclient $RPM_BUILD_ROOT/usr/bin
cp build/recipnet-shortep $RPM_BUILD_ROOT/usr/bin
cp build/recipnet-vort2ppm $RPM_BUILD_ROOT/usr/bin

# The config files go in /etc/recipnet
mkdir -p $RPM_BUILD_ROOT/etc/recipnet
cp conf/recipnetd.conf $RPM_BUILD_ROOT/etc/recipnet
cp conf/recipnet-utils.conf $RPM_BUILD_ROOT/etc/recipnet

# The recipnet-webapp.conf file goes in /etc/httpd/conf.d
mkdir -p $RPM_BUILD_ROOT/etc/httpd/conf.d
cp conf/recipnet-webapp.conf $RPM_BUILD_ROOT/etc/httpd/conf.d
                                            
# JAR files go in /usr/share/java (but not the webapp jar)
mkdir -p $RPM_BUILD_ROOT/usr/share/java
cp build/recipnetd.jar $RPM_BUILD_ROOT/usr/share/java
cp build/recipnet-utils.jar $RPM_BUILD_ROOT/usr/share/java

# man files go in /usr/share/man
mkdir -p $RPM_BUILD_ROOT/usr/share/man/man1
cp build/shortep/man1/*.1.gz $RPM_BUILD_ROOT/usr/share/man/man1
mkdir -p $RPM_BUILD_ROOT/usr/share/man/man5
cp build/shortep/man5/*.5.gz $RPM_BUILD_ROOT/usr/share/man/man5

# Native run-time libraries go in /usr/lib
mkdir -p $RPM_BUILD_ROOT/usr/lib
cp build/librecipnet_server.so $RPM_BUILD_ROOT/usr/lib

# SELinux policy goes to /usr/share/selinux
mkdir -p $RPM_BUILD_ROOT/usr/share/selinux
cp build/recipnet_site_webapp.pp $RPM_BUILD_ROOT/usr/share/selinux

# All of the web content goes in Apache's html directory and subdirs
mkdir -p $RPM_BUILD_ROOT/var/www/html/recipnet
cp -R content/site/* $RPM_BUILD_ROOT/var/www/html/recipnet
cp build/embeddedversion/footer.jsp $RPM_BUILD_ROOT/var/www/html/recipnet
cp misc/jamm-runtime/element_data.txt \
    $RPM_BUILD_ROOT/var/www/html/recipnet/WEB-INF

mkdir -p $RPM_BUILD_ROOT/var/www/html/recipnet/applets/jamm
cp build/JaMM1.jar build/JaMM2.jar build/JaMMed.jar build/miniJaMM.jar \
    $RPM_BUILD_ROOT/var/www/html/recipnet/applets/jamm
cp misc/jamm-runtime/JaMM.help \
    $RPM_BUILD_ROOT/var/www/html/recipnet/applets/jamm
cp build/uploader.jar $RPM_BUILD_ROOT/var/www/html/recipnet/applets

mkdir -p $RPM_BUILD_ROOT/var/www/html/recipnet/WEB-INF/lib
cp build/recipnet-webapp.jar \
    $RPM_BUILD_ROOT/var/www/html/recipnet/WEB-INF/lib

mkdir -p $RPM_BUILD_ROOT/var/www/html
cp misc/site-example-index.html $RPM_BUILD_ROOT/var/www/html/



###############################################################################
# CLEAN SECTION
#   this shell script runs at build time.  It cleans up after the INSTALL
#   SECTION above and removes all those temporary files.
###############################################################################
%clean
rm -rf $RPM_BUILD_ROOT



###############################################################################
# FILES SECTION
#   this is a list of every file that needs to be distributed.  We also
#   set file permissions, etc. here.  All file names here are specified 
#   relative to the RPM_BUILD_ROOT that was used during the install stage.
#   There are three sections here, one for each subpackage.  Because there is
#   no plain %files element, no recipnet.rpm file will be generated.
###############################################################################
%files server
%attr(750, root, root) /etc/rc.d/init.d/recipnetd
%attr(750, root, recipnet) /usr/bin/drecipnet
%attr(750, root, root) /usr/bin/recipnet-backup
%attr(750, root, root) /usr/bin/recipnet-checkerfixer
%attr(750, root, recipnet) /usr/bin/recipnet-createreposdir
%attr(750, root, recipnet) /usr/bin/recipnet-fixreposfile
%attr(750, root, recipnet) /usr/bin/recipnet-getrepositorysize
%attr(750, root, root) /usr/bin/recipnet-ismdebug
%attr(750, root, root) /usr/bin/recipnet-mergeconfig
%attr(755, root, root) /usr/lib/librecipnet_server.so
%attr(640, root, recipnet) /usr/share/java/recipnetd.jar

# Note: if the 'noreplace' attribute is ever turned on for the config file then
# the recipnet-mergeconfig script will need to be updated:
%config %attr(600, recipnet, recipnet) /etc/recipnet/recipnetd.conf

%files webapp
%attr(755, root, root) /usr/bin/recipnet-authconnector
%attr(755, root, root) /usr/bin/recipnet-httpddetect
%attr(755, root, root) /usr/bin/recipnet-ortep3
%attr(755, root, root) /usr/bin/recipnet-renderclient
%attr(755, root, root) /usr/bin/recipnet-vort2ppm
%attr(644, root, root) /usr/share/selinux/recipnet_site_webapp.pp
%dir %attr(644, root, root) /var/www/html/recipnet/
%attr(644, root, root) /var/www/html/recipnet/*.jsp
%attr(644, root, root) /var/www/html/recipnet/*.css
%attr(644, root, root) /var/www/html/recipnet/*.html
%attr(644, root, root) /var/www/html/recipnet/admin/
%attr(644, root, root) /var/www/html/recipnet/applets/
%attr(644, root, root) /var/www/html/recipnet/help/
%attr(644, root, root) /var/www/html/recipnet/lab/
%dir %attr(644, root, root) /var/www/html/recipnet/WEB-INF/
%attr(644, root, root) /var/www/html/recipnet/WEB-INF/*.tld
%attr(644, root, root) /var/www/html/recipnet/WEB-INF/*.txt
%dir %attr(644, root, root) /var/www/html/recipnet/WEB-INF/lib/
%attr(644, root, root) /var/www/html/recipnet/WEB-INF/lib/recipnet-webapp.jar
%dir %attr(644, root, root) /var/www/html/recipnet/images/
# Exclude the site-sponsor image -- it's specified later as a config file:
%attr(644, root, root) /var/www/html/recipnet/images/[A-Za-rt-z]*
%attr(644, root, root) /var/www/html/recipnet/images/star.gif
%attr(644, root, root) /var/www/html/recipnet/images/status.gif
%config %attr(644, root, root) /var/www/html/recipnet/WEB-INF/web.xml
%config(noreplace) %attr(644, root, root) /var/www/html/recipnet/images/sitesponsor.gif
%config %attr(644, root, root) /etc/httpd/conf.d/recipnet-webapp.conf
%config(missingok) %attr(644, root, root) /var/www/html/site-example-index.html

%files utils
%attr(755, root, root) /usr/bin/recipnet-cifimporter
%attr(750, root, root) /usr/bin/recipnet-samplestats
%attr(755, root, root) /usr/bin/recipnet-shortep
%attr(644, root, root) /usr/share/java/recipnet-utils.jar
%attr(644, root, root) /usr/share/man/man1/recipnet-shortep.1.gz
%attr(644, root, root) /usr/share/man/man5/cif.5.gz
%attr(644, root, root) /usr/share/man/man5/crt.5.gz
%attr(644, root, root) /usr/share/man/man5/cssr.5.gz
%attr(644, root, root) /usr/share/man/man5/sdt.5.gz
%attr(644, root, root) /usr/share/man/man5/star.5.gz
%config %attr(644, root, root) /etc/recipnet/recipnet-utils.conf



###############################################################################
# PRE-INSTALL SECTION
#   this is a shell script that runs at install time on the user's computer,
#   just before RPM has copied files into the installation directory.
###############################################################################
%pre server

# Create the user 'recipnet' if it doesn't already exist.  This is needed so
#   that RPM's file-copies and owner-settings will go smoothly.
RECIPNET_USER=`cat /etc/passwd |grep recipnet`
if [ -z $RECIPNET_USER ]; then
    useradd -M -s /sbin/nologin -d /var/recipnet -G users recipnet
else
    # This part is necessary only for the transition from 0.6.2 to 0.9.0
    usermod -s /sbin/nologin recipnet
fi

# Stop recipnetd if the service is currently running
if [ -f /var/run/recipnetd.pid ]; then
    service recipnetd stop
    echo "  daemon recipnetd stopped"
fi

# Detect if an 'recipnetd.conf.rpmsave' file already exists
if [ -f /etc/recipnet/recipnetd.conf.rpmsave ]; then
    export RECIPNETRPM_SAVED_FILE_EXISTS=1
fi

#### One time upgrade tasks for 0.9.0: ####

# Get rid of rmiregistry cleanly.  No sense in printing an error message if
# rmiregistry is not installed.
chkconfig --del rmiregistry 2> /dev/null
# Stop rmiregistry if the service is currently running
if [ -f /var/run/rmiregistry.pid ]; then
    service rmiregistry stop
    echo "  daemon rmiregistry stopped"
fi

# Inform the SELinux security subsystem that HTTP's daemon is permitted to
# access repository data files and directories
if [ -d /var/recipnet/data ]; then
    if [ -x /usr/bin/chcon ]; then
        chcon system_u:object_r:httpd_sys_content_t:s0 /var/recipnet/data -R
    fi
fi



###############################################################################
# POST-INSTALL SECTION
#   this is a shell script that runs at install time on the user's computer,
#   just after RPM has copied files into the installation directory.
###############################################################################
%post server
# this is to handle the name change between 0.6.1 and 0.6.2. rpm is queried 
# with the former package name and if it is not found, and $1 reports that
# this is the first installation of recipnet-site-server, the install script is
# run, otherwise the upgrade script is run
rpm -q recipnet-server > /dev/null
if [ $? -eq 1 ] && [ $1 -eq 1 ]; then
    # This is a new installation.  Do lots of special stuff.
    #

    # Create our dynamic data directories if they don't already exist
    mkdir -p -m 775 /var/recipnet/data
    chown recipnet:users /var/recipnet/data
    if [ -x /usr/bin/chcon ]; then
        # Inform the SELinux security subsystem that HTTP's daemon is permitted
        # to access repository data files and directories
        chcon system_u:object_r:httpd_sys_content_t:s0 /var/recipnet/data -R
    fi
    mkdir -p -m 700 /var/recipnet/db
    chown mysql:mysql /var/recipnet/db
    if [ -x /usr/bin/chcon ]; then
        # Inform the SELinux security susbystem that MySQL's daemon is
        # permitted to access our database files.
        chcon root:object_r:mysqld_db_t:s0 /var/recipnet/db
    fi
    mkdir -p -m 700 /var/recipnet/msgs-recv
    chown recipnet:recipnet /var/recipnet/msgs-recv
    mkdir -p -m 700 /var/recipnet/msgs-sent
    chown recipnet:recipnet /var/recipnet/msgs-sent
    mkdir -p -m 700 /var/recipnet/msgs-held
    chown recipnet:recipnet /var/recipnet/msgs-held

    # Add a database directory to MySQL pointing to our data location
    if [ ! -d /var/lib/mysql/recipnet ]; then
        ln -s /var/recipnet/db /var/lib/mysql/recipnet
    fi
    chown mysql:mysql /var/lib/mysql/recipnet
    chmod 700 /var/lib/mysql/recipnet

    # Register the recipnetd startup scripts.  Let recipnetd auto-start by 
    # default.
    chkconfig --add recipnetd
    #chkconfig recipnetd on
else
    # If the recipnetd.conf file got overwritten by the upgrade, try to rescue
    # the database passwords from the old file.
    if [ -z $RECIPNETRPM_SAVED_FILE_EXISTS ]; then
        if [ -f /etc/recipnet/recipnetd.conf.rpmsave ]; then
            echo "migrating db passwords from old recipnetd.conf file (no" \
                 "other settings will be preserved)"
            SITPWLINE=`grep SitDbPassword /etc/recipnet/recipnetd.conf.rpmsave`
            SAMPWLINE=`grep SamDbPassword /etc/recipnet/recipnetd.conf.rpmsave`
            REPPWLINE=`grep RepDbPassword /etc/recipnet/recipnetd.conf.rpmsave`
            sed -e "s/SitDbPassword=.*/$SITPWLINE/" \
                -e "s/SamDbPassword=.*/$SAMPWLINE/" \
                -e "s/RepDbPassword=.*/$REPPWLINE/" \
                < /etc/recipnet/recipnetd.conf \
                > /etc/recipnet/recipnetd.conf.tmp
            rm -f /etc/recipnet/recipnetd.conf
            mv /etc/recipnet/recipnetd.conf.tmp /etc/recipnet/recipnetd.conf
        fi
    else
        unset $RECIPNETRPM_SAVED_FILE_EXISTS
    fi

    # Perform a database schema update because this is an existing installation
    /usr/bin/drecipnet update
    if [ $? -ne 0 ]; then
        echo "Error updating schema; you must run 'drecipnet update' again"
        echo "manually before starting recipnetd."
    fi

fi

%post webapp
# Fix permissions on web content
find /var/www/html/recipnet -type d -exec chmod +x {} \;
# Tell Tomcat how to find the recipnet web app
#ln -s -f /var/www/html/recipnet /var/lib/tomcat5/webapps/recipnet
#cp -pr /var/www/html/recipnet /var/lib/tomcat/webapps/recipnet
# Symlink lots of stuff into the /etc/recipnet directory
mkdir -p /etc/recipnet
ln -s -f /etc/httpd/conf.d/recipnet-webapp.conf \
         /etc/recipnet/recipnet-webapp.conf
ln -s -f /var/www/html/recipnet/images/sitesponsor.gif \
         /etc/recipnet/sitesponsor.gif
ln -s -f /var/www/html/recipnet/WEB-INF/web.xml \
         /etc/recipnet/web.xml
# Symlink the webapp jar file into /usr/share/java
ln -s -f /var/www/html/recipnet/WEB-INF/lib/recipnet-webapp.jar \
         /usr/share/java
# Symlink Xalan into the webapp's classpath
if [ ! -e /var/www/html/recipnet/WEB-INF/lib/xalan-j2.jar ]; then
#    ln -s -f /usr/share/java/xalan-j2.jar \
    cp -p /usr/share/java/xalan-j2.jar \
             /var/www/html/recipnet/WEB-INF/lib/xalan-j2.jar
fi
if [ ! -e /var/www/html/recipnet/WEB-INF/lib/xalan-j2-serializer.jar ]; then
#    ln -s -f /usr/share/java/xalan-j2-serializer.jar \
    cp -p /usr/share/java/xalan-j2-serializer.jar \
             /var/www/html/recipnet/WEB-INF/lib/xalan-j2-serializer.jar
fi
# Tell Tomcat how to find the recipnet web app
#ln -s -f /var/www/html/recipnet /var/lib/tomcat5/webapps/recipnet
cp -pr /var/www/html/recipnet /usr/local/tomcat/webapps/recipnet

# Install the SELinux policy module
if [ -x /usr/sbin/semodule ]; then
    semodule -i /usr/share/selinux/recipnet_site_webapp.pp
fi
# Deploy a sample index.html file
if [ ! -f /var/www/html/index.html ]; then
    mv /var/www/html/site-example-index.html /var/www/html/index.html
fi



###############################################################################
# PRE-UNINSTALL SECTION
#   this is a shell script that runs at uninstall time on the user's computer,
#   just before RPM has finished deleting all the files it knows about
###############################################################################
%preun server
# Do some cleanup if we're uninstalling the last installation
if [ $1 -eq 0 ]; then
     # deactivate our startup scripts
    chkconfig --del recipnetd
fi
# Stop recipnetd if the service is currently running
if [ -f /var/run/recipnetd.pid ]; then
    service recipnetd stop
    echo "  daemon recipnetd stopped"
fi

%preun webapp
# Do some cleanup to prepare for clean uninstall of the last installation
if [ $1 -eq 0 ]; then
    rm -f /var/www/html/recipnet/WEB-INF/lib/xalan-j2.jar
    rm -f /var/www/html/recipnet/WEB-INF/lib/xalan-j2-serializer.jar    
    if [ -x /usr/sbin/semodule ]; then
        semodule -r recipnet_site_webapp
    fi
fi



###############################################################################
# POST-UNINSTALL SECTION
#   this is a shell script that runs at uninstall time on the user's computer,
#   just after RPM has finished deleting all the files it knows about
###############################################################################
%postun server
# Do lots of cleanup if we're uninstalling the last installation
if [ $1 -eq 0 ]; then
    # remove all the directories in /var
    rmdir --ignore-fail-on-non-empty /var/recipnet/data
    rmdir --ignore-fail-on-non-empty /var/recipnet/db
    rm -rf /var/recipnet/msgs-recv
    rmdir --ignore-fail-on-non-empty /var/recipnet/msgs-sent
    rm -rf /var/recipnet/msgs-held
    rmdir --ignore-fail-on-non-empty /var/recipnet

    # remove the database symlink for MySQL
    rm -f /var/lib/mysql/recipnet

    # delete the 'recipnet' user 
    userdel recipnet
fi

%postun webapp
# Do some cleanup if we're uninstalling the last installation
if [ $1 -eq 0 ]; then
    # remove all the content from the recipnet tree
    rm -rf /var/www/html/recipnet

    # remove all the symlinks we created at install time
    #rm -f /var/tomcat4/webapps/recipnet
    rm -rf /var/lib/tomcat/webapps/recipnet
    rm -f /etc/recipnet/recipnet-webapp.conf
    rm -f /etc/recipnet/sitesponsor.gif
    rm -f /etc/recipnet/web.xml
    rm -f /usr/share/java/recipnet-webapp.jar
fi
