from api_client import get_api_client
from create_test_run import get_test_case_list

client = get_api_client()


def get_case_id_from_test_id(test_id: int) -> int:
    """
    Get a case id value from a test id value in test rail.
    Test_Id in Testrail refers to a test run's copy/version of a test case. These are created for every new test run, and results are
    associated with the test_id. case_id refers to the actual test case that is stored in test rail. There is only 1 copy of this and is
    used to create test runs or reference test cases. No results are associated with the test case id directly (although you can get a list of results
    from all test runs that have this case id).
    :param test_id: TestId to send to query TestRail.
    :return: CaseId from TestRail that the TestId is associated with
    """
    return client.send_get(f'get_test/{test_id}').get('case_id')


def get_case_ids_from_run_ids(test_ids: list) -> list:
    """
    Get a list of case_ids from a list of test_ids. This new list can then be used to create a test run or other functions.
    :param test_ids: list of ints to send to test rail. These cannot have the T marker on them
    :return: A list of case_ids (no C marker)
    """
    new_list = []
    for test_id in test_ids:
        new_list.append(get_case_id_from_test_id(test_id))
    return new_list


if __name__ == '__main__':
    ids = get_test_case_list('demo_smoke_run_ids.txt')
    list_of_tests = get_case_ids_from_run_ids(ids)
    with open(f'configurations/demo_smoke.txt', "w+") as file:
        for test in list_of_tests:
            file.write(f'{test}\n')
