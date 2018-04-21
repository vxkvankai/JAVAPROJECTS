from api_client import get_api_client, get_project_id
from testrail import APIError


def find_duplicates(list_one: list, list_two: list) -> set:
    """
    Find duplicates in two lists

    :param list_one: 1st list to compare
    :param list_two: 2nd list to compare
    :return: A set object of the intersection of the two
    """
    return set(list_one).intersection(list_two)


def get_test_case_list(file_name: str) -> list:
    """
    Get the list of test cases from a file. Each test case ID should be seperated by a line break (\n) and may or may not have a C marker
    in the front of the TC number

    :param file_name: Name of the file in the configurations folder to read the list of test cases from
    :return: A list of TestCase ids (with no 'C' marker), empty list if file was not found
    :raises FileNotFoundError: If the file is not found
    """

    with open(f'configurations/{file_name}') as file:
        test_cases = [line.rstrip('\n').rstrip().lstrip('C').lstrip('T') for line in file]

    return test_cases


def get_test_case_details(case_id: int) -> dict:
    """
    Get the details of a specific test case given the id

    :param case_id: Id of the test case to look up
    :return: the JSON response
    """
    client = get_api_client()
    return client.send_get(f'/get_case/{case_id}')


def get_project_suites(project_id: int) -> list:
    """
    Get the test suites of a project

    :param project_id: Id of the project to look up
    :return: A list of suite information
    """
    client = get_api_client()
    suites = []
    for suite in client.send_get(f'/get_suites/{project_id}'):
        suites.append(suite.get('id'))
    return suites


if __name__ == '__main__':
    regression = get_test_case_list('core_regression.txt')

    reg_auto = get_test_case_list('core_reg_auto.txt')
    dups = find_duplicates(regression, reg_auto)
    auto_only = set(reg_auto).difference(dups)
    for test in auto_only:
        print(f'C{test}')

    suites = get_project_suites(get_project_id())

    for test in regression:
        try:
            test_details = get_test_case_details(test)
            if test_details.get('suite_id') not in suites:
                print('id: ' + str(test_details.get('id')))
        except APIError as e:
            print(e)
            print("Error test: " + str(test))
