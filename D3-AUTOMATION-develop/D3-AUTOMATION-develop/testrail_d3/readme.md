# Python TestRail Automation

## Requirements

- Python 3.6
- Install reqs via `pip install -r requirements.txt` (or use docker)

## Instructions

- Create an api key in TestRail to use (don't forget to click save)
- Copy the `secrets_example.ini` file to a new file called `secrets.ini`
- Change the user to your TestRail user
- Change the api key value in the file to the new key created in TestRail

## Running

Main entry point is via `auto_testrail.py`, but  if you just want to run the script,
easiest to just use docker:
See  `python3 auto_testrail.py -h` for list of options

```
docker build -t test_rail_script .
docker run --rm --name test_rail_script test_rail_script [client] [version] [type]
```
