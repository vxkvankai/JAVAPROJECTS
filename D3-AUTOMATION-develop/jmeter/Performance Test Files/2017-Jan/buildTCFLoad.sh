#!/bin/bash

jmeter -n -t conduit-load-users-nodb.jmx -p TCF_Perf_Test.config -J fileDateString=20170117 -J sourceCompanyId=perf_1 $* &

jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Perf_Test.config -J fileDateString=20170117 -J suppressAlerts=true $* &
jmeter -n -t conduit-load-accts-nodb.jmx -p TCF_Perf_Test.config -J fileDateString=20170118 $* &
