from api_client import get_api_client, get_project_id


def create_milestone(name: str, milestone_id: int = 0) -> dict:
    """
    Create a milestone (or submilestone)

    :param name: Name of the (sub)milestone to be created
    :param milestone_id: Parent milestone id, default is 0 (no parent), note that only one level of submilestones can be created
    :return: Dictionary of the server response
    """
    milestone_data = {
        'name': name,
    }

    if milestone_id > 0:
        milestone_data['parent_id'] = milestone_id

    client = get_api_client()
    project_id = get_project_id()  # NOSONAR (used below in f string)

    return client.send_post(f'add_milestone/{project_id}', milestone_data)


def get_milestone_id_by_name(name: str) -> int:
    """
    Get a milestone id given the name of the milestone

    :param name: Name of the milestone
    :return: Id of the milestone
    :raises: NoMileStoneFoundException if the name doesn't match any milestone
    """
    client = get_api_client()
    project_id = get_project_id()  # NOSONAR (used below in f string)

    milestones = client.send_get(f'get_milestones/{project_id}')
    # check if top level has it
    for milestone in milestones:
        if milestone.get('name') == name:
            return milestone.get('id')

        # check if sub level milestone exists
        sub_milestones = client.send_get('get_milestone/{}'.format(milestone.get('id')))
        for sub_milestone in sub_milestones.get('milestones'):
            if sub_milestone.get('name') == name:
                return sub_milestone.get('id')

    raise MileStoneNotFoundException(f'Milestone not found: {name}')


class MileStoneNotFoundException(Exception):
    pass


if __name__ == '__main__':
    print(get_milestone_id_by_name("3.3.1 - Core"))
