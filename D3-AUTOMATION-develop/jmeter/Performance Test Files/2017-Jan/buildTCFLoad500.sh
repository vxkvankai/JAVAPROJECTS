#!/bin/bash
###################################################################################
##
##  Potentially, the most important property in the props file is "numOfGroups"
##    -- it will control the number of loops each jmeter run does.  I suggest
##    -- you set it to 1 for your first run
##
###################################################################################
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-users-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170117 &

/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170117 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170118 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170119 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170120 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170121 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170122 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170123 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170124 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170125 &
/root/TCF_Load_Test/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Load_Test500MB.config -J fileDateString 20170126 &
