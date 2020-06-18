# CSV connector
It's build on top of ConnId framework. Supported versions of framework is 1.4.3.0

For usage follow this [tutorial](https://wiki.czechidm.com/tutorial/adm/connector_csv_-_how_to_connect_csv_connector). 

## Connector supports:
* Reading CSV file
* Reconciliation of users
* Test connector
* Generate schema

## Development rules:
### Assignment
Create a specification page in private section and consult it with module owner and other colleagues. Specification page should contain:
* Usecases - why we or user need this feature, what problem does it solve?
* Functional specification - how should it work, edge cases
* Create ticket in Redmine with final requirements and with correct target version
* Implement the feature in a separate GIT branch
* Create merge request to develop
* Get someone from product team, or module owner to review your changes
* After successfull review, ask module owner to merge you code

### Rules for code review:
* Added feature have to have at least 80% code coverage
* All changes to main functionality have to be in this [tutorial](https://wiki.czechidm.com/tutorial/adm/connector_csv_-_how_to_connect_csv_connector)
* Changelog is updated
