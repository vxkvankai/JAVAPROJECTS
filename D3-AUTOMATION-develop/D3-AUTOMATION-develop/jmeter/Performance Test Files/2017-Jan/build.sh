cp ~/Downloads/apache-jmeter-3.1.tgz .
docker build -t d3banking/saas:tcf_perf_test_jmeter .
rm apache-jmeter-3.1.tgz
