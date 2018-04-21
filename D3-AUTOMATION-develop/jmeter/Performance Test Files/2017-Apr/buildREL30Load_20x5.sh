#!/bin/bash



~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-users.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170401

~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170401
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170402
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170403
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170404
~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-accts.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170405

~/dev/tools/apache-jmeter-3.1/bin/jmeter -n -t conduit-load-mm.jmx -p REL30_Load_Test_20x5.config -J fileDateString 20170401
