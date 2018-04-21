from api_client import get_api_client, get_project_id
from test_case_helpers import get_test_case_list


def create_test_run(name: str, test_cases: list, milestone_id: int = 0, assignee_id: int = 0) -> dict:
    """
    Create a test run in TestRail

    :param name: Name of the test run to be created
    :param test_cases: List of test case id's to add to the test run
    :param milestone_id: ID of the milestone to associate the test run to (default: no association)
    :param assignee_id: ID of the user to assign the test run to (default: not assigned)
    :return: A dictionary of the return JSON data
    """

    run_data = {
        'name': name,
        'include_all': False,  # <- default is true here
        'case_ids': test_cases
    }

    if milestone_id > 0:
        run_data['milestone_id'] = milestone_id
    if assignee_id > 0:
        run_data['assignedto_id'] = assignee_id

    client = get_api_client()
    project_id = get_project_id()  # NOSONAR (Used below in f string)

    return client.send_post(f'add_run/{project_id}', run_data)


if __name__ == '__main__':
    create_test_run('3.3.1-rc2 - Synovus Smoke', get_test_case_list('synovus_smoke.txt'), milestone_id=103)

