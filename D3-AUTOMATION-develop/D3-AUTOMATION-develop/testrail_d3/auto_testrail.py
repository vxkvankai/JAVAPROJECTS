import argparse
import json
import sys

import requests

from create_test_run import create_test_run
from milestones import get_milestone_id_by_name, MileStoneNotFoundException, create_milestone
from test_case_helpers import get_test_case_list

parser = argparse.ArgumentParser()
parser.add_argument('client', help='Which client to run the suite against',
                    choices=['Arvest', 'Synovus', 'Demo', 'TCF', 'Core', 'FTB'])
parser.add_argument('version', help='Which version of D3: e.g. 3.3.1, 3.3.0, 3.3.0-rc1')
parser.add_argument('type', help='Which type of test suite', choices=['Smoke', 'Regression'])
args = parser.parse_args()

version_to_look_up = args.version.split('-')[0]

try:
    filename = '{}_{}.txt'.format(args.client.lower(), args.type.lower())
    print(f'Getting the test case list for filename: {filename}')
    list_of_ids = get_test_case_list(filename)
    print('Test case ids retrieved')
except FileNotFoundError:
    sys.exit(f'{filename} not found')

milestone_id = -1
try:
    print(f'Attempting to find the milestone: {version_to_look_up} - {args.client}')
    milestone_id = get_milestone_id_by_name(f'{version_to_look_up} - {args.client}')
    print(f'Milestone found: {milestone_id}')
except MileStoneNotFoundException:
    print('Milestone not found, attempting to create new sub milestone')
    super_milestone_id = get_milestone_id_by_name(f'{version_to_look_up}')
    milestone_id = create_milestone(f'{version_to_look_up} - {args.client}', super_milestone_id).get('id')
    print(f'Milestone created successfully, id: {milestone_id}')

if milestone_id != -1 and list_of_ids != []:
    print('Creating test run')
    test_run_name = f'{args.version} - {args.client} {args.type}'
    new_url = create_test_run(test_run_name, list_of_ids, milestone_id=milestone_id).get('url')

    slack_data = {'text': f'New Test Run Created for {test_run_name}: {new_url}'}
    r = requests.post('https://hooks.slack.com/services/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',
                      data=json.dumps(slack_data), headers={'Content-Type': 'application/json'})

else:
    sys.exit(f'Not creating test run: \nmilestone_id:{milestone_id}\nlist of ids to add: {list_of_ids}')
