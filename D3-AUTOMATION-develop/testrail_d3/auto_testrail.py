import argparse

from create_test_run import create_test_run
from milestones import get_milestone_id_by_name, NoMileStoneFoundException, create_milestone
from test_case_helpers import get_test_case_list

parser = argparse.ArgumentParser()
parser.add_argument('client', help='Which client to run the suite against', choices=['Synovus', 'Demo', 'TCF', 'Core'])
parser.add_argument('version', help='Which version of D3: e.g. 3.3.1, 3.3.0, 3.3.0-rc1')
parser.add_argument('type', help='Which type of test suite', choices=['smoke', 'regression'])
args = parser.parse_args()

version_to_look_up = args.version.split('-')[0]

milestone_id = -1
try:
    milestone_id = get_milestone_id_by_name(f'{version_to_look_up} - {args.client}')
except NoMileStoneFoundException:
    print('Milestone not found, attempting to create new sub milestone')
    super_milestone_id = get_milestone_id_by_name(f'{version_to_look_up}')
    milestone_id = create_milestone(f'{version_to_look_up} - {args.client}', super_milestone_id).get('id')

list_of_ids = get_test_case_list('{}_{}.txt'.format(args.client.lower(), args.type.lower()))

if milestone_id != -1 and list_of_ids != []:
    create_test_run(f'{args.version} - {args.client} {args.type}', list_of_ids, milestone_id=milestone_id)
else:
    print('Not creating test run')
