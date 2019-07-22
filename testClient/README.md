Default writers add value==1

# Task

The test client should be able to launch several concurrent streams on a defined subset of identifiers
- rCount – number of readers requesting the getAmount(id) method
- wCount – number of readers requesting the addAmount(id,value) method
- idList – list or range of keys that will be used for testing
These parameters can be set using the command line or configuration file
Several test clients can be launched simultaneously on one or several computers.