## Performance Testing

## Data load testing (conduit testing)
Prerequisite: You will need to have d3 seed executed and d3conduitapp running in your local tomcat. 
1. Run `conduit-settings-perf_testing.xml`
2. Run `conduit-load-accts-nodb.jmx` to generate conduit account/transactions files
3. Use above to run through conduit
4. Run `conduit-load-users-nodb.jmx` to generate user and account association files
5. Use above to run through conduit

NOTE: there is a property file that can be used with jmeter scripts to execute jmx files in command line.

## User access testing (api testing)
Prerequisite: You will need to have d3rest-app, d3alerts-app and d3conduitapp running in your local tomcat
1. Run `login-load-2-6.jmx` (lots of users) or `perf_test_login.jmx` (random small number of users) against the API (adjust number of users via properties)

## Shell scripts
Various .sh files were created to automate running some of the jmx scripts. 
