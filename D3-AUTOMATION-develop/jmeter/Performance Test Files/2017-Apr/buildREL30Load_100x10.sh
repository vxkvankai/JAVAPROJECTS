#!/bin/bash

# 1M users in 10 files

~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-users.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170424

# 2M accounts in 20 files, first 10 tx/acct in this set of files

~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170424

# additional 40 txns/account to raise total to 50 txns/account

~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170420 -J txnsOnly=true -J suppressAlerts=true
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170421 -J txnsOnly=true -J suppressAlerts=true
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170422 -J txnsOnly=true -J suppressAlerts=true
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170423 -J txnsOnly=true -J suppressAlerts=true

# new 3.0 MM recipients and transfers

~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-mm.jmx -p REL30_Load_Test_100x10.config -J fileDateString 20170424
