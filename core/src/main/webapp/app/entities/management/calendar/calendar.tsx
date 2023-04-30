import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICalendar } from 'app/shared/model/management/calendar.model';
import { getEntities } from './calendar.reducer';

export const Calendar = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const calendarList = useAppSelector(state => state.core.calendar.entities);
  const loading = useAppSelector(state => state.core.calendar.loading);
  const totalItems = useAppSelector(state => state.core.calendar.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="calendar-heading" data-cy="CalendarHeading">
        <Translate contentKey="coreApp.managementCalendar.home.title">Calendars</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coreApp.managementCalendar.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/calendar/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coreApp.managementCalendar.home.createLabel">Create new Calendar</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {calendarList && calendarList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coreApp.managementCalendar.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lactation')}>
                  <Translate contentKey="coreApp.managementCalendar.lactation">Lactation</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('jrsLact')}>
                  <Translate contentKey="coreApp.managementCalendar.jrsLact">Jrs Lact</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('statutReproduction')}>
                  <Translate contentKey="coreApp.managementCalendar.statutReproduction">Statut Reproduction</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('etatProd')}>
                  <Translate contentKey="coreApp.managementCalendar.etatProd">Etat Prod</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateNaissance')}>
                  <Translate contentKey="coreApp.managementCalendar.dateNaissance">Date Naissance</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('velage')}>
                  <Translate contentKey="coreApp.managementCalendar.velage">Velage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('chaleur')}>
                  <Translate contentKey="coreApp.managementCalendar.chaleur">Chaleur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('insemination')}>
                  <Translate contentKey="coreApp.managementCalendar.insemination">Insemination</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('cowId')}>
                  <Translate contentKey="coreApp.managementCalendar.cowId">Cow Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {calendarList.map((calendar, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/calendar/${calendar.id}`} color="link" size="sm">
                      {calendar.id}
                    </Button>
                  </td>
                  <td>{calendar.lactation}</td>
                  <td>{calendar.jrsLact}</td>
                  <td>{calendar.statutReproduction}</td>
                  <td>{calendar.etatProd}</td>
                  <td>
                    {calendar.dateNaissance ? <TextFormat type="date" value={calendar.dateNaissance} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{calendar.velage ? <TextFormat type="date" value={calendar.velage} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{calendar.chaleur ? <TextFormat type="date" value={calendar.chaleur} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {calendar.insemination ? <TextFormat type="date" value={calendar.insemination} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{calendar.cowId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/calendar/${calendar.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/calendar/${calendar.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/calendar/${calendar.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coreApp.managementCalendar.home.notFound">No Calendars found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={calendarList && calendarList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Calendar;
