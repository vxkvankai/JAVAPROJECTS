import configparser

from testrail import APIClient


def get_api_client() -> APIClient:
    """
    Get the TestRail configuration and client by reading the secrets.ini file

    :return: The TestRail APIClient
    """

    config = configparser.ConfigParser()
    config.read('secrets.ini')

    client = APIClient('https://d3banking.testrail.com')
    client.user = config['DEFAULT']['user']
    client.password = config['DEFAULT']['password']

    return client


def get_project_id() -> int:
    """
    Returns the project id that will be used for all the calls
    :return: ID of the project to add/edit/delete items in TestRail from
    """
    # 14 is the D3 Quality Assurance project
    return 14
